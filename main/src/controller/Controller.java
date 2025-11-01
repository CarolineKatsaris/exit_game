package controller;

import model.GameState;
import view.EnumScreen;
import view.MainView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

public class Controller implements PropertyChangeListener {

    private final GameState gameState;
    private final MainView view;

    /**
     * Konstruktor, registiert PropertyChangeListener und setzt den ersten Screen.
     * @param gameState
     * @param view
     */
    public Controller(GameState gameState, MainView view) {
        this.gameState = gameState;
        this.view = view;

        // Der Controller hört aufs Model, PropertyChangeListener anlegen
        gameState.addPropertyChangeListener(this);

        //Model initialisieren - triggert PropChange(Intro Daten)
        //ToDo bessere Methode zum initialisieren des Models #51
        gameState.setScreen(EnumScreen.values()[0].toString());

        view.setVisible(true);
    }

    /**
     * Lädt Screen in MainView und legt die ActionListener an.
     * @param screen Screenname als String
     * ToDo kein String
     */
    void loadScreen(String screen) {
        //Screen anzeigen
        view.showScreen(screen);

        //ActionListener anlegen, der Controller reagiert auf die View:
        //ToDo generisch, view.getScreen().getClickables() -> Schleife addActionListener
        view.getStartButton().addActionListener(e -> {
            // Spieler klickt "Starte Spiel" -> wir wechseln in den Hub
            gameState.setScreen("login");
        });
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
            loadScreen((String) e.getNewValue());
        }
    }
}
