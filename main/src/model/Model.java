package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import javax.swing.Timer;


/**
 * Zentrales Model (Observable) der Anwendung.
 * Verwaltet den {@link GameState}, Screen-Wechsel, Quiz-Logik sowie Fortschritt/Events
 * via {@link PropertyChangeSupport}.
 */
public class Model {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private final GameState gameState;

    private Question currentQuestion;
    private EnumScreen currentQuizRoomType;
    private Quiz currentQuiz;

    private int progress = 0;
    private boolean returnToHubAfterCpuOutro = false;

    /**
     * Erstellt ein neues Model mit initialisiertem {@link GameState}.
     */
    public Model() {
        this.gameState = new GameState();
    }

    public GameState getGameState() {
        return gameState;
    }

    List<Room> getRooms() {
        return gameState.getRoomOverview();
    }


    /**
     * Registriert einen PropertyChangeListener.
     *
     * @param listener Listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * Wechselt zum angegebenen Screen. Prüft, ob Introtext angezeigt werden soll.
     *
     * @param newScreen
     */
    void changeScreen(Screen newScreen) {
        gameState.changeScreen(newScreen);

        // Introtext einmalig anzeigen
        if (!newScreen.isIntroShown()
                && newScreen.getIntroText() != null
                && !newScreen.getIntroText().isBlank()) {
            newScreen.setIntroText(personalize(newScreen.getIntroText()));
            newScreen.setShowIntro(true);
        } else {
            newScreen.setShowIntro(false);
        }

        pcs.firePropertyChange("screen", null, newScreen);
    }

    /**
     * Öffnet einen Raum anhand seines Titels.
     * Zeigt beim ersten Betreten ggf. den Intro-Storytext des Raums an
     * und schaltet dann zum nächsten Screen weiter.
     *
     * @param roomTitle Titel des Raums (EnumScreen.name())
     */
    public void enterRoom(String roomTitle) {
        Room room = null;
        for (Room r : gameState.getRoomOverview()) { //Raum anhand des Titels suchen
            if (r.getTitle().name().equals(roomTitle)) {
                room = r;
                break;
            }
        }

        if (!room.isOpen()) {
            showError("Zugriff verweigert! Das Virus blockiert diesen Teil des Computers.");
            return; // im Hub bleiben
        }

        changeScreen(room);
    }

    /**
     * Wechselt zurück zur Hub-Ansicht.
     * Dazu wird in der Liste der verfügbaren Screens ein Schritt
     * zurückgegangen. Am Anfang der Liste passiert nichts.
     */
    public void returnToHub() {
        var screens = gameState.getAvailableScreens();
        var current = gameState.getCurrentScreen();

        int idx = screens.indexOf(current);
        if (idx <= 0) return;          // ganz vorne -> nichts tun

        Screen previous = screens.get(idx - 1);  // in der availableScreenList steht immer der zugehörige Hub davor
        changeScreen(previous);
    }

    /**
     * Sucht in der Room-Übersicht den Raum mit dem angegebenen EnumScreen-Typ.
     *
     * @param roomType der gesuchte Raumtyp (EnumScreen)
     * @return der passende Room oder null, falls keiner gefunden wurde
     */
    private Room findRoomByType(EnumScreen roomType) {
        for (Room r : gameState.getRoomOverview()) {
            if (r.getTitle() == roomType) {
                return r;
            }
        }
        return null;
    }

    /**
     * Ersetzt Platzhalter im Storytext durch spielerspezifische Daten.
     * Momentan wird nur {BENUTZERNAME} durch den im Spiel gesetzten Benutzernamen ersetzt.
     *
     * @param text der ursprüngliche Storytext mit Platzhaltern
     * @return der personalisierte Text
     */
    private String personalize(String text) {
        return text.replace("{BENUTZERNAME}", gameState.getUsername());
    }


