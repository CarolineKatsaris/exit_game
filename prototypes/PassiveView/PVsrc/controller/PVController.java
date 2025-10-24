package controller;

import model.PVModel;
import view.EnumScreen;
import view.PVMainView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PVController implements PropertyChangeListener {

    private final PVModel model;
    private final PVMainView view;

    public PVController(PVModel model, PVMainView view) {
        this.model = model;
        this.view = view;

        // Der Controller hört aufs Model:
        model.addPropertyChangeListener(this);

        // Der Controller reagiert auf die View:
        view.getStartButton().addActionListener(e -> {
            // Spieler klickt "Starte Spiel" -> wir wechseln in den Hub
            model.setScreen("hub");
        });

        // Initialen Screen anzeigen
        view.showScreen(EnumScreen.START.getCardName());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("screen".equals(evt.getPropertyName())) {
            String newScreen = (String) evt.getNewValue();
            if ("start".equals(newScreen)) {
                view.showScreen(EnumScreen.START.getCardName());
            } else if ("hub".equals(newScreen)) {
                view.showScreen(EnumScreen.HUB.getCardName());
            }
        }
    }
}
