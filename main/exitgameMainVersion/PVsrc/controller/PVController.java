package controller;

import model.GameState;
import view.EnumScreen;
import view.PVMainView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PVController implements PropertyChangeListener {

    private final GameState gameState;
    private final PVMainView view;

    public PVController(GameState gameState, PVMainView view) {
        this.gameState = gameState;
        this.view = view;

        // Der Controller hört aufs Model:
        gameState.addPropertyChangeListener(this);

        // Der Controller reagiert auf die View:
        view.getStartButton().addActionListener(e -> {
            // Spieler klickt "Starte Spiel" -> wir wechseln in den Hub
            gameState.setScreen("hub");
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
            }  else if ("hub".equals(newScreen)) {
                view.showScreen(EnumScreen.HUB.getCardName());
            }  //else if("login".equals(newScreen)){
                   // view.showScreen(EnumScreen.LOGIN.getCardName());
             //   }


        }
    }
}
