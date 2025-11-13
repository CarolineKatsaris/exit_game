package controller;

import model.GameState;
import model.Model;
import view.EnumScreen;
import view.MainView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

public class Controller implements PropertyChangeListener {

    private final MainView view;
    private final Model model;

    /**
     * Konstruktor, registiert PropertyChangeListener und setzt den ersten Screen.
     * @param model
     * @param view
     */
    public Controller(Model model, MainView view) {
        this.model = model;
        this.view = view;

        // Der Controller hört aufs Model, PropertyChangeListener anlegen
        model.addPropertyChangeListener(this);

        //Model initialisieren - triggert PropChange(Intro Daten)
        model.setStartState();
        view.setVisible(true);
    }

    /**
     * Lädt Screen in MainView und legt die ActionListener an.
     * @param screen Screenname als String
     * ToDo kein String
     */
    void loadScreen(String screen) {
        view.showScreen(screen); //Screen anzeigen
        // ActionListener für  Buttons Elemente hinzufügen
        switch (screen) {
            case "start":
                view.getStartButton().addActionListener(e -> model.nextScreen());
                break;
            case "login":
                view.getSubmitButton().addActionListener(e -> model.nextScreen());
                break;
            default:
                break;
        }
    }
    /**
     * Event handler für Änderungen aus dem Modell
     * @param e A PropertyChangeEvent object describing the event source 
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        //nur für Events vom Typ "screen"
        if(e.getPropertyName().equals("screen")) {
            loadScreen(((GameState) e.getNewValue()).getCurrentScreen().getTitle());
        }
    }
}
