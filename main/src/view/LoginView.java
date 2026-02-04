
package view;

import model.EnumDifficulty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class LoginView extends JLayeredView {

    private final Image backgroundImageLogin;
    final JTextField nameField;
    final JComboBox<EnumDifficulty> difficultySelection;
    final JButton submitButton;

    public LoginView() {
        // Initialisierung von JLayeredView
        super();

        // Hintergrundbild laden
        backgroundImageLogin = new ImageIcon(
                getClass().getResource("/HubViewBackground.png")
        ).getImage();

        // Rechteck für Eingabefelder
        int rectX = 50;
        int rectY = 300;
        int rectWidth = 400;
        int rectHeight = 150;

        // Label für den Namen
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setOpaque(true);
        nameLabel.setBackground(new Color(255, 255, 255, 150));
        nameLabel.setBounds(rectX + 20, rectY + 10, 200, 35);
        add(nameLabel, Integer.valueOf(1));

        // Textfeld für Namen eingeben
        nameField = new JTextField(20);
        nameField.setText("Name eingeben!");
        nameField.setForeground(Color.GRAY);
        nameField.setBounds(rectX + 220, rectY + 10, 200, 35);
        nameField.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(nameField, Integer.valueOf(1));

        // FocusListener hinzufügen
        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameField.getText().equals("Name eingeben!")) {
                    nameField.setText("");
                    nameField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().isEmpty()) {
                    nameField.setForeground(Color.GRAY);
                    nameField.setText("Name eingeben!");
                }
            }
        });

        // Auswahl der Schwierigkeit
        JLabel difficultyLabel = new JLabel("Schwierigkeitsstufe:");
        difficultyLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        difficultyLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        difficultyLabel.setForeground(Color.BLACK);
        difficultyLabel.setOpaque(true);
        difficultyLabel.setBackground(new Color(255, 255, 255, 150));
        difficultyLabel.setBounds(rectX + 20, rectY + 50, 200, 35);
        add(difficultyLabel, Integer.valueOf(1));

        EnumDifficulty[] difficulties = {EnumDifficulty.Unterstufe, EnumDifficulty.Mittelstufe, EnumDifficulty.Oberstufe};
        difficultySelection = new JComboBox<>(difficulties);
        difficultySelection.setBounds(rectX + 220, rectY + 50, 200, 40);
        difficultySelection.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(difficultySelection, Integer.valueOf(1));

        // Bestätigen Button
        submitButton = new JButton("Starten");
        submitButton.setBackground(new Color(0, 120, 215));
        submitButton.setForeground(Color.BLACK);
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        submitButton.setBounds(rectX + 90, rectY + 95, 230, 40);
        add(submitButton, Integer.valueOf(1));
    }

    // Bild als Hintergrund zeichnen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Hintergrundbild zeichnen
        if (backgroundImageLogin != null) {
            g.drawImage(backgroundImageLogin, 0, 0, getWidth(), getHeight(), this);
        }

        // Rechteck hinter den Eingabefeldern zeichnen
        Graphics2D g2d = (Graphics2D) g;

        int rectX = 40;
        int rectY = 260;
        int rectWidth = 450;
        int rectHeight = 200;

        // 1. Schatten (leicht versetzt, dunkel & transparent)
        g2d.setColor(new Color(0, 0, 0, 70));
        g2d.fillRoundRect(rectX + 6, rectY + 6, rectWidth, rectHeight, 20, 20);

        // 2. Hauptfeld (hellgrün)
        g2d.setColor(new Color(210, 255, 210, 230));
        g2d.fillRoundRect(rectX, rectY, rectWidth, rectHeight, 20, 20);

        // 3. „Glow"-Rahmen: mehrere leicht transparente Ränder
        for (int i = 0; i < 3; i++) {
            float alpha = 0.5f - i * 0.15f;
            g2d.setColor(new Color(140, 220, 140, (int) (alpha * 255)));
            g2d.setStroke(new BasicStroke(2f + i));

            g2d.drawRoundRect(
                    rectX - i, rectY - i,
                    rectWidth + 2 * i, rectHeight + 2 * i,
                    20 + i * 2, 20 + i * 2
            );
        }
    }

    public JButton getSubmitButton() {
        return submitButton;
    }

    @Override
    void setButtonsEnabled(boolean enabled) {
        // LoginView hat keine Buttons die deaktiviert werden müssen
    }
}