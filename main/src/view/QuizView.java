package view;

import model.Question;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class QuizView extends JPanel {

    private JLabel questionLabel;
    private JButton[] answerButtons = new JButton[4];

    public QuizView() {
        // Hintergrund: wir malen selbst halbtransparent
        setOpaque(false);
        setLayout(new GridBagLayout()); // zentriert das innere Panel


        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BorderLayout(10, 10));
        dialogPanel.setBackground(new Color(20, 20, 20, 230)); // dunkles Overlay für das Kästchen
        dialogPanel.setBorder(new EmptyBorder(20, 20, 20, 20));


        // Frage
        questionLabel = new JLabel("Frage kommt aus dem Model");
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Mehrzeilige Darstellung erlauben
        questionLabel.setFont(questionLabel.getFont().deriveFont(Font.BOLD, 18f));
        dialogPanel.add(questionLabel, BorderLayout.NORTH);

        // Antworten
        JPanel answersPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        for (int i = 0; i < 4; i++) {
            JButton btn = new JButton("Antwort " + (i + 1));
            btn.setActionCommand("QUIZ_ANSWER_" + i);
            answerButtons[i] = btn;
            answersPanel.add(btn);
        }

        dialogPanel.add(answersPanel, BorderLayout.CENTER);

        // DialogPanel zentriert in diesem Overlay
        add(dialogPanel);
    }

    /**
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
        String htmlText = "<html><body style='width:400px'>" + question.getQuestion() + "</body></html>";
        questionLabel.setText(htmlText);

        for (int i = 0; i < 4; i++) {
            answerButtons[i].setText(question.getAnswers().get(i));
        }
    }

    public JButton[] getAnswerButtons() {
        return answerButtons;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Halbtransparentes Dunkel über den gesamten Raum legen
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0, 150)); // 150 = Transparenz
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();

        super.paintComponent(g);
    }
}
