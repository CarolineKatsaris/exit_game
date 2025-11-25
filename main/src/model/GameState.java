package model;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;


public class GameState {


    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private final List<Room> roomOverview = new ArrayList<>();
    private final List<Screen> availableScreens;
    private final QuizLoader quizLoader;
    private Screen currentScreen;
    private String username;
    private EnumDifficulty difficulty;

    // Konstruktor - erzeugt die Räume, zunächst alle geschlossen
    public GameState() {
        Room graphicRoom = new Room(EnumScreen.GraphicRoom, false);
        Room ramRoom     = new Room(EnumScreen.RAMRoom,     false);
        Room fileRoom    = new Room(EnumScreen.FileRoom,    false);
        Room netRoom     = new Room(EnumScreen.NetRoom,     false);
        Room cpuRoom     = new Room(EnumScreen.CPURoom,     false);

        this.quizLoader = new QuizLoader ("jdbc:sqlite:ExitGame.sqlite");

        // >>> Quizze aus der Datenbank laden <<<
        graphicRoom.setQuizzes(quizLoader.loadQuizzesForRoom(EnumScreen.GraphicRoom));
        ramRoom.setQuizzes(quizLoader.loadQuizzesForRoom(EnumScreen.RAMRoom));
        fileRoom.setQuizzes(quizLoader.loadQuizzesForRoom(EnumScreen.FileRoom));
        netRoom.setQuizzes(quizLoader.loadQuizzesForRoom(EnumScreen.NetRoom));
        cpuRoom.setQuizzes(quizLoader.loadQuizzesForRoom(EnumScreen.CPURoom));

        // Räume zur Übersicht hinzufügen
        roomOverview.add(graphicRoom);
        roomOverview.add(ramRoom);
        roomOverview.add(fileRoom);
        roomOverview.add(netRoom);
        roomOverview.add(cpuRoom);

        // Räume in die Screens-Liste eintragen
        availableScreens = List.of(
                new Screen(EnumScreen.Start),
                new Screen(EnumScreen.Login),
                new Screen(EnumScreen.Room),
                graphicRoom,
                ramRoom,
                fileRoom,
                netRoom,
                cpuRoom,
                new Screen(EnumScreen.End)
        );
    }







    public List<Screen> getAvailableScreens() {
        return availableScreens;
    }

    List<Room> getRoomOverview() {
        return new ArrayList<>(roomOverview);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username){
        String old = this.username;
        this.username = username;
        pcs.firePropertyChange("username", old, username);
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    void changeScreen(Screen newScreen){
        Screen old = this.currentScreen;
        this.currentScreen = newScreen;
        //wird schon im MOdel gefeuert
        //pcs.firePropertyChange("screen",old,newScreen);
        }

    Screen findScreenByName(EnumScreen title){
        for (Screen s : availableScreens){
            if (s.getTitle().equals(title)){
                return s;
            }
        }
        return null;
    }
    public EnumDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(EnumDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    boolean allRoomsCompleted() {
        for (Room room : roomOverview) {
            if (!room.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    void checkForGameCompletion(){
        if (allRoomsCompleted()){
            changeScreen(getAvailableScreens().get(7));
            pcs.firePropertyChange("gameCompleted", false, true);
        }
    }
}
