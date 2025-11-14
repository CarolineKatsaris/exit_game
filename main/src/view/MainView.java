package view;

import model.EnumDifficulty;
import model.EnumScreen;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {

    private CardLayout cards;
    private JPanel root;

    // Referenzen auf einzelne Screens:
    private StartView startView;
    private LoginView loginView;
    private HubView hubView;
    private RoomView roomView;

    public MainView() {
        setTitle("Exit Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);

        // Layout-Container für die Screens
        cards = new CardLayout();
        root = new JPanel(cards);

        // Screens anlegen
        startView = new StartView();
        hubView = new HubView();
        loginView = new LoginView();
        roomView = new RoomView();

        // Screens registrieren mit Namen
        root.add(startView, EnumScreen.START.getCardName());
        root.add(hubView, EnumScreen.HUB.getCardName());
        root.add(loginView, EnumScreen.LOGIN.getCardName());
        root.add(roomView, EnumScreen.ROOM.getCardName());

        // Alles ins Fenster
        add(root, BorderLayout.CENTER);
    }

    // --- Zugriff für den Controller ---

    public JButton getStartButton() {
        return startView.getStartButton();
    }

    // Login-View Werte, ToDo generische Implementierung
    public JButton getSubmitButton() { return loginView.getSubmitButton();}
    public EnumDifficulty getLoginDifficulty() { return (EnumDifficulty) loginView.difficultySelection.getSelectedItem();}
    public String getLoginUsername() {
        if(loginView.nameField.getText().equals("Tippe hier deinen Vornamen ein!")){ //Default Value filtern
            return "";
        }
        else  {
            return loginView.nameField.getText();
        }
    }

    public void showScreen(String cardName) {
        cards.show(root, cardName);
    }

    public void showScreen(EnumScreen screen) {
        cards.show(root, screen.getCardName());
    }

    public void showHub() {
        cards.show(root, "hub");
    }}

