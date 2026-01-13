// RoomView.java
package view;

import javax.swing.*;
import java.awt.*;

import static java.lang.Integer.valueOf;

/**
 * View-Klasse für einen Raum-Screen.
 * Enthält:
 * <ul>
 *   <li>Unsichtbare Klickflächen für die Quiz-Buttons</li>
 *   <li>Zurück-Button</li>
 *   <li>ProgressBar</li>
 *   <li>Optional: Nebel-Overlay (animiert) für besondere Räume (z.B. CPU)</li>
 * </ul>
 */

public class RoomView extends JLayeredView {

    // UI-Komponenten
    final JButton[] klickButtons = new JButton[3];
    private final JButton back;
    private final JProgressBar progressBar;

    //Fog-Overlayx
    private JComponent fog1, fog2;
    private Timer fogTimer;
    private int fogX = 0;
    private int fogSpeed = 1;


    /**
     * Erzeugt die RoomView mit Back-Button und ProgressBar.
     * Die Quiz-Klickflächen werden später über {@link #setKlickButtons(Rectangle[])} gesetzt.
     */
    public RoomView() {
        super();

        //nur Zurück Button erzeugen
        back = new JButton("Zurück");
        back.setFont(new Font("SansSerif", Font.BOLD, 16));
        back.setBounds(20,20,100,50);
        add(back, valueOf(2)); //auf oberste Ebene legen
        //PrograssBar
        progressBar = new JProgressBar(0, 15);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(0, 100, 0));
        progressBar.setOpaque(true);
        progressBar.setBackground(Color.WHITE);
        progressBar.setBounds(20, 750,      // 40px vom unteren Rand
                250,       // links/rechts 20px Abstand
                20
        );
        progressBar.setStringPainted(true);
        progressBar.setBorderPainted(false);
        add(progressBar, Integer.valueOf(3)); // über Background, unter Buttons
    }

    /**
     * Setzt/aktualisiert die Bounds der drei unsichtbaren Quiz-Buttons.
     * Buttons werden nur beim ersten Aufruf erstellt, danach nur repositioniert.
     *
     * @param buttonBounds Array mit genau 3 Rectangles (Quiz1..Quiz3)
     */
    void setKlickButtons(Rectangle[] buttonBounds) {
        for (int i = 0; i < buttonBounds.length; i++) {

            // Button erstellen
            if (klickButtons[i] == null) {
                klickButtons[i] = makeInvisibleButton(0, 0, 0, 0, "quiz_" + i);
                add(klickButtons[i], Integer.valueOf(1));
            }

            // Nur Bounds aktualisieren (Button bleibt derselbe)
            klickButtons[i].setBounds(
                    buttonBounds[i].x,
                    buttonBounds[i].y,
                    buttonBounds[i].width,
                    buttonBounds[i].height
            );
        }
    }

    /**
     * Erstellt einen transparenten JButton als Klickfläche.
     */
    private JButton makeInvisibleButton(int x, int y, int w, int h, String cmd) {
        JButton b = new JButton();
        b.setBounds(x, y, w, h);
        // immer transparent
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);

        b.setActionCommand(cmd);
        return b;
    }

    // Zugriffsmethoden Controller
    // --- Passive View: nur Zugriff + Optik ändern ---

    public JButton getQuiz1Button() { return klickButtons[0]; }
    public JButton getQuiz2Button() { return klickButtons[1]; }
    public JButton getQuiz3Button() { return klickButtons[2]; }
    public JButton getBackButton() { return back; };

    public void setQuiz1Highlight(boolean on) { setHighlight(klickButtons[0], on); }
    public void setQuiz2Highlight(boolean on) { setHighlight(klickButtons[1], on); }
    public void setQuiz3Highlight(boolean on) { setHighlight(klickButtons[2], on); }


    /**
     * Färbt Buttons mit gelben Rand ein und ändert den Cursor
     * @param b JButton
     * @param on boolean
     */
    private void setHighlight(JButton b, boolean on) {
        if (on) {
            b.setBorder(BorderFactory.createLineBorder(
                    new Color(255, 255, 0), 3)); // gelber Rand
            b.setBorderPainted(true);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            b.setBorder(BorderFactory.createEmptyBorder());
            b.setBorderPainted(false);
            b.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        repaint();
    }

    /**
     * Aktiviert oder deaktiviert den animierten Nebel-Overlay.
     *
     * @param on true = Nebel sichtbar + Timer läuft, false = Nebel aus + Timer stoppt
     */
    /**
     * Aktiviert oder deaktiviert den animierten Nebel-Overlay.
     *
     * @param on true = Nebel sichtbar + Timer läuft,
     *           false = Nebel aus + Timer stoppt
     */
    public void enableFog(boolean on) {

        // Initialisierung nur beim ersten Aufruf
        if (fogTimer == null) {

            // Nebelbild aus den Ressourcen laden
            var url = RoomView.class.getResource("/fog.png");
            if (url == null) {
                return;
            }

            Image fogImage = new ImageIcon(url).getImage();

            // Erste Nebel-Kachel
            fog1 = new JComponent() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(fogImage, 0, 0, getWidth(), getHeight(), null);
                }
            };

            // Zweite Nebel-Kachel (für nahtlosen Übergang)
            fog2 = new JComponent() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(fogImage, 0, 0, getWidth(), getHeight(), null);
                }
            };

            // Beide Nebel-Ebenen zur View hinzufügen
            add(fog1, Integer.valueOf(1));
            add(fog2, Integer.valueOf(1));

            // Timer für die Bewegung des Nebels
            fogTimer = new Timer(40, e -> {

                int viewWidth = getWidth();
                int viewHeight = getHeight();

                // Nebel nach links bewegen
                fogX = fogX - fogSpeed;

                // Wenn der erste Nebel komplett links verschwunden ist,
                // starte den Zyklus neu
                if (fogX <= -viewWidth) {
                    fogX = 0;
                }

                // Nebel-Ebenen positionieren
                fog1.setBounds(fogX, 0, viewWidth, viewHeight);
                fog2.setBounds(fogX + viewWidth, 0, viewWidth, viewHeight);
            });

            // Startposition nach dem Layout setzen
            SwingUtilities.invokeLater(() -> {
                int viewWidth = getWidth();
                int viewHeight = getHeight();

                if (viewWidth <= 0 || viewHeight <= 0) {
                    return;
                }

                fog1.setBounds(0, 0, viewWidth, viewHeight);
                fog2.setBounds(viewWidth, 0, viewWidth, viewHeight);
            });
        }

        // Nebel ein- oder ausschalten
        if (on) {
            fog1.setVisible(true);
            fog2.setVisible(true);

            if (!fogTimer.isRunning()) {
                fogTimer.start();
            }
        } else {
            if (fogTimer.isRunning()) {
                fogTimer.stop();
            }

            fog1.setVisible(false);
            fog2.setVisible(false);
        }
    }

    /*
    Updatet ProgressBar, dabei wird der Wert vom Model an den Quizcontroller weitergegeben (immer um 1 erhöht, weil die Bar von 0-15 geht)
     */
    public void updateProgressBar(int value){
    progressBar.setValue(value);
    }
}

