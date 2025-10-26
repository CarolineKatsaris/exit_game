import javax.swing.*;
import java.awt.EventQueue;

import model.GameState;
import view.PVMainView;
import controller.PVController;

public class PVPropertyChange {
    public static void main(String[] args) {

        // hübsches Look & Feel (optional)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        EventQueue.invokeLater(() -> {
            GameState gameState  = new GameState();
            PVMainView view = new PVMainView();
            PVController controller = new PVController(gameState, view);

            view.setVisible(true);
        });
    }
}
