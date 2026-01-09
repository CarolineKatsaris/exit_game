// RoomView.java
package view;

import javax.swing.*;
import java.awt.*;

import static java.lang.Integer.valueOf;

public class RoomView extends JLayeredView {

    final JButton[] klickButtons = new JButton[3];
    private final JButton back;
    private final JProgressBar progressBar;

    public RoomView() {
        super();

        //nur Zurück Button erzeugen, die restlichen werden beim Laden des Screens per setKlickButtons erzeugt
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

    /**
     * Füge klickbare Buttons hinzu.
     * @param buttonBounds
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

    public JButton getQuiz1Button() { return klickButtons[0]; }
    public JButton getQuiz2Button() { return klickButtons[1]; }
    public JButton getQuiz3Button() { return klickButtons[2]; }

    public void setQuiz1Highlight(boolean on) { setHighlight(klickButtons[0], on); }
    public void setQuiz2Highlight(boolean on) { setHighlight(klickButtons[1], on); }
    public void setQuiz3Highlight(boolean on) { setHighlight(klickButtons[2], on); }
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
