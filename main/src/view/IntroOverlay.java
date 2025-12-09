package view;

import javax.swing.*;
import java.awt.*;

public class IntroOverlay extends JPanel {

    private final JLabel textLabel;
    private final JButton nextButton;
    private final Timer autoCloseTimer;
    private final Timer typingTimer; // Timer für das Schreiben
    private String fullText; // Vollständiger Text
    private int currentCharIndex;//aktueller Index des Buchstabens
    private Runnable onFinished;

    public IntroOverlay(String text, Runnable onFinished) {
        this.onFinished = onFinished;
        this.fullText = text; //Speichern des gesamten Textes
        this.currentCharIndex = 0; //Startindex

        // halbtransparentes Overlay
        setOpaque(false); //false = transparent
        setBackground(new Color(0, 0, 0, 180));
        setLayout(new GridBagLayout());

        // „Box“ in der Mitte
        JPanel box = new JPanel();
        box.setOpaque(true);
        box.setBackground(Color.BLACK);
        box.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        //box.setPreferredSize(new Dimension(850, 300));
        box.setLayout(new BorderLayout(10, 10));

        textLabel = new JLabel(
                "<html><div style='text-align:left;'>" + text + "</div></html>"
        );
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font(textLabel.getFont().getName(), Font.BOLD, 25));

        nextButton = new JButton("Weiter");
        nextButton.setFont(new Font(nextButton.getFont().getName(), Font.BOLD, 25));
        nextButton.addActionListener(e -> close());

        box.add(textLabel, BorderLayout.CENTER);
        box.add(nextButton, BorderLayout.SOUTH);

        add(box, new GridBagConstraints());

        autoCloseTimer = new Timer(60_000, e -> close());
        autoCloseTimer.setRepeats(false);

        //Timer für das SChreiben des Textes
        typingTimer = new Timer (100, e -> typeText());
    }

    private void typeText() {
        if (currentCharIndex < fullText.length()) {
            String partial = fullText.substring(0, currentCharIndex + 1);

            // Falls du \n als Zeilenumbruch benutzt:
            partial = partial.replace("\n", "<br>");

            String html = "<html><div style='text-align:center;'>" + partial + "</div></html>";
            textLabel.setText(html);

            currentCharIndex++;
        } else  {
            typingTimer.stop();
        }
    }

    // Overlay auf einem JLayeredPane anzeigen
    public void showOn(JLayeredPane parent) {
        setBounds(0, 0, parent.getWidth(), parent.getHeight());
        parent.add(this, JLayeredPane.POPUP_LAYER);
        parent.revalidate();
        parent.repaint();
        autoCloseTimer.start();
        typingTimer.start();
    }

    private void close() {
        autoCloseTimer.stop();
        Container parent = getParent();
        if (parent != null) {
            parent.remove(this);
            parent.revalidate();
            parent.repaint();
        }
        if (onFinished != null) {
            onFinished.run();
        }
    }
}
