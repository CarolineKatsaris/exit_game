import javax.swing.*;
import java.awt.EventQueue;

import model.GameState;
import view.MainView;
import controller.Controller;

public class ExitGame {
    public static void main(String[] args) {

        // hübsches Look & Feel (optional)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        EventQueue.invokeLater(() -> {
            GameState gameState  = new GameState();
            MainView view = new MainView();
            Controller controller = new Controller(gameState, view);
        });
    }
}
