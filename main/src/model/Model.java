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

    // hier werden die Screens auch direkt gewechselt
    void changeScreen(Screen newScreen) {
        gameState.changeScreen(newScreen);
        pcs.firePropertyChange("screen", null, newScreen);
    }

    /**
     * Methode um Räume zu öffen, später evtl. überarbeiten (locked/unlocked)
     * Nutzt die Reihenfolge in availableScreens: Start -> Login -> Hub -> (erster Raum) -> ...
     */
    public void enterRoom(String roomTitle) {
        nextScreen(); //ToDo Raum basierend auf Titel mit changeScreen öffnen, falls es erlaubt ist
    }

    //mit dieser Methode kann einfach zur HubAnsicht gewechselt werden, geht einfach bei available Screens wieder eins zurück
    public void returnToHub() {
        var screens = gameState.getAvailableScreens();
        var current = gameState.getCurrentScreen();

        int idx = screens.indexOf(current);
        if (idx <= 0) return;          // ganz vorne -> nichts tun

        Screen previous = screens.get(idx - 1);  // in der availableScreenList steht immer der zugehörige Hub davor
        changeScreen(previous);
    }


    // Methoden für Quiz

    // Methode, die den passenden Raum in GameState sucht
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

        System.out.println("Quizzes im Raum " + roomType + ": " + foundRoom.getQuizzes().size());

        // Quiz-Index setzen
        foundRoom.setCurrentQuizIndex(quizIndex);

        // aktuelles Quiz holen
        Quiz quiz = foundRoom.getCurrentQuiz();
        if (quiz == null) {
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



    // Auf Antwortbutton reagieren und Weiterschalten der Fragen
    public void handleQuizAnswer(String actionCommand) {
        // z.B. "QUIZ_ANSWER_0" -> Index extrahieren
        int chosenIndex = Integer.parseInt(
                actionCommand.substring("QUIZ_ANSWER_".length())
        );

        boolean correct = (chosenIndex == currentQuestion.getCorrectIndex());

        if (correct) {
            System.out.println("Richtige Antwort!");

            // Sicherheitsnetz: falls irgendwas schief ist
            if (currentQuiz == null) {
                pcs.firePropertyChange("quizHidden", true, false);
                return;
            }

            // *** WICHTIG: war das schon die letzte Frage? ***
            if (currentQuiz.isCompleted()) {
                // aktuelle Frage ist die letzte → Quiz beenden
                pcs.firePropertyChange("quizHidden", true, false);
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

        }
    }


    // Diese Methode markiert den Raum als abgeschlossen und prüft, ob ALLE Räume fertig sind
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
