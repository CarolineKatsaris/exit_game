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
    private JLabel errorBanner;
    private Timer errorTimer;



    JLayeredView() {
        setLayout(null);
        errorBanner = new JLabel();
        errorBanner.setOpaque(true);
        errorBanner.setBackground(new Color(120, 0, 0, 200)); // dunkelrot, leicht transparent
        errorBanner.setForeground(Color.WHITE);
        errorBanner.setFont(new Font("SansSerif", Font.BOLD, 20));
        errorBanner.setHorizontalAlignment(SwingConstants.CENTER);
        errorBanner.setVisible(false);

        // oben mittig (für 1536x1024)
        int w = 1300;
        int h = 80;
        errorBanner.setBounds((1536 - w) / 2, 250, w, h);

        add(errorBanner, Integer.valueOf(20)); // sehr weit oben im Layer

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

    public void showErrorBanner(String message) {
        errorBanner.setText(message);
        errorBanner.setVisible(true);

        // alten Timer stoppen (falls noch einer läuft)
        if (errorTimer != null) errorTimer.stop();

        // 3000 ms = 3 Sekunden
        errorTimer = new Timer(3000, e -> errorBanner.setVisible(false));
        errorTimer.setRepeats(false);
        errorTimer.start();
    }


}