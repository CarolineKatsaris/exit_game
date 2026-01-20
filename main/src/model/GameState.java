package model;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * Hält den zentralen Spielzustand (aktueller Screen, Räume, Nutzer, Difficulty)
 * und kapselt das Laden von Screen-/Room-Daten aus der Datenbank.
 * <p>
 * Zusätzlich wird die Gesamtzahl falscher Antworten über alle Räume gezählt.
 * </p>
 */
public class GameState {


    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private final List<Room> roomOverview = new ArrayList<>();
    private final List<Screen> availableScreens;
    private final DbLoader dbLoader;
    private Screen currentScreen;
    private String username;
    private EnumDifficulty difficulty;
    private int totalWrongAnswers = 0; // Gesamtzahl der falschen Antworten


    /**
     * Erzeugt den initialen Spielzustand:
     * <ul>
     *   <li>initialisiert den DB-Loader</li>
     *   <li>legt Hub und alle Räume an (zunächst nicht abgeschlossen)</li>
     *   <li>baut die Screen-Reihenfolge (Game-Flow) auf</li>
     *   <li>lädt Hintergrund, Intro und Outro für jeden Screen</li>
     * </ul>
     */
    public GameState() {

        this.dbLoader = new DbLoader("jdbc:sqlite:ExitGame.sqlite");

        Screen hubScreen = new Screen(EnumScreen.Hub);
        Room graphicRoom = new Room(EnumScreen.GraphicRoom, false);
        Room ramRoom     = new Room(EnumScreen.RAMRoom,     false);
        Room fileRoom    = new Room(EnumScreen.FileRoom,    false);
        Room netRoom     = new Room(EnumScreen.NetRoom,     false);
        Room cpuRoom     = new Room(EnumScreen.CPURoom,     false);


        // Räume zur Übersicht hinzufügen
        roomOverview.add(graphicRoom);
        roomOverview.add(ramRoom);
        roomOverview.add(fileRoom);
        roomOverview.add(netRoom);
        roomOverview.add(cpuRoom);

        // Räume in die Screens-Liste eintragen, legt Reihenfolge fest -> Hubview immer wieder zwischenschalten
        availableScreens = List.of(
                new Screen(EnumScreen.Start),
                new Screen(EnumScreen.Login),
                hubScreen,
                //new Screen(EnumScreen.Room)?,
                graphicRoom,
                hubScreen,
                ramRoom,
                hubScreen,
                fileRoom,
                hubScreen,
                netRoom,
                hubScreen,
                cpuRoom,
                new Screen(EnumScreen.End)
        );

        for (Screen screeni : availableScreens) {
            dbLoader.loadBackgroundForScreen(screeni);
            screeni.setIntroText(dbLoader.loadIntroText(screeni.getTitle()));
            screeni.setOutroText(dbLoader.loadOutroText(screeni.getTitle()));
        }

    }
    /**
     * Lädt die Quizze passend zur Schwierigkeit für jeden Raum und setzt sie im jeweiligen Room-Objekt.
     *
     * @param difficulty gewünschte Schwierigkeit
     */
    public void initQuizzesForDifficulty(EnumDifficulty difficulty) {
        // Räume haben wir ja in roomOverview
        for (Room room : roomOverview) {
            EnumScreen roomTitle = room.getTitle();
            switch (roomTitle) {
                case GraphicRoom, RAMRoom, FileRoom, NetRoom, CPURoom -> {
                    var quizzes = dbLoader.loadQuizzesForRoom(roomTitle, difficulty);
                    room.setQuizzes(quizzes);
                }
                default -> {

                }
            }
        }
    }

    /**
     * Erhöht die Gesamtzahl falscher Antworten um 1
     */
    public void incrementWrongAnswers() {
        totalWrongAnswers++;
    }

    public int getTotalWrongAnswers() {
        return totalWrongAnswers;
    }

    /**
     * Setzt die Gesamtzahl falscher Antworten zurück.
     */
    public void resetWrongAnswers() {
        totalWrongAnswers = 0;
    }

    /**
     * @return Liste der verfügbaren Screens in Spielreihenfolge
     */
    public List<Screen> getAvailableScreens() {
        return availableScreens;
    }

    List<Room> getRoomOverview() {
        return new ArrayList<>(roomOverview);
    }

    public String getUsername() {
        return username;
    }

    /**
     * Setzt den Benutzernamen und feuert ein PropertyChange-Event ("username").
     *
     * @param username neuer Benutzername
     */
    public void setUsername(String username){
        String old = this.username;
        this.username = username;
        pcs.firePropertyChange("username", old, username);
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    /**
     * Wechselt den aktuellen Screen (ohne PropertyChange-Event für "screen",
     * da dieses an anderer Stelle im Model ausgelöst wird).
     *
     * @param newScreen neuer Screen
     */
    void changeScreen(Screen newScreen){
        Screen old = this.currentScreen;
        this.currentScreen = newScreen;
        }

    /**
     * Sucht in {@link #availableScreens} nach einem Screen mit passendem Titel.
     *
     * @param title Screen-Titel
     * @return Screen-Objekt oder {@code null}, falls nicht vorhanden
     */
    public Screen getScreenByTitle(EnumScreen title) {
        for (Screen screen : availableScreens) {
            if (screen.getTitle() == title) {
                return screen;
            }
        }
        return null;
    }

    public EnumDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(EnumDifficulty difficulty) {
        this.difficulty = difficulty;
    }



}


