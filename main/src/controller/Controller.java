package controller;

import model.EnumScreen;
import model.Model;
import model.Room;
import model.Screen;
import view.MainView;
import view.RoomView;
import java.awt.Rectangle;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Zentraler Controller (MVC), der Model und View verbindet.
 * <p>
 * Aufgaben:
 * <ul>
 *   <li>Reagiert auf Model-Events via {@link PropertyChangeListener}</li>
 *   <li>Lädt den aktuellen {@link Screen} in die {@link MainView}</li>
 *   <li>Registriert Action-/Mouse-Listener (einmalig) für Hub und Räume</li>
 *   <li>Steuert view-spezifische Effekte beim Screenwechsel (z.B. Fog im CPU-Raum)</li>
 * </ul>
 */

public class Controller implements PropertyChangeListener {

    private final MainView view;
    private final Model model;

    private boolean hubListenersRegistered;


    /**
     * Erstellt den Controller, registriert ihn als Listener am Model,
     * setzt den Startzustand und zeigt die View an.
     *
     * @param model Model-Instanz (Observable)
     * @param view  MainView-Instanz (UI)
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

        if (screen instanceof Room room) {
            RoomView rv = view.getRoomView(room);

            boolean isCpuRoom = room.getTitle() == EnumScreen.CPURoom;

            // Outro erkennen: aktuelles Background-Bild ist das Outro-Bild
            boolean isOutroBackground =
                    room.getBackgroundImagePath() != null
                            && room.getOutroImagePath() != null
                            && room.getBackgroundImagePath().equals(room.getOutroImagePath());

            rv.enableFog(isCpuRoom && !isOutroBackground);
        }


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
     * Eventhandler für Änderungen aus dem Model
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

        if (room.getTitle() == EnumScreen.RAMRoom) {
                roomView.setupRamManHotspot(new Rectangle(200, 460, 100, 100));   // Männchen 1 (grün/rot)
                roomView.setupRamManHotspot2(new Rectangle(1220, 460, 100, 100));  // Männchen 2 (blau/gelb)

        }


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
