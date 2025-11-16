package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

// observable
public class Model {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private GameState gameState;
    private Question currentQuestion;


    public  Model() {
        this.gameState = new GameState();
    }

    public GameState getGameState() {
        return gameState;
    }

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
        pcs.firePropertyChange("screen", null, gameState);
    }

    //mit dieser Methode können die Screens per Raum-Objekt gewechselt werden
    void openRoom(Room room){
        if (room ==null) return;
    //hier wird der Screen Name generiert (Setzt sich aus dem RaumNamen plus "room_" zusammen
        //String roomScreen = "room_" + room.getTitle().toLowerCase();

        if(!room.isOpen()){
            room.setOpen(true);
            pcs.firePropertyChange("roomOpen", null, room);
        }
        changeScreen(room);
    }

    // Methoden für Quiz

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    // Quiz starten
    public void startQuizForCurrentRoom() {
        // TODO: hier später je nach Room die passende Frage holen
        currentQuestion = new Question(
                "Was macht die GPU?",
                List.of("Grafik berechnen", "Daten speichern", "CPU kühlen", "Strom liefern"),
                0 // richtige Antwort
        );

        // View informieren: Quiz anzeigen und Frage anzeigen
        pcs.firePropertyChange("quizShown", null, currentQuestion);
    }

    // Auf Antwortbutton reagieren
    public void handleQuizAnswer(String actionCommand) {
        // z.B. "QUIZ_ANSWER_0" -> Index extrahieren
        int chosenIndex = Integer.parseInt(
                actionCommand.substring("QUIZ_ANSWER_".length())
        );

        boolean correct = (chosenIndex == currentQuestion.getCorrectIndex());

        // TODO: Punkte / Leben / Feedback / Raumwechsel usw.
        if (correct) {
            System.out.println("Richtige Antwort!");
        } else {
            System.out.println("Falsche Antwort!");
        }

        // Quiz-Overlay ausblenden
        pcs.firePropertyChange("quizHidden", true, false);
    }

    // Diese Methode markiert den Raum als abgeschlossen und prüft, ob ALLE Räume fertig sind
    void completeRoom(Room room){
        room.setCompleted(true);
        pcs.firePropertyChange("roomCompleted", null, room);
        gameState.checkForGameCompletion();
    }

    //mit dieser Methode kann einfach zur HubAnsicht gewechselt werden
    void returnToHub(){
        //ToDo Fix
        //changeScreen("hub");
    }

    public void setStartState() {
        changeScreen(getGameState().getAvailableScreens().get(0));
    }

    public void nextScreen() {
        int nextIdx = gameState.getAvailableScreens().indexOf(gameState.getCurrentScreen());
        nextIdx++;
        changeScreen(gameState.getAvailableScreens().get(nextIdx));
    }

    public void validateLogin(String username, EnumDifficulty difficulty){
        if (!username.isBlank()){
            gameState.setUsername(username);
            gameState.setDifficulty(difficulty);
            nextScreen();
        } else {
            //auf Login Screen bleiben
        }
    }
}
