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
        roomOverview.add(new Room("Grafikkarte", false));
        roomOverview.add(new Room("RAM", false));
        roomOverview.add(new Room("Dateisystem", false));
        roomOverview.add(new Room("Netzwerk", false));
        roomOverview.add(new Room("CPU", false));
    }

    public List<Room> getRoomOverview() {
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
}
