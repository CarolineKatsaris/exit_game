package view;

import javax.swing.*;

import java.awt.*;

import static java.lang.Integer.valueOf;

/**
 * Überklasse für Room und Hub
 */
class JLayeredView extends JLayeredPane {
    JLabel background;
    String bgImagePath;
    Rectangle[] buttonBounds;

    JLayeredView() {
        setLayout(null);
    }

    void showIntro(String text) {
        setButtonsEnabled(false);

        IntroOverlay overlay = new IntroOverlay(text, () -> setButtonsEnabled(true));
        overlay.showOn(this);
    }

    void setButtonsEnabled(boolean enabled) {};

    /**
     * Setzt den Hintergrund des Views. Ersetzt den alten Hintergrund, falls vorhanden.
     * @param bgImagePath
     */
    void setBackground(String bgImagePath){
        this.bgImagePath = bgImagePath;
        ImageIcon bg = new ImageIcon(getClass().getResource(bgImagePath));

        // Alten Hintergrund entfernen, falls vorhanden
        if (background != null) {
            remove(background);
        }

        // Neuen Hintergrund erstellen
        background = new JLabel(bg);
        background.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());
        add(background, Integer.valueOf(0)); // unterste Ebene

        setPreferredSize(background.getPreferredSize());
        revalidate();
        repaint();
    }

}