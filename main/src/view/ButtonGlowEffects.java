package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class ButtonGlowEffects {

    private final JLayeredView view;

    private GlowOverlay glowOverlay;
    private Timer glowTimer;
    private float glowPhase = 0f;

    private static class GlowTarget {
        JButton btn;
        boolean enabled;
    }

    private final List<GlowTarget> targets = new ArrayList<>();

    public ButtonGlowEffects(JLayeredView view) {
        this.view = view;
    }

    public void register(JButton btn) {
        GlowTarget t = new GlowTarget();
        t.btn = btn;
        t.enabled = false;
        targets.add(t);
        start();
    }

    public void setEnabled(JButton btn, boolean on) {
        for (GlowTarget t : targets) {
            if (t.btn == btn) {
                t.enabled = on;
                if (glowOverlay != null) glowOverlay.repaint();
                return;
            }
        }
    }

    private void start() {
        if (glowOverlay == null) {
            glowOverlay = new GlowOverlay();
            // Layer so wählen, dass es sichtbar ist, aber nie klickt (contains=false).
            view.add(glowOverlay, Integer.valueOf(5));
            glowOverlay.setBounds(0, 0, view.getWidth(), view.getHeight());
        }

        if (glowTimer == null) {
            glowTimer = new Timer(50, e -> {
                glowPhase += 0.12f;
                if (glowOverlay != null) {
                    glowOverlay.setBounds(0, 0, view.getWidth(), view.getHeight());
                    glowOverlay.repaint();
                }
            });
            glowTimer.start();
        }
    }

    private class GlowOverlay extends JComponent {
        GlowOverlay() {
            setOpaque(false);
            setFocusable(false);
        }

        @Override
        public boolean contains(int x, int y) {
            return false; // niemals Maus-Target
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            float pulse = (float) ((Math.sin(glowPhase) + 1.0) / 2.0);

            for (GlowTarget t : targets) {
                if (!t.enabled) continue;

                Rectangle r = SwingUtilities.convertRectangle(
                        t.btn.getParent(),
                        t.btn.getBounds(),
                        view
                );

                paintGlow(g2, r, pulse);
            }

            g2.dispose();
        }

        private void paintGlow(Graphics2D g2, Rectangle r, float pulse) {
            int inset = 4;                 // kaschiert Ungenauigkeiten
            Rectangle rr = new Rectangle(r);
            rr.grow(-inset, -inset);
            if (rr.width <= 0 || rr.height <= 0) return;

            int alpha = 77;                // 30%
            float strength = 0.9f + 0.1f * pulse;

            int cx = rr.x + rr.width / 2;
            int cy = rr.y + rr.height / 2;
            float radius = (float) Math.hypot(rr.width / 2f, rr.height / 2f) * 1.1f;

            RadialGradientPaint paint = new RadialGradientPaint(
                    new Point2D.Float(cx, cy),
                    radius,
                    new float[]{0f, 0.85f, 1f},
                    new Color[]{
                            new Color(255, 255, 0, (int) (alpha * strength)),
                            new Color(255, 255, 0, (int) (alpha * 0.15f * strength)),
                            new Color(255, 255, 0, 0)
                    }
            );

            Paint old = g2.getPaint();
            g2.setPaint(paint);
            g2.fillRect(rr.x, rr.y, rr.width, rr.height);
            g2.setPaint(old);
        }
    }
}
