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

    public  Model() {
        this.gameState = new GameState();
    }

    public GameState getGameState() {
        return gameState;
    }

    //TODO: brauchen wir diese Methode noch?
    void setGameState(GameState newState) {
        GameState oldState = this.gameState;
        this.gameState = newState;
        pcs.firePropertyChange("gameState", oldState, newState);
    }

    List<Room> getRooms(){
        return gameState.getRoomOverview();
    }

    //ToDo das soll in GameState und in getScreenByTitle umbenannt werden
    public Room getRoomByRoomTitle(String roomTitle) {
//        for (Raum r : gameState.getRoomOverview()) {
//            if (r.getTitle().equalsIgnoreCase(roomTitle)){
//                return r;
//            }
//        }
        return null;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
     }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    // hier werden die Screens auch direkt gewechselt
    void changeScreen(Screen newScreen){
        gameState.changeScreen(newScreen);
        pcs.firePropertyChange("screen", null, newScreen);
    }

    /* Methode um Räume zu öffen, später evtl. überarbeiten (locked/unlocked)
     * Nutzt die Reihenfolge in availableScreens: Start -> Login -> Hub -> (erster Raum) -> ...
     */

    public void enterRoom() {
        var screens = gameState.getAvailableScreens();
        var current = gameState.getCurrentScreen();

        int currentIdx = screens.indexOf(current);
        if (currentIdx == -1) return; // Fallback: wenn currentScreen nicht in der Liste ist, nichts tun

        int nextIdx = currentIdx + 1;
        if (nextIdx < screens.size()) {
            changeScreen(screens.get(nextIdx)); // Wechselt zum nächsten Screen in der Liste
        }
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






    //mit dieser Methode können die Screens per Raum-Objekt gewechselt werden
    void openRoom(Room room){
        if (room ==null) return;
    //hier wird der Screen Name generiert (Setzt sich aus dem RaumNamen plus "room_" zusammen
        // String roomScreen = "room_" + room.getTitle().toLowerCase();

        if(!room.isOpen()){
            room.setOpen(true);
            pcs.firePropertyChange("roomOpen", null, room);
        }
        changeScreen(room);
    }

    // Methoden für Quiz

    // Methode, die den passenden Raum in GameState sucht
    public void startQuizForRoom(EnumScreen roomType) {
        currentQuizRoomType = roomType;
        Question qFromQuiz = null;

        Room foundRoom = null;
        for (Room r : gameState.getRoomOverview()) {
            if (r.getTitle() == roomType) {
                foundRoom = r;
                break;
            }
        }

        // aktuelles Quiz im Raum holen
        Quiz quiz = foundRoom.getCurrentQuiz();
        if (quiz == null) {
            return;
        }
        currentQuiz = quiz;

        // aktuelle Frage aus dem Quiz holen
        qFromQuiz = quiz.getCurrentQuestion();
        if (qFromQuiz == null) {
            return;
        }

        // Frage im Model merken und View informieren
        currentQuestion = qFromQuiz;
        pcs.firePropertyChange("quizShown", null, currentQuestion);
    }


//TODO: Methode brauchen wir nicht mehr? Löschen?
    /* // Quiz starten
    public void startQuizForCurrentRoom() {
        Question qFromQuiz = null;

        // Frage aus dem aktuellen Raum/Quiz holen
        if (gameState.getCurrentScreen() instanceof Room room) {
            Quiz quiz = room.getCurrentQuiz();
            if (quiz != null) {
                qFromQuiz = quiz.getCurrentQuestion();   // nimmt intern die aktuelle Frage
            }
        }


        currentQuestion = qFromQuiz;

        // View informieren: Quiz anzeigen und Frage anzeigen
        pcs.firePropertyChange("quizShown", null, currentQuestion);
    }
*/

     //iteriert durch die Räume, holt sich Titel und Typ des Raumes
    private Room findRoomByType(EnumScreen roomType) {
        for (Room r : gameState.getRoomOverview()) {
            if (r.getTitle() == roomType) {
                return r;
            }
        }
        return null;
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
    void completeRoom(Room room){
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
    public void validateLogin(String username, EnumDifficulty difficulty) {
        if (!username.isBlank()) {
            gameState.setUsername(username);
            gameState.setDifficulty(difficulty);

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
            // Stay on the Login screen if the username is invalid
        }
    }
}
