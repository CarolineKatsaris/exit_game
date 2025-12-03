package model;

import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Room extends Screen {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    //private final String roomTitle;
    private boolean open;
    private boolean completed;

    private String introText;
    private String outroText;
    private boolean introShown = false;
    private boolean outroShown = false;

    private List<Quiz> quizzes = new ArrayList<>();
    private int currentQuizIndex = 0;

    public Room(EnumScreen roomTitle, boolean open) {
        super(roomTitle); //roomTitle an Elternklasse Screen übergeben und dort als title speichern
        this.open = open;
    }

    // Methoden, um Text anzuzeigen
    public String getIntroText() {
        return introText;
    }

    public void setIntroText(String introText) {
        this.introText = introText;
    }

    public String getOutroText() {
        return outroText;
    }

    public void setOutroText(String outroText) {
        this.outroText = outroText;
    }

    public boolean isIntroShown() {
        return introShown;
    }

    public void setIntroShown(boolean introShown) {
        this.introShown = introShown;
    }

    public boolean isOutroShown() {
        return outroShown;
    }

    public void setOutroShown(boolean outroShown) {
        this.outroShown = outroShown;
    }


    boolean isCompleted() {
        //Raum ist abgeschlossen, wenn alle Quizzes abgeschlossen sind
        for (Quiz quiz : quizzes) {
            if (!quiz.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    //ersetzt den Setter, da hier der abgeschlossene Raum markiert wird
    //es gibt ja in der Regel kein Zurück von abgeschlossen
    void markAsCompleted() {
        this.completed = true;
    }

    //sollte der Raum bei einem Spielneustart doch zurückgesetzt werden müssen, dann über diese Methode
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

    //Beantwortet, welches Quiz gerade aktiv ist
    Quiz getCurrentQuiz() {
        return quizzes.get(currentQuizIndex);
    }

    void setCurrentQuizIndex(int index) {
        if (index >= 0 && index < quizzes.size()) {
            this.currentQuizIndex = index;
        }
    }

    void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = new ArrayList<>(quizzes);
    }

    //von hier bekommt man die Liste der im Raum vorhandenen Quizzes
    List<Quiz> getQuizzes() {
        return new ArrayList<>(quizzes);
    }

    public void setCompleted(boolean completed) {
        boolean old = this.completed;
        this.completed = completed;
        pcs.firePropertyChange("completed", old, completed);
}
}


