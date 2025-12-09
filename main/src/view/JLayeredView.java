package view;

import javax.swing.*;

/**
 * Überklasse für Room und Hub
 */
class JLayeredView extends JLayeredPane {
    JLayeredView() {}

    void showIntro() {
        setButtonsEnabled(false);

        String text =
                "Tja, das war eine Falle!<br>" +
                        "Jetzt bist du mein Gefangener und ich glaube nicht, " +
                        "dass du es schaffst hier wieder rauszukommen –<br>" +
                        "Ich werde jetzt die ganze Schule infizieren ...";

        IntroOverlay overlay = new IntroOverlay(text, () -> setButtonsEnabled(true));
        overlay.showOn(this);
    }

    void setButtonsEnabled(boolean enabled) {};
}
