package view;

import model.Question;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class QuizView extends JPanel {

    private JLabel questionLabel;
    private JButton[] answerButtons = new JButton[4];
    private JButton quizStopButton;

    // === Style wie TextOverlay ===
    private static final Color OVERLAY_TINT = new Color(40, 60, 40, 120);
    private static final Color BOX_BG = new Color(18, 42, 32);
    private static final Color BOX_BORDER = new Color(60, 140, 110);

    private static final Color BTN_BG = new Color(160, 180, 140);
    private static final Color BTN_FG = new Color(30, 50, 30);
    private static final Color BTN_BORDER = new Color(40, 60, 40);

    private static final Font RETRO_FONT = new Font(Font.MONOSPACED, Font.BOLD, 18);


    public QuizView() {
        // Hintergrund: wir malen selbst halbtransparent
        setOpaque(false);
        setLayout(new GridBagLayout()); // zentriert das innere Panel


        JPanel dialogPanel = new JPanel();
        styleDialogPanel(dialogPanel);
        dialogPanel.setLayout(new BorderLayout(30, 30)); // schönere Aufteilung



        // Frage
        questionLabel = new JLabel("Frage kommt aus dem Model");
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Mehrzeilige Darstellung erlauben
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setFont(RETRO_FONT.deriveFont(28f));
        dialogPanel.add(questionLabel, BorderLayout.NORTH);

        // Antworten
        JPanel answersPanel = new JPanel(new GridLayout(2, 2, 50, 50));
        answersPanel.setBackground(Color.BLACK);
        answersPanel.setOpaque(false); // damit BOX_BG vom dialogPanel durchscheint

        for (int i = 0; i < 4; i++) {
            JButton btn = new JButton("Antwort " + (i + 1));
            styleRetroButton(btn);
            btn.setActionCommand("QUIZ_ANSWER_" + i);

            // Defaults speichern
            btn.putClientProperty("defaultBg", btn.getBackground());
            btn.putClientProperty("defaultFg", btn.getForeground());
            btn.putClientProperty("defaultContentAreaFilled", btn.isContentAreaFilled());
            btn.putClientProperty("defaultBorderPainted", btn.isBorderPainted());
            btn.putClientProperty("defaultOpaque", btn.isOpaque());

            answerButtons[i] = btn;
            answersPanel.add(btn);
        }

        quizStopButton = new JButton("Abbrechen");
        styleRetroButton(quizStopButton);
        quizStopButton.setForeground(new Color(180, 60, 60));      // rot, aber nicht knallig
        quizStopButton.setBorder(BorderFactory.createLineBorder(new Color(120, 40, 40), 3));


        dialogPanel.add(answersPanel, BorderLayout.CENTER);
        dialogPanel.add(quizStopButton, BorderLayout.SOUTH);
        // DialogPanel zentriert in diesem Overlay
        add(dialogPanel);
    }


    /**
     * Methode, die den Text einer Frage und die Antwortmöglichkeiten (auf die buttons) einfügt
     * Wird vom Controller/View aufgerufen, wenn das Modell eine neue Frage liefert.
     */

    public void setQuestion(Question question) {
        if (question == null) {
            questionLabel.setText("");
            for (JButton btn : answerButtons) {
                btn.setText("");
            }
            return;
        }

        // HTML-Wrapper, damit lange Fragen umbrechen
        String htmlText = "<html><body style='width:400px; text-align:center;'>" + question.getQuestion() + "</body></html>";
        questionLabel.setText(htmlText);

        for (int i = 0; i < 4; i++) {
            String answer = question.getAnswers().get(i);
            String htmlAnswer =
                    "<html><div style='width:250px; text-align:center;'>"
                            + answer +
                            "</div></html>";
            answerButtons[i].setText(htmlAnswer);
        }
    }

    public JButton[] getAnswerButtons() {
        return answerButtons;
    }

    public JButton getQuizStopButton() {return quizStopButton;};

    /**
     *Setzt ein halbtransparentes dunkles Overlay über den gesamten Raum
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Halbtransparentes Dunkel über den gesamten Raum legen
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(OVERLAY_TINT); // 150 = Transparenz
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();

        super.paintComponent(g);
    }

    protected void restoreDefaultButton(JButton b) {
        b.setBackground((Color) b.getClientProperty("defaultBg"));
        b.setForeground((Color) b.getClientProperty("defaultFg"));
        b.setContentAreaFilled((Boolean) b.getClientProperty("defaultContentAreaFilled"));
        b.setBorderPainted((Boolean) b.getClientProperty("defaultBorderPainted"));
        b.setOpaque((Boolean) b.getClientProperty("defaultOpaque"));

        // optional, aber hilft bei "hängt optisch"
        b.repaint();
    }

    private void styleRetroButton(JButton b) {
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setContentAreaFilled(true);

        b.setBackground(BTN_BG);
        b.setForeground(BTN_FG);
        b.setBorder(BorderFactory.createLineBorder(BTN_BORDER, 3));
        b.setFont(RETRO_FONT.deriveFont(18f));
    }

    private void styleDialogPanel(JPanel p) {
        p.setOpaque(true);
        p.setBackground(BOX_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BOX_BORDER, 4),
                BorderFactory.createEmptyBorder(16, 24, 16, 24)
        ));
    }



    public void highlightIncorrectQuizAnswer(int buttonIndex) {
        if (buttonIndex < 0 || buttonIndex >= getAnswerButtons().length) {
            return; // Überprüfen, ob der Index gültig ist
        }
        JButton[] answerButtons = getAnswerButtons();
        JButton buttonToHighlight = answerButtons[buttonIndex];
        buttonToHighlight.setOpaque(true);
        buttonToHighlight.setContentAreaFilled(true);
        buttonToHighlight.setBorderPainted(false);
        buttonToHighlight.setBackground(Color.RED); // Button rot färben

        // Timer, um die Farbe nach 2 Sekunden zurückzusetzen
        Timer timer = new Timer(500, e -> restoreDefaultButton(buttonToHighlight));
        timer.setRepeats(false);
        timer.start();
    }

    public void highlightCorrectQuizAnswer(int buttonIndex) {
        if (buttonIndex < 0 || buttonIndex >= getAnswerButtons().length) {
            return; // Überprüfen, ob der Index gültig ist
        }
        JButton[] answerButtons = getAnswerButtons();
        JButton buttonToHighlight = answerButtons[buttonIndex];
        buttonToHighlight.setOpaque(true);
        buttonToHighlight.setContentAreaFilled(true);
        buttonToHighlight.setBorderPainted(false);
        buttonToHighlight.setBackground(Color.GREEN); // Button grün färben

        // Timer, um die Farbe nach 2 Sekunden zurückzusetzen
        Timer timer = new Timer(2000, e -> restoreDefaultButton(buttonToHighlight));
        timer.setRepeats(false);
        timer.start();
    }

}
