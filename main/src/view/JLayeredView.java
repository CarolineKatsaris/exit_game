package view;

import javax.swing.*;

import java.awt.*;

import static java.lang.Integer.valueOf;

/**
 * Basisklasse für Hub- und Room-Views auf Basis eines {@link JLayeredPane}.
 * <p>
 * Stellt u.a. folgende Funktionen bereit:
 * <ul>
 *   <li>Hintergrundbild setzen (Layer 0)</li>
 *   <li>Overlay-Text anzeigen und nach dem Schließen ein Event auslösen</li>
 *   <li>Fehlermeldungs-Banner (oben mittig) mit Auto-Hide</li>
 * </ul>
 * </p>
 */
class JLayeredView extends JLayeredPane {
    JLabel background;
    String bgImagePath;

    private java.awt.event.ActionListener overlayClosedListener;
    private JLabel errorBanner;
    private Timer errorTimer;


    /**
     * Initialisiert das Layered View mit absolutem Layout und vorbereitetem Error-Banner.
     */
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
     * Zeigt ein Text-Overlay (z.B. Intro/Outro) an und deaktiviert währenddessen Buttons.
     * Nach dem Schließen wird das OverlayClosed-Event ausgelöst.
     *
     * @param text anzuzeigender Overlay-Text
     */
    void showOverlay(String text) {
        setButtonsEnabled(false);
        TextOverlay overlay = new TextOverlay(text, () -> {
            setButtonsEnabled(true);
            fireOverlayClosed();
        });
        overlay.showOn(this);
    }

    /**
     * Setzt einen Listener, der aufgerufen wird, sobald ein Overlay geschlossen wurde.
     *
     * @param l ActionListener (wird mit {@code null} Event aufgerufen)
     */
    public void setOverlayClosedListener(java.awt.event.ActionListener l) {
        this.overlayClosedListener = l;
    }

    /**
     * Löst das OverlayClosed-Event aus, falls ein Listener registriert ist.
     */
    protected void fireOverlayClosed() {
        if (overlayClosedListener != null) {
            overlayClosedListener.actionPerformed(null);
        }
    }

    /**
     * Aktiviert/Deaktiviert Buttons/Klickbereiche im View.
     * Subklassen überschreiben diese Methode und schalten ihre Buttons.
     *
     * @param enabled {@code true} aktiviert, {@code false} deaktiviert
     */
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
    /**
     * Zeigt eine Fehlermeldung als Banner an und blendet diese nach 3 Sekunden automatisch aus.
     *
     * @param message anzuzeigender Text
     */
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