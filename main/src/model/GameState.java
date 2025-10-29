package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class GameState {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    // das ist euer "Screen"-Zustand aus dem Klassendiagramm - wofür wird das benötigt?
    private String screen = "start";
    private String username;
    //Liste der einzelnen Räume, die schon offen bzw. geschlossen sind
    private final List<Room> roomOverview = new ArrayList<>();

    // Konstruktor - erzeugt die Räume, zunächst alle geschlossen
    public GameState() {
        Room graphicRoom = new Room("Grafikkarte",false);
        Room ramRoom = new Room("RAM", false);
        Room fileRoom = new Room("Dateisystem", false);
        Room netRoom = new Room("Netzwerk", false);
        Room cpuRoom = new Room("CPU", false);

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
    }

    List<Room> getRoomOverview() {
        return new ArrayList<>(roomOverview);
    }

    //benötigt für Anrede des Benutzers im Storytelling
    public String getUsername() {
        return username;
    }

    public void setUsername(String username){
        String old = this.username;
        this.username = username;
        pcs.firePropertyChange("username", old, username); //übermittelt Namensänderungen
    }

    // Verwendung für das Cardlayout
    public String getScreen() {
        return screen;
    }
    // ermöglicht die Überprüfung des Screens
    public void setScreen(String newScreen) {
        String old = this.screen;
        this.screen = newScreen;
        // feuert Event "screen hat sich geändert"
        pcs.firePropertyChange("screen", old, newScreen);
    }
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
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
    }

