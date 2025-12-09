package view;

import javax.swing.*;

/**
 * Überklasse für Room und Hub
 */
class JLayeredView extends JLayeredPane {
    JLayeredView() {}

    void showIntro(String text) {
        setButtonsEnabled(false);

        IntroOverlay overlay = new IntroOverlay(text, () -> setButtonsEnabled(true));
        overlay.showOn(this);
    }

    void setButtonsEnabled(boolean enabled) {};
}
