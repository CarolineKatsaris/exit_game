package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

// observable
public class Model {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private GameState gameState;

    public  Model() {
        this.gameState = new GameState();
    }

    public GameState getGameState() {
        return gameState;
    }

    //Hier erfolgt durch die beiden Methoden der Zugriff auf die einzelnen Räume
    public List<Room> getRooms(){
        return gameState.getRoomOverview();
    }

    //ToDo das soll in GameState und in getScreenByTitle umbenannt werden
    public Room getRoomByRoomTitle(String roomTitle) {
        for (Room r : gameState.getRoomOverview()) {
            if (r.getTitle().equalsIgnoreCase(roomTitle)){
                return r;
        }
    }
        return null;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
     }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    //hier wird immer nach dem aktuellen Gamestate abgefragt
    public void setGameState(GameState newState) {
        GameState oldState = this.gameState;
        this.gameState = newState;
        pcs.firePropertyChange("gameState", oldState, newState);
    }

    // hier werden die Screens auch direkt gewechselt
    public void changeScreen(Screen newScreen){
        gameState.changeScreen(newScreen);
        pcs.firePropertyChange("screen", null, gameState);
    }

    //mit dieser Methode können die Screens per Room-Objekt gewechselt werden
    public void openRoom(Room room){
        if (room ==null) return;
    //hier wird der Screen Name generiert (Setzt sich aus dem RaumNamen plus "room_" zusammen
        String roomScreen = "room_" + room.getTitle().toLowerCase();

        if(!room.isOpen()){
            room.setOpen(true);
            pcs.firePropertyChange("roomOpen", null, room);
        }
        changeScreen(room);
    }

    // Diese Methode markiert den Raum als abgeschlossen und prüft, ob ALLE Räume fertig sind
    public void completeRoom(Room room){
        room.setCompleted(true);
        pcs.firePropertyChange("roomCompleted", null, room);
        gameState.checkForGameCompletion();
    }
    //mit dieser Methode kann einfach zur HubAnsicht gewechselt werden
    public void returnToHub(){
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
}
