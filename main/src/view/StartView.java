package view;

import javax.swing.*;
import java.awt.*;

public class StartView extends JPanel {

    private JButton startButton;

    public StartView() {
        // Hintergrund fallback (falls Bild nicht geladen)
        setBackground(Color.black);
        //Font
        final Font retroFont = new Font(Font.MONOSPACED, Font.PLAIN, 24);


        // 1) Bild laden und prüfen
        java.net.URL imgUrl = getClass().getResource("/StartViewBackground.png");
        if (imgUrl == null) {
            throw new IllegalStateException("Bild '/StartViewBackground.png' wurde nicht gefunden!");
        }

        ImageIcon bgIcon = new ImageIcon(imgUrl);
        JLabel background = new JLabel(bgIcon);

        // *** WICHTIG ***
        // Wir verwenden jetzt BorderLayout auf dem Hintergrundlabel,
        // damit wir Text oben mittig und Button unten einfügen können.
        background.setLayout(new BorderLayout());
        background.setPreferredSize(new Dimension(
                bgIcon.getIconWidth(),
                bgIcon.getIconHeight()
        ));

        // 2) Deine Text-Elemente
        JLabel introText = new JLabel("Ein Virus verbreitet sich in deiner Schule und diesmal ist es nicht Corona…");
        introText.setHorizontalAlignment(SwingConstants.CENTER);
        introText.setFont(retroFont);
        introText.setForeground(new Color(220, 240, 230));

        JLabel subText = new JLabel("Kannst du helfen es zu stoppen?");
        subText.setHorizontalAlignment(SwingConstants.CENTER);
        subText.setFont(retroFont);
        subText.setForeground(new Color(220, 240, 230));

        // 3) Textpanel wie von dir gebaut
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false); // transparent lassen, damit das Bild durchscheint
        textPanel.add(introText);
        textPanel.add(subText);

        // 4) Button bauen
        startButton = new JButton("Virus stoppen");

        // gleicher Font, aber größer
        startButton.setFont(retroFont.deriveFont(18f));

        // Farben & Verhalten
        startButton.setPreferredSize(new Dimension(260, 60));
        startButton.setFocusPainted(false);
        startButton.setOpaque(true);
        startButton.setContentAreaFilled(true);

        startButton.setBackground(new Color(18, 42, 32));      // Waldgrün
        startButton.setForeground(new Color(200, 60, 60));    // ROTER Text
        startButton.setBorder(
                BorderFactory.createLineBorder(new Color(180, 40, 40), 3) // rote Umrandung
        );


        // 5) Button-Panel für unten
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);

        // Abstand nach unten erzeugt visuell „nach oben schieben“
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));

        bottomPanel.add(startButton);


        // 6) Jetzt Text & Button ins Hintergrundlabel einfügen
        // BorderLayout.CENTER   -> dein Text (mittig)
        // BorderLayout.SOUTH    -> Button (unten)
        background.add(textPanel, BorderLayout.CENTER);
        background.add(bottomPanel, BorderLayout.SOUTH);

        // 7) Und das alles in dieses StartView einsetzen
        setLayout(new BorderLayout());
        add(background, BorderLayout.CENTER);
    }

    public JButton getStartButton() {
        return startButton;
    }
}
