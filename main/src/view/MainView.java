package view;

import model.*;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {

    private CardLayout cards;
    private JPanel root;

    // Referenzen auf einzelne Screens:
    private StartView startView;
    private LoginView loginView;
    private HubView hubView;
    private RoomView graphicsView;
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
        graphicsView = new RoomView("/GraphicsCardRoomView_elements.png", new Rectangle[] {new Rectangle(400, 400, 120, 290), new Rectangle(1000, 330, 240, 180), new Rectangle(1130, 640, 260, 160) });
        quizView = new QuizView();
        // GlassPane setzen
        setGlassPane(quizView);
        quizView.setVisible(false);


        // Screens registrieren mit Namen
        root.add(startView, EnumScreen.Start.toString());
        root.add(hubView, EnumScreen.Hub.toString());
        root.add(loginView, EnumScreen.Login.toString());
        root.add(graphicsView, EnumScreen.GraphicRoom.toString());

        // Alles ins Fenster
        add(root, BorderLayout.CENTER);
    }

    /**
     * Zeigt den angegebenen Screen an (stateless). Dazu muss eine Card in root existieren, die mit dem Title von screen übereinstimmt.
     */
    public void showScreen(Screen screen) {
        cards.show(root, screen.getTitle().toString());

        // Fehlerbehandlung
        if (screen.isError()) {
       // loginView.errorLabel.setVisible(true);
            JOptionPane.showMessageDialog(root, screen.getErrorMessage(), "Fehler", JOptionPane.ERROR_MESSAGE); //ToDo bessere Fehleranzeige als ein Popup

        }
    }


    //
// ─────────────────────────────────────────────────────────────────────────────
//   START-VIEW BUTTONS
// ─────────────────────────────────────────────────────────────────────────────
//
    public JButton getStartButton() {
        return startView.getStartButton();
    }

//
// ─────────────────────────────────────────────────────────────────────────────
//   LOGIN-VIEW BUTTONS UND EINGABEN
// ─────────────────────────────────────────────────────────────────────────────
//

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
    //
    // ─────────────────────────────────────────────────────────────────────────────
    //   HUB-VIEW
    // ─────────────────────────────────────────────────────────────────────────────
    //
    //  Dieser Button liegt als „unsichtbarer“ Hotspot über der Grafikkarten-Grafik
    //  im Hub-Bild. Der Controller nutzt ihn, um in den Graphics-Room zu wechseln.
    //
    public HubView getHubView() {
        return hubView;
    }

    //
    // ─────────────────────────────────────────────────────────────────────────────
    //   ROOM-VIEW + BUTTONS
    // ─────────────────────────────────────────────────────────────────────────────
    //
    //  Drei unsichtbare Hotspots in der RoomView:
    //   • quiz1Button  → GPU/VRAM-Panel
    //   • quiz2Button  → rechter Störbildschirm
    //   • quiz3Button  → Framebuffer-Konsole
    //  Zusätzlich ein sichtbarer „Zurück zum Hub“-Button.
    //
    public RoomView getRoomView() {
        return graphicsView;
    }

    public JButton getBackButton() { return graphicsView.getBackButton(); };

    public void showQuiz(Question q) {
        quizView.setQuestion(q);
        quizView.setVisible(true);
    }

    public void hideQuiz() {
        quizView.setVisible(false);
    }

    //Färbt Button rot für eine Sekunde
    public void highlightIncorrectAnswer(JButton button) {
        button.setBackground(Color.RED);

        //Farbe nach einer Sekunde zurücksetzen
        Timer timer = new Timer(1000, e -> button.setBackground(null));
        timer.setRepeats(false);
        timer.start();
    }

    public JButton[] getQuizAnswerButtons() {
        return quizView.getAnswerButtons();
    }
}

