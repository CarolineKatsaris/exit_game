package view;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public class MainView extends JFrame {

    private CardLayout cards;
    private JPanel root;
    private static final Dimension GAME_SIZE = new Dimension(1536, 1024);




    // Referenzen auf einzelne Screens:
    private StartView startView;
    private LoginView loginView;
    private VirusTrapView virusTrapView ;
    private HubView hubView;
    private Map<EnumScreen, RoomView> roomViews = new EnumMap<>(EnumScreen.class);
    private RoomView graphicsView;
    private RoomView ramView;
    private RoomView fileView;
    private RoomView networkView;
    private RoomView cpuView;
    private QuizView quizView;

    private int globalProgress = 0;


    /**
     * Gibt den aktuell sichtbaren RoomView zurück.
     * @return der sichtbare RoomView oder {@code null}, falls keiner aktiv ist
     */

    private RoomView getActiveRoomView() {
        for (RoomView rv : roomViews.values()) {
            if (rv.isVisible()) return rv;
        }
        return null;
    }

    public VirusTrapView getVirusTrapView() {
        return virusTrapView;
    }


    public MainView() {
        setTitle("Exit Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Frame darf später maximiert werden; die Game-Fläche bleibt fix 1536x1024
        setMinimumSize(new Dimension(900, 700)); // optional, damit es nicht zu klein wird


        // Layout-Container für die Screens
        cards = new CardLayout();
        root = new JPanel(cards);
        root.setPreferredSize(GAME_SIZE);
        root.setSize(GAME_SIZE); // wichtig für absolute layouts / bounds-basierte Views


        // Screens anlegen
        startView = new StartView();
        hubView = new HubView();
        virusTrapView = new VirusTrapView();
        loginView = new LoginView();
        graphicsView = new RoomView();
        ramView = new RoomView();
        fileView = new RoomView();
        networkView = new RoomView();
        cpuView = new RoomView();
        quizView = new QuizView();
        // GlassPane setzen
        setGlassPane(quizView);
        quizView.setVisible(false);


        // Screens registrieren mit Namen
        root.add(startView, EnumScreen.Start.toString());
        root.add(virusTrapView, EnumScreen.VirusTrap.toString());
        root.add(hubView, EnumScreen.Hub.toString());
        root.add(loginView, EnumScreen.Login.toString());
        root.add(graphicsView, EnumScreen.GraphicRoom.toString());
        root.add(ramView, EnumScreen.RAMRoom.toString());
        root.add(fileView, EnumScreen.FileRoom.toString());
        root.add(networkView, EnumScreen.NetRoom.toString());
        root.add(cpuView, EnumScreen.CPURoom.toString());




        // RoomViews in Map registrieren
        roomViews.put(EnumScreen.GraphicRoom, graphicsView);
        roomViews.put(EnumScreen.RAMRoom, ramView);
        roomViews.put(EnumScreen.FileRoom, fileView);
        roomViews.put(EnumScreen.NetRoom,networkView);
        roomViews.put(EnumScreen.CPURoom,cpuView);

        // Alles ins Fenster
        add(root, BorderLayout.CENTER);
        LetterboxPanel letterbox = new LetterboxPanel(root, GAME_SIZE);
        setContentPane(letterbox);

        pack();
        setLocationRelativeTo(null);

        // Maximiert starten:
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Optional: echtes Fullscreen (ohne Taskbar) wäre extra, aber MAXIMIZED reicht meist.
        setVisible(true);



    }

    /**
     * Zeigt den angegebenen Screen an (stateless). Dazu muss eine Card in root existieren, die mit dem Title von screen übereinstimmt.
     */
    public void showScreen(Screen screen) {
        RoomView view;

        if(screen instanceof Room){ //Wenn Room, dann setze Hintergrundimage
           view = roomViews.get(screen.getTitle());
           view.setBackground(screen.getBackgroundImagePath());
           view.setKlickButtons(screen.getQuizBtnsBounds());
        }

        if(screen.getTitle() == EnumScreen.Hub){ //Wenn Hub, dann setze Hintergrundimage
            hubView.setBackground(screen.getBackgroundImagePath());
        }

        cards.show(root, screen.getTitle().toString());

        // Fehlerbehandlung
        if (screen.isError()) {
            String msg = screen.getErrorMessage();

            if (screen.getTitle() == EnumScreen.Hub) {
                hubView.showErrorBanner(msg);
            } else if (screen instanceof Room) {
                getRoomView((Room) screen).showErrorBanner(msg);
            }

            screen.clearErrorMessage();
        }


        if (screen.isShowIntro()) {
            if (screen.getTitle() == EnumScreen.Hub) {
                hubView.showOverlay(screen.getIntroText());
                screen.setIntroShown(true); //ToDo View sollte per Event Modell benachrichtigen, wenn Intro vorbei ist, so dass die dann isIntroShown aufrufen kann und das aktualisierte Screen Objekt per property change erneut an die View senden kann
            } else if (screen instanceof Room) { //generisch für alle Rooms
                getRoomView((Room) screen).showOverlay(screen.getIntroText());
                screen.setIntroShown(true);
            }
        }

        if (screen.isShowOutro()) {
            if (screen.getTitle() == EnumScreen.Hub) {
                hubView.showOverlay(screen.getIntroText());
                screen.setOutroShown(true); //ToDo View sollte per Event Modell benachrichtigen, wenn Intro vorbei ist, so dass die dann isIntroShown aufrufen kann und das aktualisierte Screen Objekt per property change erneut an die View senden kann
            } else if (screen instanceof Room) { //generisch für alle Rooms
                getRoomView((Room) screen).showOverlay(screen.getOutroText());
                screen.setOutroShown(true);
            }
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
    /**
     * Blendet das Quiz-Overlay aus und zeigt den Zurück-Button
     * im aktuell aktiven Raum wieder an.
     */

    public void hideQuiz() {
        quizView.setVisible(false);
        RoomView rv = getActiveRoomView();
        if (rv != null) rv.getBackButton().setVisible(true);
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


    // Fortschritt über alle Räume hinweg
    public void updateProgress(int value) {
        for (RoomView rv : roomViews.values()) {
            rv.updateProgressBar(value);
        }
    }

}

