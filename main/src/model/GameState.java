package model;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;


public class GameState {


    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private final List<Room> roomOverview = new ArrayList<>();
    private final List<Screen> availableScreens;
    private final DbLoader dbLoader;
    private Screen currentScreen;
    private String username;
    private EnumDifficulty difficulty;
    private int totalWrongAnswers = 0; // Gesamtzahl der falschen Antworten


    // Konstruktor - erzeugt die Räume, zunächst alle geschlossen
    public GameState() {

        this.dbLoader = new DbLoader("jdbc:sqlite:ExitGame.sqlite");

        Screen hubScreen = new Screen(EnumScreen.Hub);
        Room graphicRoom = new Room(EnumScreen.GraphicRoom, false);
        Room ramRoom     = new Room(EnumScreen.RAMRoom,     false);
        Room fileRoom    = new Room(EnumScreen.FileRoom,    false);
        Room netRoom     = new Room(EnumScreen.NetRoom,     false);
        Room cpuRoom     = new Room(EnumScreen.CPURoom,     false);


        // Räume zur Übersicht hinzufügen
        roomOverview.add(graphicRoom);
        roomOverview.add(ramRoom);
        roomOverview.add(fileRoom);
        roomOverview.add(netRoom);
        roomOverview.add(cpuRoom);

        // Räume in die Screens-Liste eintragen, legt Reihenfolge fest -> Hubview immer wieder zwischenschalten?


        availableScreens = List.of(
                new Screen(EnumScreen.Start),
                new Screen(EnumScreen.Login),
                hubScreen,
                //new Screen(EnumScreen.Room)?,
                graphicRoom,
                hubScreen,
                ramRoom,
                hubScreen,
                fileRoom,
                hubScreen,
                netRoom,
                hubScreen,
                cpuRoom,
                new Screen(EnumScreen.End)
        );

        for (Screen screeni : availableScreens) {
            dbLoader.loadBackgroundForScreen(screeni);
            screeni.setIntroText(dbLoader.loadIntroText(screeni.getTitle()));
            screeni.setOutroText(dbLoader.loadOutroText(screeni.getTitle()));
        }

    }
    // >>> Quizze aus der Datenbank laden <<<

    public void initQuizzesForDifficulty(EnumDifficulty difficulty) {
        // Räume haben wir ja in roomOverview
        for (Room room : roomOverview) {
            EnumScreen roomTitle = room.getTitle();
            switch (roomTitle) {
                case GraphicRoom, RAMRoom, FileRoom, NetRoom, CPURoom -> {
                    var quizzes = dbLoader.loadQuizzesForRoom(roomTitle, difficulty);
                    room.setQuizzes(quizzes);
                }
                default -> {

                }
            }
        }
    }
    public void incrementWrongAnswers() {
        totalWrongAnswers++;
    }

    public int getTotalWrongAnswers() {
        return totalWrongAnswers;
    }

    public void resetWrongAnswers() {
        totalWrongAnswers = 0;
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


    public Screen getScreenByTitle(EnumScreen title) {
        for (Screen screen : availableScreens) {
            if (screen.getTitle() == title) {
                return screen;
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


//ToDo: Ist 7 der Endscreen?
    void checkForGameCompletion(){
        if (allRoomsCompleted()){
            changeScreen(getAvailableScreens().get(7));
            pcs.firePropertyChange("gameCompleted", false, true);
            //Gesamtzahl falscher Antworten in der Konsole ausgeben
            System.out.println("Gesamtzahl falscher Antworten: " + totalWrongAnswers);
        }
    }
}
