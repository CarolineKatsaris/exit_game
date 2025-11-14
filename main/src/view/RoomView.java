// RoomView.java
package view;

import javax.swing.*;

public class RoomView extends JLayeredPane {

    private final JLabel background;
    private final JButton quiz1Btn; // GPU -> VRAM
    private final JButton quiz2Btn; // Framebuffer
    private final JButton quiz3Btn; // gestörter Screen

    public RoomView() {
        setLayout(null);

        ImageIcon bg = new ImageIcon(getClass()
                .getResource("/GraphicsCardRoomView_elements.png"));
        background = new JLabel(bg);
        background.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());
        add(background, Integer.valueOf(0)); // unterste Ebene

        // Koordinaten ggf. anpassen!
        quiz1Btn = makeInvisibleButton(400, 400, 120, 290, "quiz_1");
        quiz2Btn = makeInvisibleButton(1000, 330, 240, 180, "quiz_2");
        quiz3Btn = makeInvisibleButton(1130, 640, 260, 160, "quiz_3");

        add(quiz1Btn, Integer.valueOf(1));
        add(quiz2Btn, Integer.valueOf(1));
        add(quiz3Btn, Integer.valueOf(1));

        setPreferredSize(background.getPreferredSize());
    }

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

    // --- Passive View: nur Zugriff + Optik ändern ---

    public JButton getQuiz1Button() { return quiz1Btn; }
    public JButton getQuiz2Button() { return quiz2Btn; }
    public JButton getQuiz3Button() { return quiz3Btn; }

    public void setQuiz1Highlight(boolean on) { setHighlight(quiz1Btn, on); }
    public void setQuiz2Highlight(boolean on) { setHighlight(quiz2Btn, on); }
    public void setQuiz3Highlight(boolean on) { setHighlight(quiz3Btn, on); }

    private void setHighlight(JButton b, boolean on) {
        if (on) {
            b.setBorder(BorderFactory.createLineBorder(
                    new java.awt.Color(255, 255, 0), 3)); // gelber Rand
            b.setBorderPainted(true);
            b.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        } else {
            b.setBorder(BorderFactory.createEmptyBorder());
            b.setBorderPainted(false);
            b.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
        // NICHT: setOpaque(true) oder setContentAreaFilled(true)
        repaint();
    }
}
