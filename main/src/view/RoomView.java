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
    private final ButtonGlowEffects glow;
    private final JButton back;
    private final SpecialEffects effects;
    //private final JProgressBar progressBar;
    private final Font retroFont = new Font(Font.MONOSPACED, Font.BOLD, 18);

    private static final Color BG_DARK = new Color(18, 42, 32);
    private static final Color FG_CYBER = new Color(200, 230, 215);
    private static final Color BORDER_RED = new Color(180, 40, 40);

    private static final int MAX_VIRUSES = 15;

    private static final int ICON_SIZE = 44;
    // ~1 cm / ~2 cm in px (bei ~96 DPI)
    private static final int SHIFT_X = 38;
    private static final int SHIFT_Y = 76;

    // Panelgröße etwas größer, weil Icons jetzt doppelt so groß sind
    private static final int VIRUS_PANEL_W = 320;
    private static final int VIRUS_PANEL_H = 140;
    private static final int FOG_PAD = 80; // mehr = größerer Nebel-Rand

    private final JLabel[] virusIcons = new JLabel[MAX_VIRUSES];
    private ImageIcon virusIconScaled;
    private Image fogImage;
    private Timer wobbleTimer;
    private final Point[] virusBasePos = new Point[MAX_VIRUSES];
    private double wobblePhase = 0;



    /**
     * Erzeugt die RoomView mit Back-Button und ProgressBar.
     * Die Quiz-Klickflächen werden später über {@link #setKlickButtons(Rectangle[])} gesetzt.
     */
    public RoomView() {
        super();

        //nur Zurück Button erzeugen
        back = new JButton("Zurück");
        back.setFont(retroFont.deriveFont(18f));
        back.setBounds(20, 20, 140, 52);

        back.setFocusPainted(false);
        back.setOpaque(true);
        back.setContentAreaFilled(true);

        back.setBackground(FG_CYBER);
        back.setForeground(BG_DARK);
        back.setBorder(BorderFactory.createLineBorder(BORDER_RED, 3));

        add(back, valueOf(2)); //auf oberste Ebene legen


        glow = new ButtonGlowEffects(this);
        effects = new SpecialEffects(this);

        // 1) Fog-PNG laden (Resource)
        fogImage = new ImageIcon(
                getClass().getResource("/virus_fog.png")
        ).getImage();

// 2) Virus-Icon laden + skalieren
        ImageIcon raw = new ImageIcon(getClass().getResource("/Virus.png"));
        Image img = raw.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        virusIconScaled = new ImageIcon(img);

// 3) Panel platzieren (verschoben wie gewünscht)
        virusPanel.setOpaque(false);

        int baseX = 20;
        int baseY = 710;
        int oldX = baseX + SHIFT_X;
        int oldY = baseY + SHIFT_Y;

        virusPanel.setBounds(
                oldX - FOG_PAD,
                oldY - FOG_PAD,
                VIRUS_PANEL_W + 2 * FOG_PAD,
                VIRUS_PANEL_H + 2 * FOG_PAD
        );


        add(virusPanel, Integer.valueOf(3));

// 4) feste Koordinaten (Hybrid-Haufen) – Werte sind bereits auf ICON_SIZE=44 ausgelegt
        Point[] pos = new Point[] {
                new Point(20, 95), new Point(55, 90), new Point(90, 98), new Point(125, 92), new Point(160, 100),
                new Point(40, 62), new Point(78, 56), new Point(112, 64), new Point(145, 58), new Point(178, 66),
                new Point(62, 25), new Point(98, 20), new Point(132, 28), new Point(168, 22), new Point(205, 30)
        };

// 5) Labels anlegen
        for (int i = 0; i < MAX_VIRUSES; i++) {
            JLabel l = new JLabel(virusIconScaled);
            l.setBounds(pos[i].x + FOG_PAD, pos[i].y + FOG_PAD, ICON_SIZE, ICON_SIZE);
            virusBasePos[i] = new Point(l.getX(), l.getY());
            virusIcons[i] = l;
            virusPanel.add(l);
        }

// 6) Startzustand
        setVirusCount(MAX_VIRUSES);

        wobbleTimer = new Timer(40, e -> {
            wobblePhase += 0.3;

            for (int i = 0; i < MAX_VIRUSES; i++) {
                JLabel l = virusIcons[i];
                Point base = virusBasePos[i];
                if (l == null || base == null || !l.isVisible()) continue;

                int offsetX = (int) (Math.sin(wobblePhase + i) * 4);
                int offsetY = (int) (Math.cos(wobblePhase * 1.3 + i) * 2);

                l.setLocation(base.x + offsetX, base.y + offsetY);
            }
        });
        wobbleTimer.start();



    }

    private void setVirusCount(int count) {
        count = Math.max(0, Math.min(MAX_VIRUSES, count));

        for (int i = 0; i < MAX_VIRUSES; i++) {
            if (virusIcons[i] != null) {           // schützt vor NPE, falls mal früher aufgerufen
                virusIcons[i].setVisible(i < count);
            }
        }

        virusPanel.revalidate();
        virusPanel.repaint();
    }

    public void updateProgressBar(int value) {
        int remaining = MAX_VIRUSES - value; // value = gelöste Quizze (0..15)
        setVirusCount(remaining);
    }

    private final JPanel virusPanel = new JPanel(null) {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (fogImage != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Nebel leicht größer zeichnen als Panel
                int w = getWidth();
                int h = getHeight();
                double s = 1.2; // 1.1 bis 1.4
                int dw = (int)(w * s);
                int dh = (int)(h * s);
                int dx = (w - dw) / 2;
                int dy = (h - dh) / 2;
                // Nebel etwas nach links verschieben
                g2.drawImage(fogImage, -30, 0, w, h, this);

                g2.dispose();
            }
        }
    };




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

                // NEU: Glow für diesen Button registrieren (nur einmal!)
                glow.register(klickButtons[i]);

                // Optional: Hand-Cursor direkt am Button (HoverAdapter kann bleiben)
                klickButtons[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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

    public void setQuiz1Highlight(boolean on) {glow.setEnabled(klickButtons[0], on); }
    public void setQuiz2Highlight(boolean on) {glow.setEnabled(klickButtons[1], on); }
    public void setQuiz3Highlight(boolean on) {glow.setEnabled(klickButtons[2], on); }





    public void enableFog(boolean on) {
        effects.enableFog(on);
    }

    public void setupRamManHotspot(Rectangle bounds) {
        effects.setupRamManHotspot(bounds);
    }

    public void setupRamManHotspot2(Rectangle bounds) {
        effects.setupRamManHotspot2(bounds);
    }



}

