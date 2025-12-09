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

    // Konstruktor - erzeugt die Räume, zunächst alle geschlossen
    public GameState() {
        Screen hubScreen = new Screen(EnumScreen.Hub);Room graphicRoom = new Room(EnumScreen.GraphicRoom, false);

        Room ramRoom     = new Room(EnumScreen.RAMRoom,     false);
        Room fileRoom    = new Room(EnumScreen.FileRoom,    false);
        Room netRoom     = new Room(EnumScreen.NetRoom,     false);
        Room cpuRoom     = new Room(EnumScreen.CPURoom,     false);

        this.dbLoader = new DbLoader("jdbc:sqlite:ExitGame.sqlite");

        // >>> Quizze aus der Datenbank laden <<<
        //ToDo generischer in SCchleife
        graphicRoom.setQuizzes(dbLoader.loadQuizzesForRoom(EnumScreen.GraphicRoom));
        ramRoom.setQuizzes(dbLoader.loadQuizzesForRoom(EnumScreen.RAMRoom));
        fileRoom.setQuizzes(dbLoader.loadQuizzesForRoom(EnumScreen.FileRoom));
        netRoom.setQuizzes(dbLoader.loadQuizzesForRoom(EnumScreen.NetRoom));
        cpuRoom.setQuizzes(dbLoader.loadQuizzesForRoom(EnumScreen.CPURoom));


        // Into- / Outro-Texte
        hubScreen.setIntroText(dbLoader.loadIntroText(EnumScreen.Hub));
        hubScreen.setOutroText(dbLoader.loadOutroText(EnumScreen.Hub));

        graphicRoom.setIntroText(dbLoader.loadIntroText(EnumScreen.GraphicRoom));
        graphicRoom.setOutroText(dbLoader.loadOutroText(EnumScreen.GraphicRoom));

        ramRoom.setIntroText(dbLoader.loadIntroText(EnumScreen.RAMRoom));
        ramRoom.setOutroText(dbLoader.loadOutroText(EnumScreen.RAMRoom));

        fileRoom.setIntroText(dbLoader.loadIntroText(EnumScreen.FileRoom));
        fileRoom.setOutroText(dbLoader.loadOutroText(EnumScreen.FileRoom));

        netRoom.setIntroText(dbLoader.loadIntroText(EnumScreen.NetRoom));
        netRoom.setOutroText(dbLoader.loadOutroText(EnumScreen.NetRoom));

        cpuRoom.setIntroText(dbLoader.loadIntroText(EnumScreen.CPURoom));
        cpuRoom.setOutroText(dbLoader.loadOutroText(EnumScreen.CPURoom));


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
                new Screen(EnumScreen.Hub),
                ramRoom,
                new Screen(EnumScreen.Hub),
                fileRoom,
                new Screen(EnumScreen.Hub),
                netRoom,
                new Screen(EnumScreen.Hub),
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

    void checkForGameCompletion(){
        if (allRoomsCompleted()){
            changeScreen(getAvailableScreens().get(7));
            pcs.firePropertyChange("gameCompleted", false, true);
        }
    }
}
