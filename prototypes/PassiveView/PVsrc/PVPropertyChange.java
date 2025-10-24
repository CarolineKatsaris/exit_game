import javax.swing.*;
import java.awt.EventQueue;

import model.PVModel;
import view.PVMainView;
import controller.PVController;

public class PVPropertyChange {
    public static void main(String[] args) {

        // hübsches Look & Feel (optional)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        EventQueue.invokeLater(() -> {
            PVModel model = new PVModel();
            PVMainView view = new PVMainView();
            PVController controller = new PVController(model, view);

            view.setVisible(true);
        });
    }
}
