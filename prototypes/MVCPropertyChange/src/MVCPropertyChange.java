import javax.swing.*;
import java.awt.*;

/**
 * Einstiegspunkt der Anwendung. Initialisiert den AllgController
 * und setzt Look & Feel sowie Fenster-Parameter.
 */
public class MVCPropertyChange {
    public static void main(String[] args) {
        // Optional: System-Look & Feel aktivieren für native Optik
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        EventQueue.invokeLater(() -> {
            Controller controller = new Controller();
            // brauch ich das extra oder Teil von Konstruktor?
            // controller.show();
        });
    }
}
