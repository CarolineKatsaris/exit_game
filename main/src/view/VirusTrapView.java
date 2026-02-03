package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VirusTrapView extends JLayeredView {

    private final DigitRainPanel rain;
    private final JLabel virusLabel;
    private final VirusText textOverlay;
    private Timer textFadeTimer;
    private float textAlpha = 0f;





    private Timer autoAdvanceTimer;

    public VirusTrapView() {
        super();

        // Hintergrund: Zahlenregen
        rain = new DigitRainPanel();
        rain.setBounds(0, 0, 1536, 1024);
        add(rain, Integer.valueOf(0));

        // Virusbild
        ImageIcon virusIcon = new ImageIcon(getClass().getResource("/Virus.png"));

        Image scaled = virusIcon.getImage().getScaledInstance(520, 520, Image.SCALE_SMOOTH);
        virusLabel = new JLabel(new ImageIcon(scaled));
        virusLabel.setBounds(80, 240, 520, 520);
        add(virusLabel, Integer.valueOf(5));

        textOverlay = new VirusText (
                "TJA... DAS WAR EINE FALLE.",
                "Du bist jetzt mein Gefangener",
                "und ich bezweifle, dass du",
                "hier wieder rauskommst."
        );
        textOverlay.setBounds(0, 0, 1536, 1024);
        add(textOverlay, Integer.valueOf(20)); // <- über allem



        // Bei Resize alles passend halten
        addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                rain.setSize(getSize());

                // Optional: dynamisch neu positionieren
                // Virus links, Bubble rechts – grob responsiv
                int w = getWidth();
                int h = getHeight();

                int vSize = Math.min(520, Math.min(w / 2 - 80, h - 200));
                vSize = Math.max(320, vSize);

                ImageIcon base = new ImageIcon(getClass().getResource("/Virus.png"));
                Image vScaled = base.getImage().getScaledInstance(vSize, vSize, Image.SCALE_SMOOTH);
                virusLabel.setIcon(new ImageIcon(vScaled));
                virusLabel.setBounds(60, (h - vSize) / 2, vSize, vSize);

                int textX = virusLabel.getX() + virusLabel.getWidth() + 30;
                int textY = virusLabel.getY() + 80;

                textOverlay.setAnchor(textX, textY);


                int bx = virusLabel.getX() + virusLabel.getWidth() + 40;
                int bw = Math.max(300, w - bx - 60);
                int bh = Math.min(340, h / 2);

                textOverlay.setSize(getSize());


            }
        });
    }




    /**
     * Startet die Sequenz:
     * - Zahlenregen animieren
     * - nach delayMs automatisch onDone ausführen (z.B. model.nextScreen())
     */
    public void start(Runnable onDone) {
        rain.start();

        // Text-Fade reset
        textAlpha = 0f;
        textOverlay.setAlpha(textAlpha);

        // Fade-In Timer (ca. 1.2s)
        if (textFadeTimer != null) textFadeTimer.stop();
        textFadeTimer = new Timer(33, e -> {
            textAlpha = Math.min(1f, textAlpha + 0.03f);
            textOverlay.setAlpha(textAlpha);
            textOverlay.repaint();
            if (textAlpha >= 1f) textFadeTimer.stop();
        });
        textFadeTimer.start();

        if (autoAdvanceTimer != null) autoAdvanceTimer.stop();

        int delayMs = 15_000; // 15 Sekunden
        autoAdvanceTimer = new Timer(delayMs, e -> {
            rain.stop();
            if (onDone != null) onDone.run();
        });
        autoAdvanceTimer.setRepeats(false);
        autoAdvanceTimer.start();
    }

    // Buttons gibt’s hier nicht, aber als Absicherung
    @Override
    void setButtonsEnabled(boolean enabled) {}

    // ─────────────────────────────────────────────────────────────
    //   Hintergrund: „Matrix“-Zahlenregen
    // ─────────────────────────────────────────────────────────────
    private static class DigitRainPanel extends JComponent {
        private static class Drop {
            int x;
            float y;
            float speed;
            char digit;
            int fontSize;
        }

        private final Random rnd = new Random();
        private final List<Drop> drops = new ArrayList<>();
        private Timer timer;

        DigitRainPanel() {
            setOpaque(true);

            addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override public void componentResized(java.awt.event.ComponentEvent e) {
                    initDrops(180); // mehr als vorher
                }
            });
        }


        void start() {
            if (timer != null && timer.isRunning()) return;

            timer = new Timer(33, e -> tick()); // ~30 FPS
            timer.start();
        }

        void stop() {
            if (timer != null) timer.stop();
        }

        private void initDrops(int count) {
            int w = getWidth();
            int h = getHeight();
            if (w <= 10 || h <= 10) return;

            drops.clear();
            for (int i = 0; i < count; i++) drops.add(newDrop(true));
        }


        private Drop newDrop(boolean randomY) {
            Drop d = new Drop();
            int w = Math.max(1, getWidth());
            int h = Math.max(1, getHeight());

            d.x = rnd.nextInt(Math.max(1, w));
            d.y = randomY ? rnd.nextInt(Math.max(1, h)) : -rnd.nextInt(300);
            d.speed = 2.0f + rnd.nextFloat() * 6.0f;
            d.digit = rnd.nextBoolean() ? '0' : '1';
            d.fontSize = 14 + rnd.nextInt(18);
            return d;
        }

        private void tick() {
            int h = getHeight();

            for (int i = 0; i < drops.size(); i++) {
                Drop d = drops.get(i);
                d.y += d.speed;

                // ab und zu Digit wechseln
                if (rnd.nextFloat() < 0.15f) {
                    d.digit = rnd.nextBoolean() ? '0' : '1';
                }

                if (d.y > h + 40) {
                    drops.set(i, newDrop(false));
                }
            }

            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                // Trail-Fade
                g2.setComposite(AlphaComposite.SrcOver.derive(0.20f));
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setComposite(AlphaComposite.SrcOver);

                // ALLES, was d benutzt, MUSS hier rein
                for (Drop d : drops) {

                    // Head etwas heller
                    g2.setFont(new Font("Monospaced", Font.BOLD, d.fontSize));
                    g2.setColor(new Color(140, 255, 170, 230));
                    g2.drawString(String.valueOf(d.digit), d.x, (int) d.y);

                    // Optional: Trail (2 Zeichen drüber)
                    g2.setColor(new Color(80, 255, 120, 120));
                    g2.drawString(String.valueOf(d.digit), d.x, (int) d.y - d.fontSize);
                    g2.drawString(String.valueOf(d.digit), d.x, (int) d.y - 2 * d.fontSize);
                }

            } finally {
                g2.dispose();
            }
        }

    }

    // ─────────────────────────────────────────────────────────────
    //   Text
    // ─────────────────────────────────────────────────────────────
    private static class VirusText extends JComponent {
        private final String title;
        private final String[] lines;
        private float alpha = 1f;
        private int baseX;
        private int baseY;

        void setAnchor(int x, int y) {
            this.baseX = x;
            this.baseY = y;
        }

        VirusText(String title, String... lines) {
            this.title = title;
            this.lines = lines;
            setOpaque(false);
        }

        void setAlpha(float a) {
            this.alpha = a;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                g2.setComposite(AlphaComposite.SrcOver.derive(alpha));



                // Position rechts neben Virus (robust: orientiert sich an Layout)
                // Wenn Virus links sitzt, passt das gut:
                int x = baseX;
                int y = baseY;


                // Titel groß/rot/dramatisch
                Font titleFont = new Font(Font.MONOSPACED, Font.BOLD, 50);
                g2.setFont(titleFont);

                // roter Glow/Shadow
                g2.setColor(new Color(120, 0, 0, 180));
                g2.drawString(title, x + 3, y + 3);
                g2.setColor(new Color(255, 40, 40));
                g2.drawString(title, x, y);

                // Body etwas kleiner, hell (oder leicht grünlich)
                Font bodyFont  = new Font(Font.MONOSPACED, Font.BOLD, 34);
                g2.setFont(bodyFont);
                FontMetrics fm = g2.getFontMetrics();
                int lineY = y + fm.getHeight() + 30;

                g2.setColor(new Color(0, 0, 0, 140)); // Shadow
                for (String line : lines) {
                    g2.drawString(line, x + 2, lineY + 2);
                    lineY += fm.getHeight() + 10;
                }

                lineY = y + fm.getHeight() + 30;
                g2.setColor(new Color(235, 235, 235)); // Textfarbe
                for (String line : lines) {
                    g2.drawString(line, x, lineY);
                    lineY += fm.getHeight() + 10;
                }

            } finally {
                g2.dispose();
            }
        }
    }





}
