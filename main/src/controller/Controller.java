package controller;

import model.*;
import view.MainView;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Controller implements PropertyChangeListener {

    private final MainView view;
    private final Model model;
    private boolean hubListenersRegistered;
    private boolean roomListenersRegistered;

    /**
     * Constructs a Controller instance that connects the given Model and MainView.
     * The Controller observes the Model for property changes and initializes the
     * application's starting state. It also makes the MainView visible.
     *
     * @param model The Model instance to be controlled and observed.
     * @param view  The MainView instance to be interacted with and controlled.
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
     * Generische Methode, um Listener nur einmal zu registrieren.
     * @param button
     * @param action
     */
    private void registerListener(JButton button, java.awt.event.ActionListener action) {
        if (button.getActionListeners().length == 0) {
            button.addActionListener(action);
        }
    }

    /**
     * Lädt Screen in MainView und legt die ActionListener an.
     *
     * @param screen Screen als EnumScreen
     *               Später: view.getButtonsFor(screen).forEach(btn ->
     *               btn.addActionListener(e -> model.handleEvent(btn.getActionCommand()))
     *               );
     */
    void loadScreen(Screen screen) {
        view.showScreen(screen); //Screen anzeigen
        switch (screen.getTitle()) { // ActionListener für Buttons Elemente hinzufügen
            case Start: //ToDo -> {} * eventuell Pfeil + Klammernschreibweise statt break
                registerListener(view.getStartButton(), e -> model.nextScreen());
                break;
            case Login:
                registerListener(view.getSubmitButton(),
                        e -> model.validateLogin(view.getLoginUsername(), view.getLoginDifficulty()));
                break;
            case Hub:
                registerHubListeners();
                break;
            default:
                break;
        }
        if(screen instanceof Room) { //Sonderfall: Screen ist Raum
            registerRoomListeners();
        }

    }

    /**
     * Event handler für Änderungen aus dem Modell
     *
     * @param e A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        //nur für Events vom Typ "screen"
        if(e.getPropertyName().equals("screen")) {
           Screen screen = (Screen) e.getNewValue();
           loadScreen(screen);
        }
    }

    // ToDo: Generischere Methode finden
    private void registerRoomListeners() {

        if (roomListenersRegistered) return;
        roomListenersRegistered = true;

        var room = view.getRoomView();

        // -------------------
        // QUIZ-BUTTON CLICKS ->  model.nextScreen() als Platzhalter
        // -------------------
        room.getQuiz1Button().addActionListener(e -> model.startQuizForRoom(EnumScreen.GraphicRoom)); // Dieser Button ist aktiviert
        room.getQuiz2Button().addActionListener(e -> model.nextScreen());
        room.getQuiz3Button().addActionListener(e -> model.nextScreen());

        // -------------------
        // HOVER-EFFEKTE
        // -------------------
        room.getQuiz1Button().addMouseListener(
                new HoverAdapter(
                        () -> room.setQuiz1Highlight(true),
                        () -> room.setQuiz1Highlight(false)
                )
        );

        room.getQuiz2Button().addMouseListener(
                new HoverAdapter(
                        () -> room.setQuiz2Highlight(true),
                        () -> room.setQuiz2Highlight(false)
                )
        );

        room.getQuiz3Button().addMouseListener(
                new HoverAdapter(
                        () -> room.setQuiz3Highlight(true),
                        () -> room.setQuiz3Highlight(false)
                )
        );

        room.getBackButton().addActionListener(e ->
            model.returnToHub());

    }


    // Hier müssen später die HubButtons Listener eingefügt werden
    private void registerHubListeners() {

        // Listener dürfen nur EINMAL registriert werden
        if (hubListenersRegistered) return;
        hubListenersRegistered = true;

        var hub = view.getHubView();
        //   CLICK – Raum öffnen
        hub.getGraphicsCardButton().addActionListener(e -> {
            model.enterRoom(e.getActionCommand()); //ActionCommand == Screen.Title
        });

        //   HOVER – Rahmen ein/aus
        hub.getGraphicsCardButton().addMouseListener(
                new HoverAdapter(
                        () ->hub.setGraphicsCardHighlight(true),
                        () ->hub.setGraphicsCardHighlight(false)
                )
        );
    }
}
