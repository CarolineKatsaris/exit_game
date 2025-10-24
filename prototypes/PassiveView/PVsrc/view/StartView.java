package view;

import javax.swing.*;
import java.awt.*;

public class StartView extends JPanel {

    private JButton startButton;

    public StartView() {
        setLayout(new BorderLayout());

        // Hintergrundbild laden
        ImageIcon img = new ImageIcon(getClass().getResource("/VirusBegin.png"));
        JLabel imgLabel = new JLabel(img);

        startButton = new JButton("Virus stoppen");

        JPanel buttom = new JPanel();
        buttom.add(startButton);

        add(imgLabel, BorderLayout.CENTER);
        add(buttom, BorderLayout.SOUTH);
    }

    public JButton getStartButton() {
        return startButton;
    }
}
