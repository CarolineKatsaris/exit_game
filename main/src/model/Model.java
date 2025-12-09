package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

// observable
public class Model {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private GameState gameState;
    private Question currentQuestion;
    private EnumScreen currentQuizRoomType;
    private Quiz currentQuiz;

    public Model() {
        this.gameState = new GameState();
    }
    public GameState getGameState() {
        return gameState;
    }

    List<Room> getRooms() {
        return gameState.getRoomOverview();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * Wechselt zum angegebenen Screen. Prüft, ob Introtext angezeigt werden soll.
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
        } else {newScreen.setShowIntro(false);}

        pcs.firePropertyChange("screen", null, newScreen);
    }

    /**
     * Methode um Räume zu öffen, später evtl. überarbeiten (locked/unlocked)
     * Nutzt die Reihenfolge in availableScreens: Start -> Login -> Hub -> (erster Raum) -> ...
     * Öffnet einen Raum anhand seines Titels.
     * Zeigt beim ersten Betreten den Intro-Storytext des Raums an
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
     * @param roomType   der Raum, zu dem das Quiz gehört
     * @param quizIndex  Index des Quizzes im Raum
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
            System.out.println("Quiz " + quizIndex + " ist noch gesperrt! "
                    + "Freigeschaltet bis: " + foundRoom.getHighestUnlockedQuizIndex());
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
            System.out.println("Quiz " + quizIndex + " in Raum " + roomType
                    + " ist bereits abgeschlossen und kann nicht neu gestartet werden.");
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
        // z.B. "QUIZ_ANSWER_0" -> Index extrahieren
        int chosenIndex = Integer.parseInt(
                actionCommand.substring("QUIZ_ANSWER_".length())
        );

        boolean correct = (chosenIndex == currentQuestion.getCorrectIndex());

        if (correct) {
            System.out.println("Richtige Antwort!");
            pcs.firePropertyChange("correctAnswer", null, chosenIndex);

            // Sicherheitsnetz: falls irgendwas schief ist
            if (currentQuiz == null) {
                pcs.firePropertyChange("quizHidden", true, false);
                return;
            }

            // war das schon die letzte Frage?
            if (currentQuiz.isCompleted()) {
                // aktuelle Frage ist die letzte → Quiz beenden
                pcs.firePropertyChange("quizHidden", true, false);

                currentQuiz.markFinished(); // Quiz wird als beendet markiert -> kann nicht nochmal gespielt werden


                // Prüfen, ob das das letzte Quiz im Raum ist für Ourto
                Room room = findRoomByType(currentQuizRoomType);
                if (room != null) {
                    List<Quiz> quizzesInRoom = room.getQuizzes();
                    int quizIndex = quizzesInRoom.indexOf(currentQuiz);

                    // nächstes Quiz freischalten, je nachdem welches da höchste aktuell freigeschaltene ist
                    if (quizIndex == room.getHighestUnlockedQuizIndex()
                            && quizIndex < quizzesInRoom.size() - 1) {

                        room.unlockNextQuiz();
                        System.out.println("Quiz " + (quizIndex + 1) + " wurde freigeschaltet!");
                    }


                    boolean isLastQuizInRoom = (quizIndex == quizzesInRoom.size() - 1);

                    if (isLastQuizInRoom) {

                        // Raum als abgeschlossen markieren + ggf. Spielende prüfen
                        completeRoom(room);

                        // Outro-Text nur einmal anzeigen
                        if (!room.isOutroShown()
                                && room.getOutroText() != null
                                && !room.getOutroText().isBlank()) {

                            room.setOutroShown(true);

                            System.out.println("OUTRO for " + currentQuizRoomType + ": "
                                    + personalize(room.getOutroText()));


                            // pcs.firePropertyChange("storyText", null, room.getOutroText());
                        }
                    }
                }

                return;
            }

            // sonst zur nächsten Frage schalten
            currentQuiz.nextQuestion();
            Question next = currentQuiz.getCurrentQuestion();

            if (next != null) {
                currentQuestion = next;
                pcs.firePropertyChange("quizShown", null, currentQuestion);
            } else {
                pcs.firePropertyChange("quizHidden", true, false);
            }

        } else {
            // falsche Antwort → Quiz offen lassen, nur Feedback
            System.out.println("Falsche Antwort!");
            pcs.firePropertyChange("incorrectAnswer", null, chosenIndex);

        }
    }

    /**
     * Markiert einen Raum als abgeschlossen und löst ein Event aus.
     * Prüft anschließend, ob alle Räume beendet wurden.
     *
     * @param room der abgeschlossene Raum
     */
    void completeRoom(Room room) {
        room.setCompleted(true);
        pcs.firePropertyChange("roomCompleted", null, room);
        gameState.checkForGameCompletion();
    }
    public void setStartState() {
        changeScreen(getGameState().getAvailableScreens().get(0));
    }

    /**
     * Schaltet zum nächsten Screen aus der Liste availableScreens in gameState. Dazu wird currentScreen in der Liste gesucht und dann der nächste Screen ermittelt.
     */
    public void nextScreen() {
        int nextIdx = gameState.getAvailableScreens().indexOf(gameState.getCurrentScreen());
        nextIdx++;
        changeScreen(gameState.getAvailableScreens().get(nextIdx));
    }

    void showError(String errorMessage) {
        gameState.getCurrentScreen().setErrorMessage(errorMessage);
        changeScreen(gameState.getCurrentScreen());
    }

    /**
     * Validiert die Login-Eingaben (Benutzername + Schwierigkeitsgrad).
     * Speichert die Daten im GameState, legt einen neuen Spieler in der Datenbank an
     * und wechselt zum nächsten Screen. Zeigt bei leerem Namen einen Fehler an.
     *
     * @param username   eingegebener Benutzername
     * @param difficulty ausgewählte Schwierigkeitsstufe
     */
    public void validateLogin(String username, EnumDifficulty difficulty) {
        if (!username.isBlank()) {
            gameState.setUsername(username);
            gameState.setDifficulty(difficulty);
            gameState.getCurrentScreen().clearErrorMessage();

            // Load and register the SQLite JDBC driver
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to load SQLite JDBC driver", e);
            }

            // Inserting the username into the "spieler" table in the SQLite database
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
