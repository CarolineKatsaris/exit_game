package view;

import javax.swing.*;
import java.awt.*;

/**
 * TextOverlay ist ein einfaches Overlay, das Text in einem JLayeredPane (Room, Hub) anzeigt. Es wird für Intros und Outros verwendet.
 */
public class TextOverlay extends JPanel {

    private final JTextArea textArea; // TextArea anstelle von JLabel
    private final Timer autoCloseTimer;
    private final Timer typingTimer; // Timer für das Schreiben
    private final String fullText; // Vollständiger Text
    private final Runnable onFinished;
    private int currentCharIndex; // Aktueller Index des Buchstabens
    private final Font retroFont = new Font(Font.MONOSPACED, Font.PLAIN, 18);

    public TextOverlay(String text, Runnable onFinished) {
        this.onFinished = onFinished;
        this.fullText = text; // Speichern des gesamten Textes
        this.currentCharIndex = 0; // Startindex

        // halbtransparentes Overlay
        setOpaque(false); // false = transparent
        setBackground(new Color(40, 60, 40, 120));
        setLayout(new GridBagLayout());

        // „Box“ in der Mitte
        JPanel box = new JPanel(new GridBagLayout());
        box.setOpaque(true);
        box.setBackground(new Color(18, 42, 32));
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 140, 110), 4),
                BorderFactory.createEmptyBorder(16, 24, 16, 24)
        ));

        // JTextArea erstellen
        textArea = new JTextArea();
        textArea.setLineWrap(true); // Aktiviert Zeilenumbrüche
        textArea.setWrapStyleWord(true); // Bricht an Wortgrenzen um
        textArea.setOpaque(false); // Hintergrund transparent
        textArea.setForeground(Color.WHITE);
        textArea.setFont(retroFont.deriveFont(Font.BOLD, 20f));
        textArea.setEditable(false); // TextArea nicht bearbeitbar

        // ScrollPane für JTextArea
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 500)); // HIER NOCH: Bevorzugte Größe einstellen
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false); // Hintergrund des Viewports transparent
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);



        // GridBagConstraints für das JScrollPane
        GridBagConstraints gbcTextArea = new GridBagConstraints();
        gbcTextArea.gridx = 0; // Spalte 0
        gbcTextArea.gridy = 0; // Zeile 0
        gbcTextArea.weightx = 1.0; // Füllt den verfügbaren Platz
        gbcTextArea.fill = GridBagConstraints.BOTH; // Füllt sowohl horizontal als auch vertikal
        box.add(scrollPane, gbcTextArea); // JScrollPane hinzufügen

        // GridBagConstraints für den Button
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.gridx = 0; // Spalte 0
        gbcButton.gridy = 1; // Zeile 1
        gbcButton.weightx = 0; // Keine Gewichtung, Button hat feste Größe
        gbcButton.fill = GridBagConstraints.NONE; // Button hat keine Füllung
        JButton nextButton = new JButton("Weiter");
        nextButton.setFont(new Font(nextButton.getFont().getName(), Font.BOLD, 25));
        nextButton.setPreferredSize(new Dimension(150, 50)); // Festlegen der Button-Größe
        nextButton.setFocusPainted(false);
        nextButton.setBackground(new Color(160, 180, 140));
        nextButton.setForeground(new Color(30, 50, 30));
        nextButton.setBorder(BorderFactory.createLineBorder(new Color(40, 60, 40), 3));
        nextButton.setFont(retroFont.deriveFont(Font.BOLD, 18f));

        nextButton.addActionListener(e -> close());
        box.add(nextButton, gbcButton);

        add(box, new GridBagConstraints());

        autoCloseTimer = new Timer(120_000, e -> close()); //Fenster wird nach 2 Minuten ausgeblendet
        autoCloseTimer.setRepeats(false);

        // Timer für das Schreiben des Textes
        typingTimer = new Timer(25, e -> typeText()); //Buchstaben erscheinen mit einer Delay von 25 (größerer Wert --> langsamer Text)
    }

    /**
     * Text mit einem Timer schreiben.
     */
    private void typeText() {
        if (currentCharIndex < fullText.length()) {
            String partial = fullText.substring(0, currentCharIndex + 1);

            // Falls du \n als Zeilenumbruch benutzt:
            partial = partial.replace("\n", "\n");

            textArea.setText(partial); // TextArea aktualisieren

            currentCharIndex++;
        } else {
            typingTimer.stop();
        }
    }

    /**
     * Overlay auf einem JLayeredPane anzeigen
     *
     * @param parent
     */
    public void showOn(JLayeredPane parent) {
        setBounds(0, 0, parent.getWidth(), parent.getHeight());
        parent.add(this, JLayeredPane.POPUP_LAYER);
        parent.revalidate();
        parent.repaint();
        autoCloseTimer.start();
        typingTimer.start();
    }

    /**
     * Overlay ausblenden.
     */
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
