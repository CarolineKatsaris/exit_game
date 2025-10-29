package view;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {

    private final Image backgroundImageLogin;

    final JTextField nameField;
    final JComboBox<String> difficultySelection;
    final JButton submitButton;

    public LoginView() {
        // Hintergrundbild laden (Pfad anpassen!)
        backgroundImageLogin = new ImageIcon(
                getClass().getResource("/HubViewBackground.png")
        ).getImage();

        setLayout(new GridBagLayout()); //flexibles Layout, Komponenten können in Gitter angeordnet werden
        GridBagConstraints gbc = new GridBagConstraints();//Klasse, um Einstellungen für Positionierung der Komponenten zu definieren
        gbc.insets= new Insets(10,10, 10, 10); //Abstand um Komponenten
        gbc.anchor = GridBagConstraints.CENTER; //Ausrichtung der Komoponente in zugewiesener Zelle; Hier: Mittig

        //Label für den Namen
        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameLabel, gbc);

        //Textfeld für Namen eingeben
        nameField = new JTextField(20);
        gbc.gridx = 1;
        add(nameField, gbc);

        //Auswahl der Schwierigkeit
        JLabel difficultyLabel = new JLabel("Schwierigkeitsstufe:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(difficultyLabel, gbc);

        String[] difficulties = {"Unterstufe", "Mittelstufe", "Oberstufe"};
        difficultySelection = new JComboBox<>(difficulties);
        gbc.gridx = 1;

        Font font = new Font("SansSerif", Font.BOLD, 16);
        nameLabel.setFont(font);
        difficultyLabel.setFont(font);

        add(difficultySelection, gbc);

        //Bestätigen Button
        submitButton = new JButton("Starten");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        submitButton.setBackground(new Color(0, 120, 215));
        submitButton.setForeground(Color.BLACK);
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        add(submitButton, gbc);
    }

    //Bild als Hintergrund zeichnen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImageLogin != null) {
            g.drawImage(backgroundImageLogin, 0, 0, getWidth(), getHeight(), this);
        }
    }

}
