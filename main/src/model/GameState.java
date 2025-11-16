package model;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class GameState {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);// ToDo: Im Controller wird nur der pcs aus dem Model aufgerufen, der hier kann raus :)
    private final List<Room> roomOverview = new ArrayList<>(); //Liste der einzelnen Räume, die schon offen bzw. geschlossen sind
    private final List<Screen>availableScreens; //Liste aller möglichen Screens, die angesteuert werden können
    private Screen currentScreen;
    private String username;
    private EnumDifficulty difficulty;

    // Konstruktor - erzeugt die Räume, zunächst alle geschlossen
    public GameState() {
        Room graphicRoom = new Room(EnumScreen.GraphicRoom,false);
        Room ramRoom = new Room(EnumScreen.RAMRoom, false);
        Room fileRoom = new Room(EnumScreen.FileRoom, false);
        Room netRoom = new Room(EnumScreen.NetRoom, false);
        Room cpuRoom = new Room(EnumScreen.CPURoom, false);

        //Quizze für jeden Raum erstellen und auf eine Liste mit allen drei setzen
        Quiz graphicQuiz1 = new Quiz();
        Quiz graphicQuiz2 = new Quiz();
        Quiz graphicQuiz3 = new Quiz();
        graphicRoom.setQuizzes(List.of(graphicQuiz1,graphicQuiz2,graphicQuiz3));

        Quiz ramQuiz1 = new Quiz();
        Quiz ramQuiz2 = new Quiz();
        Quiz ramQuiz3 = new Quiz();
        ramRoom.setQuizzes(List.of(ramQuiz1,ramQuiz2,ramQuiz3));

        Quiz fileQuiz1 = new Quiz();
        Quiz fileQuiz2 = new Quiz();
        Quiz fileQuiz3 = new Quiz();
        fileRoom.setQuizzes(List.of(fileQuiz1,fileQuiz2,fileQuiz3));

        Quiz netQuiz1 = new Quiz();
        Quiz netQuiz2 = new Quiz();
        Quiz netQuiz3 = new Quiz();
        netRoom.setQuizzes(List.of(netQuiz1,netQuiz2,netQuiz3));

        Quiz cpuQuiz1 = new Quiz();
        Quiz cpuQuiz2 = new Quiz();
        Quiz cpuQuiz3 = new Quiz();
        cpuRoom.setQuizzes(List.of(cpuQuiz1,cpuQuiz2,cpuQuiz3));

        //Räume zur Übersicht hinzufügen
        roomOverview.add(graphicRoom);
        roomOverview.add(ramRoom);
        roomOverview.add(fileRoom);
        roomOverview.add(netRoom);
        roomOverview.add(cpuRoom);

        //Räume in die Screens Liste eintragen
        availableScreens = List.of(
            new Screen(EnumScreen.Start),
            new Screen(EnumScreen.Login),
            //new Screen("hub"),
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

    //Wird benötigt für Anrede des Benutzers im Storytelling
    public String getUsername() {
        return username;
    }

    public void setUsername(String username){ //übermittelt Namensänderungen ToDo: -> Muss ins Model
        String old = this.username;
        this.username = username;
        pcs.firePropertyChange("username", old, username);
    }

    // Verwendung für das Card Layout
    public Screen getCurrentScreen() {
        return currentScreen;
    }

    //Hier wird geprüft, ob der Screen, den ich ansteuern möchte, überhaupt existiert (aus der Liste, die oben angelegt wurde)
    void changeScreen(Screen newScreen){
        if (!availableScreens.contains(newScreen)){
            System.err.println("Unbekannter Screen: " + newScreen.getTitle());
            return;
        }

        this.currentScreen = newScreen;
    }

    public EnumDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(EnumDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    //prüft, ob ALLE Räume abgeschlossen sind und das Spiel somit zu Ende ist
    boolean allRoomsCompleted() {
        for (Room room : roomOverview) {
            if (!room.isCompleted()) {
                return false; // sobald ein Raum noch nicht abgeschlossen ist
            }
        }
            return true; // alle Räume abgeschlossen
        }

    //diese Methode leitet automatisch zum EndScreen über, wenn alle Räume abgeschlossen sind
    void checkForGameCompletion(){ //ToDo: -> Muss ins Model
        if (allRoomsCompleted()){
            changeScreen(getAvailableScreens().get(7));
            pcs.firePropertyChange("gameCompleted", false, true);
        }
    }
}

