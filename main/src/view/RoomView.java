package view;

import javax.swing.*;

public class RoomView extends JLayeredPane {

    private final JLabel background;
    private final JButton quiz1Btn; // GPU→VRAM
    private final JButton quiz2Btn; // Framebuffer
    private final JButton quiz3Btn; // gestörter Screen

    public RoomView() {
        setLayout(null);

        ImageIcon bg = new ImageIcon(getClass().getResource("/GraphicsCardRoomView_elements.png"));
        background = new JLabel(bg);
        background.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());
        add(background, Integer.valueOf(0)); // Hintergrund-Ebene

        // Unsichtbare Buttons (Positionen anpassen!)
        quiz1Btn = makeInvisible(180, 250, 120, 120, "quiz_1");
        quiz2Btn = makeInvisible(800, 350, 180, 130, "quiz_2");
        quiz3Btn = makeInvisible(720, 200, 200, 100, "quiz_3");

        add(quiz1Btn, Integer.valueOf(1));
        add(quiz2Btn, Integer.valueOf(1));
        add(quiz3Btn, Integer.valueOf(1));

        setPreferredSize(background.getPreferredSize());
    }

    private JButton makeInvisible(int x, int y, int w, int h, String action) {
        JButton b = new JButton();
        b.setBounds(x, y, w, h);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setActionCommand(action);
        return b;
    }

    // Passive-View: nur Zugriff/Registrierungs-Methoden
    public JButton getQuiz1Button() { return quiz1Btn; }
    public JButton getQuiz2Button() { return quiz2Btn; }
    public JButton getQuiz3Button() { return quiz3Btn; }
}
