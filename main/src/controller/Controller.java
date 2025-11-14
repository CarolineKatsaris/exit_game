package controller;

import model.GameState;
import model.Model;
import model.EnumScreen;
import view.MainView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Controller implements PropertyChangeListener {

    private final MainView view;
    private final Model model;

    /**
     * Konstruktor, registriert PropertyChangeListener und setzt den ersten Screen.
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
     * @param screen Screen als EnumScreen
     *
     */
    void loadScreen(EnumScreen screen) {
        view.showScreen(screen); //Screen anzeigen
        // ActionListener für  Buttons Elemente hinzufügen
        switch (screen) {
            case START:
                view.getStartButton().addActionListener(e -> model.nextScreen());
                break;
            case LOGIN:
                view.getSubmitButton().addActionListener(e -> model.validateLogin(view.getLoginUsername(), view.getLoginDifficulty()));
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
        if("screen".equals(e.getPropertyName())) {
            GameState state = (GameState) e.getNewValue();
            String title = state.getCurrentScreen().getTitle();  // z.B. "start", "Grafikkarte", ...

            EnumScreen screen;
            try {
                // versucht, z.B. "start" → "START" → EnumScreen.START
                screen = EnumScreen.valueOf(title.toUpperCase());
            } catch (IllegalArgumentException ex) {
                // Wenn es kein Enum-Wert ist (z.B. "Grafikkarte"), dann ist es irgendein Raum:
                screen = EnumScreen.ROOM;
            }

            loadScreen(screen);
        }
    }
}