    /**
     * Startet ein Quiz in einem bestimmten Raum.
     * Prüft, ob das Quiz freigeschaltet ist und noch nicht beendet wurde.
     * Informiert die View über die anzuzeigende Frage per "quizShown".
     *
     * @param roomType  der Raum, zu dem das Quiz gehört
     * @param quizIndex Index des Quizzes im Raum
     */
    public void startQuizForRoom(EnumScreen roomType, int quizIndex) {
        currentQuizRoomType = roomType;

        // passenden Raum finden
        Room foundRoom = null;
        for (Room r : gameState.getRoomOverview()) {
            if (r.getTitle() == roomType) {
                foundRoom = r;
                break;
            }
        }

        if (foundRoom == null) {
            return;
        }

        // Quizze nur in der erlaubten Reihenfolge spielen lassen
        if (quizIndex > foundRoom.getHighestUnlockedQuizIndex()) {
            showError( "Zugriff verweigert! Das Virus blockiert dieses Modul. "
                    + "Sichere zuerst die vorherige Einheit.");
            return;
        }

        // Quiz-Index setzen
        foundRoom.setCurrentQuizIndex(quizIndex);

        // aktuelles Quiz holen
        Quiz quiz = foundRoom.getCurrentQuiz();
        if (quiz == null) {
            return;
        }
        if (quiz.isFinished()) {
            showError("Dieser Bereich des Computers wurde bereits erfolgreich vom Virus befreit. " +
                    "Kümmere dich um den nächsten Bereich!");
            return;
        }

        currentQuiz = quiz;

        // aktuelle Frage holen
        Question qFromQuiz = quiz.getCurrentQuestion();
        if (qFromQuiz == null) {
            return;
        }

        currentQuestion = qFromQuiz;
        pcs.firePropertyChange("quizShown", null, currentQuestion);
    }

    /**
     * Verarbeitet einen Klick auf eine Quiz-Antwort.
     * Prüft, ob die Antwort korrekt ist, schaltet ggf. zur nächsten Frage
     * oder beendet das Quiz. Schaltet bei Quiz-Ende das nächste Quiz frei
     * und zeigt ggf. den Outro-Text an.
     *
     * @param actionCommand ActionCommand des Antwort-Buttons (z.B. "QUIZ_ANSWER_0")
     */
    public void handleQuizAnswer(String actionCommand) {
        int chosenIndex = Integer.parseInt(
                actionCommand.substring("QUIZ_ANSWER_".length())
        );

        boolean correct = (chosenIndex == currentQuestion.getCorrectIndex());

        if (correct) {
            System.out.println("Richtige Antwort!");
            pcs.firePropertyChange("correctAnswer", null, chosenIndex);

            // 2 Sekunden warten, dann erst weiterschalten
            Timer t = new Timer(2000, e -> {

                // Sicherheitsnetz
                if (currentQuiz == null) {
                    pcs.firePropertyChange("quizHidden", true, false);
                    return;
                }

                // letzte Frage?
                if (currentQuiz.isCompleted()) {
                    pcs.firePropertyChange("progress", progress, progress + 1);
                    progress++;

                    pcs.firePropertyChange("quizHidden", true, false);
                    currentQuiz.markFinished();

                    Room room = findRoomByType(currentQuizRoomType);
                    if (room != null) {
                        List<Quiz> quizzesInRoom = room.getQuizzes();
                        int quizIndex = quizzesInRoom.indexOf(currentQuiz);

                        // nächstes Quiz freischalten
                        if (quizIndex == room.getHighestUnlockedQuizIndex()
                                && quizIndex < quizzesInRoom.size() - 1) {
                            room.unlockNextQuiz();
                        }

                        boolean isLastQuizInRoom = (quizIndex == quizzesInRoom.size() - 1);
                        if (isLastQuizInRoom) {
                            completeRoom(room);
                        }
                    }
                    return;
                }

                // sonst nächste Frage
                currentQuiz.nextQuestion();
                Question next = currentQuiz.getCurrentQuestion();

                if (next != null) {
                    currentQuestion = next;
                    pcs.firePropertyChange("quizShown", null, currentQuestion);
                } else {
                    pcs.firePropertyChange("quizHidden", true, false);
                }

            });

            t.setRepeats(false);
            t.start();

            return; // wichtig: sofort raus, sonst läuft else/weiterer Code weiter
        }

        // falsche Antwort
        System.out.println("Falsche Antwort!");
        pcs.firePropertyChange("incorrectAnswer", null, chosenIndex);
        gameState.incrementWrongAnswers();
    }


