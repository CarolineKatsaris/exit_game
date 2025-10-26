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

    //benötigt für Anrede des Benutzers im Storytelling
    public String getUsername() {
        return username;
    }

    public String setUsername(){
        return username;
    }
    //Liste der einzelnen Räume, die schon offen sind bzw. geschlossen
    //private List<Room> roomOverview = new ArrayList<Room>(){
    //};

    // ist das Teil des CardLayouts? Wird das dafür verwendet?
    public String getScreen() {
        return screen;
    }
    // ist das Teil des CardLayouts? Wird das dafür verwendet?
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
