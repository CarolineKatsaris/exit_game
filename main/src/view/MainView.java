package view;

import model.EnumDifficulty;
import model.EnumScreen;
import model.Question;

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
    private QuizView quizView;

    public MainView() {
        setTitle("Exit Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1536, 1024);

        // Layout-Container für die Screens
        cards = new CardLayout();
        root = new JPanel(cards);

        // Screens anlegen
        startView = new StartView();
        hubView = new HubView();
        loginView = new LoginView();
        roomView = new RoomView();
        quizView = new QuizView();
        // GlassPane setzen
        setGlassPane(quizView);
        quizView.setVisible(false);


        // Screens registrieren mit Namen
        root.add(startView, EnumScreen.Start.toString());
        // später: root.add(hubView, EnumScreen.Hub.toString());
        root.add(loginView, EnumScreen.Login.toString());
        root.add(roomView, EnumScreen.Room.toString());

        // Alles ins Fenster
        add(root, BorderLayout.CENTER);
    }

    // --- Zugriff für den Controller ---

    public JButton getStartButton() {
        return startView.getStartButton();
    }

    // Login-View Werte, ToDo generische Implementierung
    public JButton getSubmitButton() {
        return loginView.getSubmitButton();
    }

    public EnumDifficulty getLoginDifficulty() {
        return (EnumDifficulty) loginView.difficultySelection.getSelectedItem();
    }

    public String getLoginUsername() {
        if (loginView.nameField.getText().equals("Tippe hier deinen Vornamen ein!")) { //Default Value filtern
            return "";
        } else {
            return loginView.nameField.getText();
        }
    }

    public RoomView getRoomView() {
        return roomView;
    }

    public void showScreen(String cardName) {
        cards.show(root, cardName);
    } // String cardName -> Screen s (Enum), cardname -> s.name

    public void showScreen(EnumScreen screen) {
        cards.show(root, screen.toString());
    }

    /*public void showHub() {
        cards.show(root, "hub");
    }*/
    public void showQuiz(Question q) {
        quizView.setQuestion(q);
        quizView.setVisible(true);
    }

    public void hideQuiz() {
        quizView.setVisible(false);
    }

    public JButton[] getQuizAnswerButtons() {
        return quizView.getAnswerButtons();
    }
}

