package model;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class Room extends Screen {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    //private final String roomTitle;
    private boolean open;
    private boolean completed;

    private List<Quiz> quizzes = new ArrayList<>();
    private int currentQuizIndex = 0;

    // bis zu welchem Quiz-Index ist freigeschaltet?
    private int highestUnlockedQuizIndex = 0; // 0 = nur erstes Quiz erlaubt


    public Room(EnumScreen roomTitle, boolean open) {
        super(roomTitle); //roomTitle an Elternklasse Screen übergeben und dort als title speichern
        this.open = open;
    }

    public int getHighestUnlockedQuizIndex() {
        return highestUnlockedQuizIndex;
    }

    // nächstes Quiz freischalten (falls vorhanden)
    public void unlockNextQuiz() {
        if (highestUnlockedQuizIndex < quizzes.size() - 1) {
            highestUnlockedQuizIndex++;
        }
    }


    /**
     * Raum ist abgeschlossen, wenn alle Quizzes abgeschlossen sind
     * @return
     */
    boolean isCompleted() {
        for (Quiz quiz : quizzes) {
            if (!quiz.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }


    /**
     * Sollte der Raum bei einem Spielneustart doch zurückgesetzt werden müssen, dann über diese Methode.
     */
    void resetRoom() {
        this.open = false;
        this.completed = false;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * Beantwortet, welches Quiz gerade aktiv ist
     * @return
     */
    Quiz getCurrentQuiz() {
        return quizzes.get(currentQuizIndex);
    }

    void setCurrentQuizIndex(int index) {
        if (index >= 0 && index < quizzes.size()) {
            this.currentQuizIndex = index;
        }
    }

    //von hier bekommt man die Liste der im Raum vorhandenen Quizzes
    List<Quiz> getQuizzes() {
        return new ArrayList<>(quizzes);
    }

    void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = new ArrayList<>(quizzes);
    }
}


