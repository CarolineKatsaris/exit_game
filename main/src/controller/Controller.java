package controller;

import model.EnumScreen;
import model.Model;
import model.Room;
import model.Screen;
import view.MainView;
import view.RoomView;

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
     *
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
        // Overlay-Close (Weiter) an das Model melden
        if (screen.getTitle() == EnumScreen.Hub) {
            view.getHubView().setOverlayClosedListener(e -> model.overlayClosed());
        }
        if (screen instanceof Room) {
            view.getRoomView((Room) screen).setOverlayClosedListener(e -> model.overlayClosed());
        }

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
        if (screen instanceof Room) { //Sonderfall: Screen ist Raum
            registerRoomListeners((Room) screen);
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

        if (e.getPropertyName().equals("screen")) {
            Screen screen = (Screen) e.getNewValue();
            loadScreen(screen);
            return;
        }

        if (e.getPropertyName().equals("gameCompleted")) {
            int wrong = (int) e.getNewValue();
            view.getHubView().showFinalStats(wrong);
        }
    }



    // ToDo: Generischere Methode finden

    /**
     * Registiert die Actionlistener für die Schaltflächen im Raum wenn sie noch nicht vorhanden sind.
     * @param room
     */
    private void registerRoomListeners(Room room) {

        if (room.isListenersRegistered()) return; //wird im room Objekt gespeichert
        room.setListenersRegistered(true);

        RoomView roomView = view.getRoomView(room);

        // Registriert die Quiz-Buttons eines Raums so, dass beim Klick
        // das jeweils passende Quiz für genau diesen Raum gestartet wird.
        // @param roomType Typ des Raums, aus dem das Quiz gestartet wird

        EnumScreen roomType = room.getTitle();

        registerListener(roomView.getQuiz1Button(), e -> model.startQuizForRoom(roomType, 0));
        registerListener(roomView.getQuiz2Button(), e -> model.startQuizForRoom(roomType, 1));
        registerListener(roomView.getQuiz3Button(), e -> model.startQuizForRoom(roomType, 2));


        // -------------------
        // HOVER-EFFEKTE
        // -------------------
        roomView.getQuiz1Button().addMouseListener(
                new HoverAdapter(
                        () -> roomView.setQuiz1Highlight(true),
                        () -> roomView.setQuiz1Highlight(false)
                )
        );

        roomView.getQuiz2Button().addMouseListener(
                new HoverAdapter(
                        () -> roomView.setQuiz2Highlight(true),
                        () -> roomView.setQuiz2Highlight(false)
                )
        );

        roomView.getQuiz3Button().addMouseListener(
                new HoverAdapter(
                        () -> roomView.setQuiz3Highlight(true),
                        () -> roomView.setQuiz3Highlight(false)
                )
        );

        roomView.getBackButton().addActionListener(e ->
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

        // CLICK – RAM-Raum öffnen
        hub.getRamButton().addActionListener(e ->
                model.enterRoom(e.getActionCommand())
        );

        // CLICK – Files-Raum öffnen
        hub.getFileBtn().addActionListener(e ->
                model.enterRoom(e.getActionCommand())
        );

        // CLICK – Network-Raum öffnen
        hub.getNetworkBtn().addActionListener(e ->
                model.enterRoom(e.getActionCommand())
        );

        // CLICK – CPU-Raum öffnen
        hub.getCpuBtn().addActionListener(e ->
                model.enterRoom(e.getActionCommand())
        );


        //   HOVER – Rahmen ein/aus
        hub.getGraphicsCardButton().addMouseListener(
                new HoverAdapter(
                        () -> hub.setButtonHighlight(hub.getGraphicsCardButton(), true),
                        () -> hub.setButtonHighlight(hub.getGraphicsCardButton(), false)
                )
        );

        // HOVER – Rahmen ein/aus für RAM-Button
        hub.getRamButton().addMouseListener(
                new HoverAdapter(
                        () -> hub.setButtonHighlight(hub.getRamButton(), true),
                        () -> hub.setButtonHighlight(hub.getRamButton(), false)
                )
        );

        hub.getFileBtn().addMouseListener(
                new HoverAdapter(
                        () -> hub.setButtonHighlight(hub.getFileBtn(), true),
                        () -> hub.setButtonHighlight(hub.getFileBtn(), false)
                )
        );

        hub.getNetworkBtn().addMouseListener(
                new HoverAdapter(
                        () -> hub.setButtonHighlight(hub.getNetworkBtn(), true),
                        () -> hub.setButtonHighlight(hub.getNetworkBtn(), false)
                )
        );

        hub.getCpuBtn().addMouseListener(
                new HoverAdapter(
                        () -> hub.setButtonHighlight(hub.getCpuBtn(), true),
                        () -> hub.setButtonHighlight(hub.getCpuBtn(), false)
                )
        );

    }
}
