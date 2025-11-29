package controller;

import model.*;
import view.MainView;

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
     * Lädt Screen in MainView und legt die ActionListener an.
     *
     * @param screen Screen als EnumScreen
     *               Später: view.getButtonsFor(screen).forEach(btn ->
     *               btn.addActionListener(e -> model.handleEvent(btn.getActionCommand()))
     *               );
     */
    void loadScreen(EnumScreen screen) {
        view.showScreen(screen); //Screen anzeigen
        // ActionListener für Buttons Elemente hinzufügen
        switch (screen) {
            case Start: //-> {} * eventuell Pfeil + Klammernschreibweise statt break
                view.getStartButton().addActionListener(e -> model.nextScreen());
                break;
            case Login:
                view.getSubmitButton().addActionListener(e -> model.validateLogin(view.getLoginUsername(), view.getLoginDifficulty()));
                break;
            //-> {} * eventuell Pfeil + Klammernschreibweise statt break
            case Room:
                registerRoomListeners();
                break;
            case Hub:
                registerHubListeners();
                break;

            default:
                break;
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
        //if(e.getPropertyName().equals("screen")) {
            if ("screen".equals(e.getPropertyName())){
                Screen screen = (Screen) e.getNewValue();

                //ToDo Workaround, da View nur Room anzeigen kann, aber noch keinen Mechanismus hat, Rooms generisch anzuzeigen
                //ToDo loadScreen muss das Screen Objekt an view weitergeben und nicht nur den Titel.
                if(screen instanceof Room) {
                    loadScreen(EnumScreen.Room);
                }
                else {
                    loadScreen(screen.getTitle());
                }

            //loadScreen(((GameState) e.getNewValue()).getCurrentScreen().getTitle());
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
            model.enterRoom();

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
