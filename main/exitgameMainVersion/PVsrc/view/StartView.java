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
