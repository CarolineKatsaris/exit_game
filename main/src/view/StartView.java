package view;

import javax.swing.*;
import java.awt.*;


public class StartView extends JPanel {

    private JButton startButton;

    public StartView() {
        setLayout(new BorderLayout());

        // Hintergrundbild laden und Null-Prüfung //
        java.net.URL imgUrl = getClass().getResource("/VirusBegin.png");
        if (imgUrl == null) {
            throw new IllegalStateException("Bild '/VirusBegin.png' wurde nicht gefunden!");
        }
        ImageIcon img = new ImageIcon(imgUrl);
        JLabel imgLabel = new JLabel(img);

        // Starttext (ohne Benutzeransprache, da vor dem Login)
        imgLabel.setLayout(new BorderLayout()); //neuer "Container" für den Text, erstmal als eher unflexibles
                                                // BorderLayout, kann man leicht abändern

        JLabel introText = new JLabel("Willkommen im digitalen Escape Room!");
        introText.setHorizontalAlignment(SwingConstants.CENTER);  // Zentrieren (oder ggf anders)
        introText.setFont(new Font("SansSerif", Font.BOLD, 20));   // Schriftart und Größe
        introText.setForeground(Color.WHITE);

        JLabel subText = new JLabel("Finde Hinweise, löse Rätsel und stoppe den Virus!");
        subText.setHorizontalAlignment(SwingConstants.CENTER); // Zentrieren
        subText.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subText.setForeground(Color.WHITE);

        JPanel textPanel = new JPanel(new GridLayout(2, 1)); //Legt den Text in zwei Zeilen und eine Spalte
        textPanel.setOpaque(false); // durchsichtig, damit das Bild sichtbar bleibt (Opaque ist erstmal nicht transparent)
        textPanel.add(introText);
        textPanel.add(subText);

        imgLabel.add(textPanel, BorderLayout.CENTER); // legt Text auf das Bild


        startButton = new JButton("Virus stoppen");

        JPanel bottom = new JPanel();
        bottom.add(startButton);

        add(imgLabel, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public JButton getStartButton() {
        return startButton;
    }
}
