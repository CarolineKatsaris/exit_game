package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class PVModel {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    // das ist euer "Screen"-Zustand aus dem Klassendiagramm
    private String screen = "start";

    public String getScreen() {
        return screen;
    }

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
