package view;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    final JTextField nameField;
    final JComboBox<String> difficultySelection;
    final JButton submitButton;

        public LoginView() {
            setLayout(new GridBagLayout()); //flexibles Layout, Komponenten können in Gitter angeordnet werden
            GridBagConstraints gbc = new GridBagConstraints();//Klasse, um Einstellungen für Positionierung der Komponenten zu definieren
            gbc.insets= new Insets(10,10, 10, 10); //Abstand um Komponenten
            gbc.anchor = GridBagConstraints.CENTER; //Ausrichtung der Komoponente in zugewiesener Zelle; Hier: Mittig

            //Label für den Namen
            JLabel nameLabel = new JLabel("Name:");
            gbc.gridx = 0;
            gbc.gridy=0;
            add(nameLabel, gbc);

            //Textfeld für Namen
            nameField = new JTextField(20);
            gbc.gridx = 1;
            add(nameField, gbc);

            // Label für die Schwierigkeitsstufe
            JLabel difficultyLabel = new JLabel("Schwierigkeitsstufe:");
            gbc.gridx = 0;
            gbc.gridy = 1;
            add(difficultyLabel, gbc);

            // Dropdown-Menü für die Schwierigkeitsstufe
            String[] difficulties = {"Unterstufe", "Mittelstufe", "Oberstufe"};
            difficultySelection = new JComboBox<>(difficulties);
            gbc.gridx = 1;
            add(difficultySelection, gbc);

            // Bestätigungsbutton
            submitButton = new JButton("Starten"); //Button für Ok
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2; //zieht sich über zwei Spalten
            gbc.anchor = GridBagConstraints.CENTER;
            add(submitButton, gbc);

            //Hier fehlt noch das Hintergrundbild
            //   // Hintergrundbild laden und Null-Prüfung //
            // java.net.URL imgUrl = getClass().getResource("/VirusBegin.png");
            // if (imgUrl == null) {
            // throw new IllegalStateException("Bild '/VirusBegin.png' wurde nicht gefunden!");
            // }

        }

}
