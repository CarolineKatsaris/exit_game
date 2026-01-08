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
    private java.awt.event.ActionListener overlayClosedListener;


    JLayeredView() {
        setLayout(null);
    }

    /**
     * Zeige Intro oder Outro Overlay an.
     * @param text
     */
    void showOverlay(String text) {
        setButtonsEnabled(false);
        TextOverlay overlay = new TextOverlay(text, () -> {
            setButtonsEnabled(true);
            fireOverlayClosed();
        });
        overlay.showOn(this);
    }

    public void setOverlayClosedListener(java.awt.event.ActionListener l) {
        this.overlayClosedListener = l;
    }

    protected void fireOverlayClosed() {
        if (overlayClosedListener != null) {
            overlayClosedListener.actionPerformed(null);
        }
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