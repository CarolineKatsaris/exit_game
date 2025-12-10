// RoomView.java
package view;

import javax.swing.*;
import java.awt.*;

import static java.lang.Integer.valueOf;

public class RoomView extends JLayeredView {

    private final JLabel background;
    final JButton[] quizButtons;
    private final JButton back;
    final String bgImagePath;
    final Rectangle[] quizBtnsBounds;
    private final JProgressBar progressBar;


    public RoomView(String bgImagePath, Rectangle[] quizBtnsBounds) {
        this.bgImagePath = bgImagePath;
        this.quizBtnsBounds = quizBtnsBounds;
        this.quizButtons = new JButton[quizBtnsBounds.length]; //initialize Array to length of quizBtnsBounds
        setLayout(null);

        ImageIcon bg = new ImageIcon(getClass().getResource(bgImagePath));
        background = new JLabel(bg);
        background.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());
        add(background, valueOf(0)); // unterste Ebene

        for(int i = 0; i < quizBtnsBounds.length; i++) { //Buttons in Schleife erstellen
            quizButtons[i] = makeInvisibleButton(quizBtnsBounds[i].x, quizBtnsBounds[i].y, quizBtnsBounds[i].width, quizBtnsBounds[i].height, "quiz_"+valueOf(i).toString());
            add(quizButtons[i], valueOf(1));
        }

        setPreferredSize(background.getPreferredSize());

        back = new JButton("Zurück");
        back.setFont(new Font("SansSerif", Font.BOLD, 16));
        back.setBounds(20,20,100,50);
        add(back, valueOf(2)); //auf oberste Ebene legen

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
        add(progressBar, Integer.valueOf(3)); // über Background, unter Buttons oder wie du willst
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

    public JButton getQuiz1Button() { return quizButtons[0]; }
    public JButton getQuiz2Button() { return quizButtons[1]; }
    public JButton getQuiz3Button() { return quizButtons[2]; }

    public void setQuiz1Highlight(boolean on) { setHighlight(quizButtons[0], on); }
    public void setQuiz2Highlight(boolean on) { setHighlight(quizButtons[1], on); }
    public void setQuiz3Highlight(boolean on) { setHighlight(quizButtons[2], on); }

   public JButton getBackButton() { return back; };

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

    /*
    Updatet ProgressBar, dabei wird der Wert vom Model an den Quizcontroller weitergegeben (immer um 1 erhöht, weil die Bar von 0-15 geht)
     */
    public void updateProgressBar(int value){
    progressBar.setValue(value);
    }
}
