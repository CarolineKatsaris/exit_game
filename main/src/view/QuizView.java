package view;

import model.Question;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class QuizView extends JPanel {

    private JLabel questionLabel;
    private JButton[] answerButtons = new JButton[4];
    private JButton quizStopButton;

    public QuizView() {
        // Hintergrund: wir malen selbst halbtransparent
        setOpaque(false);
        setLayout(new GridBagLayout()); // zentriert das innere Panel


        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BorderLayout(50, 50));
        dialogPanel.setBackground(new Color(20, 20, 20, 230)); // dunkles Overlay für das Kästchen
        dialogPanel.setBorder(new EmptyBorder(50, 50, 50, 50));


        // Frage
        questionLabel = new JLabel("Frage kommt aus dem Model");
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Mehrzeilige Darstellung erlauben
        questionLabel.setFont(questionLabel.getFont().deriveFont(Font.BOLD, 30f));
        dialogPanel.add(questionLabel, BorderLayout.NORTH);

        // Antworten
        JPanel answersPanel = new JPanel(new GridLayout(2, 2, 50, 50));
        answersPanel.setBackground(Color.BLACK);
        for (int i = 0; i < 4; i++) {
            JButton btn = new JButton("Antwort " + (i + 1));
            btn.setFont(btn.getFont().deriveFont(Font.BOLD, 25f));
            btn.setActionCommand("QUIZ_ANSWER_" + i);
            answerButtons[i] = btn;
            answersPanel.add(btn);
        }

        quizStopButton = new JButton("Abbrechen");
        quizStopButton.setForeground(Color.RED);
        quizStopButton.setFont(quizStopButton.getFont().deriveFont(Font.BOLD, 25f));

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
        String htmlText = "<html><body style='width:400px,text-align:center;'>" + question.getQuestion() + "</body></html>";
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
     *Setzt ein halbtransparentes schwarzes Overlay über den gesamten Raum
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Halbtransparentes Dunkel über den gesamten Raum legen
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0, 150)); // 150 = Transparenz
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();

        super.paintComponent(g);
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
        Timer timer = new Timer(500, e -> {
            buttonToHighlight.setBackground(null); // Hintergrundfarbe zurücksetzen
            buttonToHighlight.setContentAreaFilled(false);
            buttonToHighlight.setBorderPainted(true);
        });
        timer.setRepeats(false); // Timer soll nur einmal ablaufen
        timer.start(); // Timer starten
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
        buttonToHighlight.setBackground(Color.GREEN); // Button rot färben

        // Timer, um die Farbe nach 2 Sekunden zurückzusetzen
        Timer timer = new Timer(50, e -> {
            buttonToHighlight.setBackground(null); // Hintergrundfarbe zurücksetzen
            buttonToHighlight.setContentAreaFilled(false);
            buttonToHighlight.setBorderPainted(true);
        });
        timer.setRepeats(false); // Timer soll nur einmal ablaufen
        timer.start(); // Timer starten
    }

}
