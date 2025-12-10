package view;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public class MainView extends JFrame {

    private CardLayout cards;
    private JPanel root;


    // Referenzen auf einzelne Screens:
    private StartView startView;
    private LoginView loginView;
    private HubView hubView;
    private Map<EnumScreen, RoomView> roomViews = new EnumMap<>(EnumScreen.class);
    private RoomView graphicsView;
    private RoomView ramView;
    // später andere Räume: private Roomview filesView;
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
        ramView = new RoomView("/RAM_DataLeak.png",new Rectangle[] {
                new Rectangle(545, 128, 420, 80), // TODO: richtige Werte
                new Rectangle(545, 738, 200, 180), // TODO
                new Rectangle(1041, 229, 300, 130) // TODO
        });
        // später andere Räume
        quizView = new QuizView();
        // GlassPane setzen
        setGlassPane(quizView);
        quizView.setVisible(false);


        // Screens registrieren mit Namen
        root.add(startView, EnumScreen.Start.toString());
        root.add(hubView, EnumScreen.Hub.toString());
        root.add(loginView, EnumScreen.Login.toString());
        root.add(graphicsView, EnumScreen.GraphicRoom.toString());
        root.add(ramView, EnumScreen.RAMRoom.toString());
// später: root.add(fileView, EnumScreen.FileRoom.toString());

        // RoomViews in Map registrieren
        roomViews.put(EnumScreen.GraphicRoom, graphicsView);
        roomViews.put(EnumScreen.RAMRoom, ramView);

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
       //loginView.errorLabel.setVisible(true);
            JOptionPane.showMessageDialog(root, screen.getErrorMessage(), "Fehler", JOptionPane.ERROR_MESSAGE); //ToDo bessere Fehleranzeige als ein Popup

        }


        if (screen.isShowIntro()) {
            // Bestimme den aktuellen Screen-Typ und rufe showIntro() auf
            if (screen.getTitle() == EnumScreen.Hub) {
                hubView.showIntro(screen.getIntroText());
                screen.setIntroShown(true); //ToDo View sollte per Event Modell benachrichtigen, wenn Intro vorbei ist, so dass die dann isIntroShown aufrufen kann und das aktualisierte Screen Objekt per property change erneut an die View senden kann
            } else if (screen.getTitle() == EnumScreen.GraphicRoom) {
                graphicsView.showIntro(screen.getIntroText());
                screen.setIntroShown(true);
            }
            // Für zukünftige Räume (RAMRoom, FileRoom, etc.) müssen entsprechende Views hinzugefügt werden oder in Map Title -> xViewObjekt generalisiseren, so dass man über den Title eine Referenz auf das spezifische Room Objekt bekommt
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
        if (loginView.nameField.getText().equals("Name eingeben!")) { //Default Value filtern
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
    //Methode: direkt mit Room aus dem Model
    public RoomView getRoomView(Room room) {
        return roomViews.get(room.getTitle());
    }
   /** kann man auch direkt in den Controller machen
    public JButton getBackButton(Room room) {
        return getRoomView(room).getBackButton();
    } */

    public JButton getBackButton() { return graphicsView.getBackButton(); };
    public JButton getStopButton(){return quizView.getQuizStopButton();}

    // ─────────────────────────────────────────────────────────────────────────────
    //   QUIZ-VIEW + BUTTONS
    // ─────────────────────────────────────────────────────────────────────────────
   public void showQuiz(Question q) {
       quizView.setQuestion(q);
       quizView.setVisible(true);
       // Hide back button in current room view
       for (RoomView view : roomViews.values()) {
           if (view.isVisible()) {
               view.getBackButton().setVisible(false);
               break;
           }
       }
   }

    public void hideQuiz() {
        quizView.setVisible(false);
        getBackButton().setVisible(true);
    }


    /**
     * Färbt die Antworten rot für 1/2 Sekunde
     * Button Index wird vom Controller übergeben
     * @param buttonIndex
     */
    public void highlightIncorrectAnswer(int buttonIndex) {
       quizView.highlightIncorrectQuizAnswer(buttonIndex);
    }

    /**
     * Färbt die Antworten grün für 1/4 Sekunde
     * Button Index wird vom Controller übergeben
     * @param buttonIndex
     */
    public void highlightCorrectAnswer(int buttonIndex) {
        quizView.highlightCorrectQuizAnswer(buttonIndex);
    }


    public JButton[] getQuizAnswerButtons() {
        return quizView.getAnswerButtons();
    }
    public JButton getQuizStopButton(){
        return quizView.getQuizStopButton();
    }
}

