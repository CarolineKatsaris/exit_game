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

    void setBackground(String bgImagePath){
        this.bgImagePath = bgImagePath;
        ImageIcon bg = new ImageIcon(getClass().getResource(bgImagePath));
        background = new JLabel(bg);
        background.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());
        try {remove(background);} catch (Exception ignored) {} //remove old background if present
        add(background, valueOf(0)); // unterste Ebene
        setPreferredSize(background.getPreferredSize());
    }
}
