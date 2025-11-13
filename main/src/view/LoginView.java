package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

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
        GridBagConstraints gbc = new GridBagConstraints(); //Klasse, um Einstellungen für Positionierung der Komponenten zu definieren
        gbc.insets = new Insets(10, 10, 10, 10); //Abstand um Komponenten
        gbc.anchor = GridBagConstraints.CENTER; //Ausrichtung der Komponente in zugewiesener Zelle; Hier: Mittig

        //Label für den Namen
        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        nameLabel.setForeground(Color.BLACK);
        add(nameLabel, gbc);
        nameLabel.setOpaque(true);
        nameLabel.setBackground(new Color(255, 255, 255, 150));

        //Textfeld für Namen eingeben
        nameField = new JTextField(20);
        nameField.setText("Tippe hier deinen Vornamen ein!");
        nameField.setForeground(Color.GRAY);
        gbc.gridx = 1;
        add(nameField, gbc);

        /*// FocusListener hinzufügen
        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameField.getText().equals("Vorname eingeben")) {
                    nameField.setText(""); // Platzhaltertext entfernen
                    nameField.setForeground(Color.BLACK); // Schriftfarbe ändern, wenn der Benutzer tippt
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().isEmpty()) {
                    nameField.setForeground(Color.GRAY); // Farbe zurücksetzen
                    nameField.setText("Vorname eingeben"); // Platzhaltertext zurücksetzen
                }
            }
        });*/

        //Auswahl der Schwierigkeit
        JLabel difficultyLabel = new JLabel("Schwierigkeitsstufe:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        difficultyLabel.setForeground(Color.BLACK);
        add(difficultyLabel, gbc);
        difficultyLabel.setOpaque(true);
        difficultyLabel.setBackground(new Color(255, 255, 255, 150));

        String[] difficulties = {"Unterstufe", "Mittelstufe", "Oberstufe"};
        difficultySelection = new JComboBox<>(difficulties);
        gbc.gridx = 1;
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

        // Hintergrundbild zeichnen
        if (backgroundImageLogin != null) {
            g.drawImage(backgroundImageLogin, 0, 0, getWidth(), getHeight(), this);
        }

        // Rechteck hinter den Eingabefeldern zeichnen
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(255, 255, 255, 200)); // Weiß mit etwas Transparenz

        // Berechnung der Position und Größe des Rechtecks
        int rectX = 50; // X-Position des Rechtecks
        int rectY = 200; // Y-Position des Rechtecks
        int rectWidth = getWidth() - 100; // Breite des Rechtecks
        int rectHeight = 150; // Höhe des Rechtecks

        g2d.fillRoundRect(rectX, rectY, rectWidth, rectHeight, 20, 20); // Rechteck mit abgerundeten Ecken
    }
}
