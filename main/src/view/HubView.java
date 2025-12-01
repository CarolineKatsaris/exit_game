package view;

import javax.swing.*;

import java.awt.*;

public class HubView extends JLayeredPane {

    private JLabel background;

    private JButton graphicsCardBtn;

    public HubView() {

        setLayout(null);

        ImageIcon img = new ImageIcon(getClass().getResource("/HubViewBackground.png"));
        background = new JLabel(img);
        background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
        add(background, Integer.valueOf(0));  // unterste Ebene

        // Invisible Button über die Grafik "Grafikkarte"

        graphicsCardBtn = makeInvisibleButton(80, 200, 250, 300,"graphics_card");

        add(graphicsCardBtn, Integer.valueOf(1)); // eine Ebene drüber

    }

    private JButton makeInvisibleButton(int x, int y, int w, int h, String cmd) {

        JButton b = new JButton();
        b.setBounds(x, y, w, h);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setActionCommand(cmd);
        return b;

    }

    // Hover-Highlight (Rand)

    public void setGraphicsCardHighlight(boolean on) {

        if (on) {
            graphicsCardBtn.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
            graphicsCardBtn.setBorderPainted(true);
            graphicsCardBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {

            graphicsCardBtn.setBorder(BorderFactory.createEmptyBorder());
            graphicsCardBtn.setBorderPainted(false);
            graphicsCardBtn.setCursor(Cursor.getDefaultCursor());
        }
        repaint();
    }

    public JButton getGraphicsCardButton() {
        return graphicsCardBtn;
    }

}