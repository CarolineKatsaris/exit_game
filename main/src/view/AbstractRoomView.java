package view;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractRoomView extends JLayeredPane {

    protected JLabel background;
    protected JButton quiz1Btn;
    protected JButton quiz2Btn;
    protected JButton quiz3Btn;
    protected JButton back;

    public AbstractRoomView() {
        setLayout(null);

        // Hintergrund aus Subklasse
        ImageIcon bg = new ImageIcon(
                getClass().getResource(getBackgroundImagePath())
        );
        background = new JLabel(bg);
        background.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());
        add(background, Integer.valueOf(0));

        // Quizbuttons aus Subklasse
        Rectangle[] bounds = getQuizButtonBounds();
        quiz1Btn = makeInvisibleButton(bounds[0], "quiz_1");
        quiz2Btn = makeInvisibleButton(bounds[1], "quiz_2");
        quiz3Btn = makeInvisibleButton(bounds[2], "quiz_3");

        add(quiz1Btn, Integer.valueOf(1));
        add(quiz2Btn, Integer.valueOf(1));
        add(quiz3Btn, Integer.valueOf(1));

        setPreferredSize(background.getPreferredSize());

        // Back-Button bleibt überall gleich (oder später auch abstrakt machen)
        back = new JButton("Zurück");
        back.setBounds(20, 20, 100, 30);
        add(back, Integer.valueOf(2));
    }

    // ─────────────────────────────────────────────────────────
    // Abstrakte „Konfigurations“-Methoden für die Subklassen
    // ─────────────────────────────────────────────────────────

    /** Pfad zum Hintergrundbild, z.B. "/GraphicsCardRoomView_elements.png" */
    protected abstract String getBackgroundImagePath();

    /** Drei Bounds für die Quiz-Hotspots (quiz_1, quiz_2, quiz_3) */
    protected abstract Rectangle[] getQuizButtonBounds();

    // ─────────────────────────────────────────────────────────
    // Hilfsmethoden & Getter (wie bisher in RoomView)
    // ─────────────────────────────────────────────────────────

    private JButton makeInvisibleButton(Rectangle r, String cmd) {
        JButton b = new JButton();
        b.setBounds(r);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setActionCommand(cmd);
        return b;
    }

    public JButton getQuiz1Button() { return quiz1Btn; }
    public JButton getQuiz2Button() { return quiz2Btn; }
    public JButton getQuiz3Button() { return quiz3Btn; }
    public JButton getBackButton()  { return back; }

    public void setQuiz1Highlight(boolean on) { setHighlight(quiz1Btn, on); }
    public void setQuiz2Highlight(boolean on) { setHighlight(quiz2Btn, on); }
    public void setQuiz3Highlight(boolean on) { setHighlight(quiz3Btn, on); }

    private void setHighlight(JButton b, boolean on) {
        if (on) {
            b.setBorder(BorderFactory.createLineBorder(
                    new java.awt.Color(255, 255, 0), 3));
            b.setBorderPainted(true);
            b.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        } else {
            b.setBorder(BorderFactory.createEmptyBorder());
            b.setBorderPainted(false);
            b.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
        repaint();
    }
}
