import javax.swing.*;

public class ClickableAreas {
    public static void main(String[] args) { // ich kommentiere zu Testzwecken
        SwingUtilities.invokeLater(() -> {
            ClickableAreasUI gameUI = new ClickableAreasUI();
            gameUI.setVisible(true);
        });
    }
}