    /**
     * Markiert einen Raum als abgeschlossen und löst ein Event aus.
     * Prüft anschließend, ob alle Räume beendet wurden.
     *
     * @param room der abgeschlossene Raum
     */
    void completeRoom(Room room) {
        room.setCompleted(true);
        // Nächsten Raum freischalten (Reihenfolge wie in roomOverview)
        List<Room> rooms = gameState.getRoomOverview();
        int idx = rooms.indexOf(room);

        if (idx >= 0 && idx < rooms.size() - 1) {
            rooms.get(idx + 1).setOpen(true);
        }

        if (room.getTitle() == EnumScreen.CPURoom) {
            returnToHubAfterCpuOutro = true;
        }

        System.out.println("Bisherige Gesamtzahl falscher Antworten: "
                + gameState.getTotalWrongAnswers());

        // Outro-Text anzeigen
        if (!room.isOutroShown()
                && room.getOutroText() != null
                && !room.getOutroText().isBlank()) {
            room.setShowOutro(true);
            room.setOutroText(personalize(room.getOutroText()));
            room.setBackgroundImagePath(room.getOutroImagePath()); // Outro-Bild als Hintergrund setzen
            changeScreen(room);
        }

    }
    /**
     * Wird genutzt wenn ein Overlay geschlossen wurde.
     * Wird u.a. nach dem CPU-Outro genutzt, um zurück zum Hub zu wechseln
     * und das Spielende-Event zu feuern.
     */
    public void overlayClosed() {
        if (!returnToHubAfterCpuOutro) return;

        //Spielende Sonderfall:
        returnToHubAfterCpuOutro = false;

        // zurück zum Hub
        Screen HubEnd = gameState.getScreenByTitle(EnumScreen.Hub);
        HubEnd.setBackgroundImagePath(HubEnd.getOutroImagePath());
        changeScreen(HubEnd);

        // Fehlerzahl an den Controller melden
        pcs.firePropertyChange("gameCompleted", null, gameState.getTotalWrongAnswers());
    }

    public void setStartState() {
        changeScreen(getGameState().getAvailableScreens().get(0));
    }

    public void cancelQuiz() {
        pcs.firePropertyChange("quizHidden", false, true);
    }

    /**
     * Schaltet zum nächsten Screen aus der Liste availableScreens in gameState. Dazu wird currentScreen in der Liste gesucht und dann der nächste Screen ermittelt.
     */
    public void nextScreen() {
        int nextIdx = gameState.getAvailableScreens().indexOf(gameState.getCurrentScreen());
        nextIdx++;
        changeScreen(gameState.getAvailableScreens().get(nextIdx));
    }

    void showError(String sError) {
        gameState.getCurrentScreen().setErrorMessage(sError);
        changeScreen(gameState.getCurrentScreen());
    }

    /**
     * Validiert die Login-Eingaben (Benutzername + Schwierigkeitsgrad).
     * Speichert die Daten im GameState, legt einen neuen Spieler in der Datenbank an
     * und wechselt zum nächsten Screen. Zeigt bei leerem Namen oder wenn ";" enthalten ist (SQL Injection) einen Fehler an.
     *
     * @param username   eingegebener Benutzername
     * @param difficulty ausgewählte Schwierigkeitsstufe
     */
    public void validateLogin(String username, EnumDifficulty difficulty) {
        if (!username.isBlank() && !username.contains(";")) {
            gameState.setUsername(username);
            gameState.setDifficulty(difficulty);
            gameState.getCurrentScreen().clearErrorMessage();
            gameState.initQuizzesForDifficulty(difficulty);

            // Räume resetten: alle zu, nur der erste offen
            for (Room r : gameState.getRoomOverview()) {
                r.setOpen(false); // auf true setzten für Testbetrieb um in alle Räume sofort gehen zu können
            }
            gameState.getRoomOverview().get(0).setOpen(true); // z.B. erster Raum (GraphicRoom)

            // Fehlversuchszähler für ein neues Spiel zurücksetzen
            gameState.resetWrongAnswers();

            // JDBC Driver registieren
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to load SQLite JDBC driver", e);
            }

            // Spielername zur Datenbank hinzufügen
            try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:sqlite:ExitGame.sqlite");
                 java.sql.PreparedStatement pstmt = conn.prepareStatement("INSERT INTO spieler (name) VALUES (?);")) {

                pstmt.setString(1, username);
                pstmt.executeUpdate();

            } catch (java.sql.SQLException e) {
                e.printStackTrace(); // Log or handle the SQL exception
            }

            nextScreen();
        } else {
            showError("Ungültiger Benutzername");
        }
    }
}
