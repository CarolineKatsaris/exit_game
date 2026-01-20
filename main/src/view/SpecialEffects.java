package view;

import javax.swing.*;
import java.awt.*;

/**
 * Kapselt Spezialeffekte für RoomView:
 * - Fog-Overlay (animiert)
 * - Zwei unabhängige Glow-Hotspots (Männchen):
 *   1) grün/rot
 *   2) blau/gelb
 *
 * Ein Overlay + ein Timer für beide Glows.
 */
public class SpecialEffects {

    private final RoomView view;

    // ---------- Fog ----------
    private JComponent fog1, fog2;
    private Timer fogTimer;
    private int fogX = 0;
    private int fogSpeed = 1;

    // ---------- Glow Overlay + Timer (gemeinsam) ----------
    private GlowOverlay glowOverlay;
    private Timer glowTimer;
    private float glowPhase = 0f;

    // ---------- Hotspot 1 (grün/rot) ----------
    private JButton manBtn1;
    private boolean enabled1 = false;
    private boolean altColor1 = false;
    private int cx1, cy1, r1;

    // ---------- Hotspot 2 (blau/gelb) ----------
    private JButton manBtn2;
    private boolean enabled2 = false;
    private boolean altColor2 = false;
    private int cx2, cy2, r2;

    public SpecialEffects(RoomView view) {
        this.view = view;
    }

    // =========================================================
    // Fog
    // =========================================================

    public void enableFog(boolean on) {
        if (fogTimer == null) {
            var url = RoomView.class.getResource("/fog.png");
            if (url == null) return;

            Image fogImage = new ImageIcon(url).getImage();

            fog1 = createFogTile(fogImage);
            fog2 = createFogTile(fogImage);

            view.add(fog1, Integer.valueOf(1));
            view.add(fog2, Integer.valueOf(1));

            fogTimer = new Timer(40, e -> {
                int w = view.getWidth();
                int h = view.getHeight();
                if (w <= 0 || h <= 0) return;

                fogX -= fogSpeed;
                if (fogX <= -w) fogX = 0;

                fog1.setBounds(fogX, 0, w, h);
                fog2.setBounds(fogX + w, 0, w, h);
            });

            SwingUtilities.invokeLater(() -> {
                int w = view.getWidth();
                int h = view.getHeight();
                if (w <= 0 || h <= 0) return;

                fog1.setBounds(0, 0, w, h);
                fog2.setBounds(w, 0, w, h);
            });
        }

        if (on) {
            fog1.setVisible(true);
            fog2.setVisible(true);
            if (!fogTimer.isRunning()) fogTimer.start();
        } else {
            if (fogTimer.isRunning()) fogTimer.stop();
            fog1.setVisible(false);
            fog2.setVisible(false);
        }
    }

    private JComponent createFogTile(Image fogImage) {
        return new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(fogImage, 0, 0, getWidth(), getHeight(), null);
            }
        };
    }

    // =========================================================
    // Glow Hotspots (2x)
    // =========================================================

    /** Hotspot 1: grün/rot */
    public void setupRamManHotspot(Rectangle bounds) {
        if (manBtn1 == null) {
            manBtn1 = new JButton();
            makeInvisible(manBtn1);
            manBtn1.addActionListener(e -> {
                if (enabled1) altColor1 = !altColor1;
            });
            view.add(manBtn1, Integer.valueOf(2));
        }

        manBtn1.setBounds(bounds);

        cx1 = bounds.x + bounds.width / 2;
        cy1 = bounds.y + bounds.height / 2;
        r1 = Math.max(bounds.width, bounds.height);

        enabled1 = true;
        startGlow();
    }

    /** Hotspot 2: blau/gelb */
    public void setupRamManHotspot2(Rectangle bounds) {
        if (manBtn2 == null) {
            manBtn2 = new JButton();
            makeInvisible(manBtn2);
            manBtn2.addActionListener(e -> {
                if (enabled2) altColor2 = !altColor2;
            });
            view.add(manBtn2, Integer.valueOf(2));
        }

        manBtn2.setBounds(bounds);

        cx2 = bounds.x + bounds.width / 2;
        cy2 = bounds.y + bounds.height / 2;
        r2 = Math.max(bounds.width, bounds.height);

        enabled2 = true;
        startGlow();
    }

    /** Unabhängig schalten */
    public void setRamMan1Enabled(boolean on) {
        enabled1 = on;
        // optional: Klick deaktivieren (Glow-Schalter bleibt trotzdem maßgeblich)
        if (manBtn1 != null) manBtn1.setEnabled(on);
        if (glowOverlay != null) glowOverlay.repaint();
    }

    /** Unabhängig schalten */
    public void setRamMan2Enabled(boolean on) {
        enabled2 = on;
        if (manBtn2 != null) manBtn2.setEnabled(on);
        if (glowOverlay != null) glowOverlay.repaint();
    }

    private static void makeInvisible(AbstractButton b) {
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
    }

    private void startGlow() {
        if (glowOverlay == null) {
            glowOverlay = new GlowOverlay();
            view.add(glowOverlay, Integer.valueOf(9));
            glowOverlay.setBounds(0, 0, view.getWidth(), view.getHeight());
        }

        if (glowTimer == null) {
            glowTimer = new Timer(50, e -> {
                glowPhase += 0.12f;
                glowOverlay.repaint();
            });
            glowTimer.start();
        } else if (!glowTimer.isRunning()) {
            glowTimer.start();
        }
    }

    private class GlowOverlay extends JComponent {
        GlowOverlay() { setOpaque(false); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            float pulse = (float) ((Math.sin(glowPhase) + 1.0) / 2.0);

            // Hotspot 1: grün/rot
            if (enabled1) {
                paintOneGlow(
                        g2, pulse,
                        cx1, cy1, r1,
                        altColor1,
                        new Color(0, 255, 0),    // grün
                        new Color(255, 0, 0)     // rot
                );
            }

            // Hotspot 2: blau/gelb
            if (enabled2) {
                paintOneGlow(
                        g2, pulse,
                        cx2, cy2, r2,
                        altColor2,
                        new Color(0, 140, 255),  // blau (angenehmer als reines 0,0,255)
                        new Color(255, 255, 0)   // gelb
                );
            }

            g2.dispose();
        }

        private void paintOneGlow(Graphics2D g2, float pulse,
                                  int cx, int cy, int r,
                                  boolean altColor,
                                  Color base, Color alt) {
            if (r <= 0) return;

            int radius = (int) (r * 0.6 + pulse * (r * 0.35));
            float alpha = 0.30f + pulse * 0.35f;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            Color chosen = altColor ? alt : base;

            Color inner = new Color(chosen.getRed(), chosen.getGreen(), chosen.getBlue(), 180);
            Color outer = new Color(chosen.getRed(), chosen.getGreen(), chosen.getBlue(), 0);

            RadialGradientPaint paint = new RadialGradientPaint(
                    new Point(cx, cy),
                    radius,
                    new float[]{0f, 1f},
                    new Color[]{inner, outer}
            );

            g2.setPaint(paint);
            g2.fillOval(cx - radius, cy - radius, 2 * radius, 2 * radius);
        }
    }
}
