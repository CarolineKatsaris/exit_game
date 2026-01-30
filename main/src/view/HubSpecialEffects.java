package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class HubSpecialEffects {

    private final HubView view;

    private GlowOverlay glowOverlay;
    private Timer glowTimer;
    private float glowPhase = 0f;

    private static class GlowTarget {
        JButton btn;
        boolean enabled;
    }

    private final List<GlowTarget> buttonGlows = new ArrayList<>();

    public HubSpecialEffects(HubView view) {
        this.view = view;
    }

    public void registerButtonGlow(JButton btn) {
        GlowTarget t = new GlowTarget();
        t.btn = btn;
        t.enabled = false;
        buttonGlows.add(t);

        startGlow();
    }

    public void setButtonGlowEnabled(JButton btn, boolean on) {
        for (GlowTarget t : buttonGlows) {
            if (t.btn == btn) {
                t.enabled = on;
                if (glowOverlay != null) glowOverlay.repaint();
                return;
            }
        }
    }

    private void startGlow() {
        if (glowOverlay == null) {
            glowOverlay = new GlowOverlay();

            // WICHTIG: Layer-Entscheidung siehe Fix 2 unten!
            view.add(glowOverlay, Integer.valueOf(2));

            // einmalig ok, aber reicht nicht
            glowOverlay.setBounds(0, 0, view.getWidth(), view.getHeight());
        }

        if (glowTimer == null) {
            glowTimer = new Timer(50, e -> {
                glowPhase += 0.12f;

                if (glowOverlay != null) {
                    // NEU: Bounds nachziehen (HubView ist beim Konstruktor oft 0x0)
                    glowOverlay.setBounds(0, 0, view.getWidth(), view.getHeight());
                    glowOverlay.repaint();
                }
            });
            glowTimer.start();
        }
    }


    private class GlowOverlay extends JComponent {
        GlowOverlay() { setOpaque(false); }

        @Override
        public boolean contains(int x, int y) {
            return false; // Maus geht "durch" das Overlay durch
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            float pulse = (float) ((Math.sin(glowPhase) + 1.0) / 2.0);

            for (GlowTarget t : buttonGlows) {
                if (!t.enabled) continue;

                Rectangle r = SwingUtilities.convertRectangle(
                        t.btn.getParent(),
                        t.btn.getBounds(),
                        view
                );

                paintButtonOverlayGlow(g2, r, pulse);
            }

            g2.dispose();
        }

        private void paintButtonOverlayGlow(Graphics2D g2, Rectangle r, float pulse) {

            // Optional: leicht nach innen, um Ungenauigkeiten zu kaschieren
            int inset = 4; // 0..10
            Rectangle rr = new Rectangle(r);
            rr.grow(-inset, -inset);
            if (rr.width <= 0 || rr.height <= 0) return;

            // 30% Alpha
            int alphaInner = 77;

            // optional: sanftes Atmen, kann auch 1.0f sein
            float strength = 0.9f + 0.1f * pulse;

            int cx = rr.x + rr.width / 2;
            int cy = rr.y + rr.height / 2;

            // ✅ WICHTIG: Radius = halbe Diagonale (bis zur Ecke), leicht größer für weicheren Rand
            float halfW = rr.width / 2f;
            float halfH = rr.height / 2f;
            float radius = (float) Math.hypot(halfW, halfH) * 1.10f;

            RadialGradientPaint paint = new RadialGradientPaint(
                    new java.awt.geom.Point2D.Float(cx, cy),
                    radius,
                    new float[]{0.0f, 0.85f, 1.0f}, // späterer Abfall -> weicher
                    new Color[]{
                            new Color(255, 255, 0, (int) (alphaInner * strength)), // ~30%
                            new Color(255, 255, 0, (int) (alphaInner * 0.15f * strength)), // fast transparent
                            new Color(255, 255, 0, 0) // 0%
                    }
            );

            Paint old = g2.getPaint();
            g2.setPaint(paint);

            // Rechteck oder RoundRect – unabhängig vom Feathering
            g2.fillRect(rr.x, rr.y, rr.width, rr.height);
            // g2.fillRoundRect(rr.x, rr.y, rr.width, rr.height, 20, 20);

            g2.setPaint(old);
        }



    }
}
