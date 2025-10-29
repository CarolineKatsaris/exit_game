package model;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private final String roomTitle;
    private boolean open;
    private boolean completed;
    private List<Quiz> quizzes = new ArrayList<>();
    private int currentQuizIndex = 0;

    public Room(String roomTitle, boolean open) {
        this.roomTitle = roomTitle;
        this.open = open;
    }

    boolean isCompleted() {
        //Raum ist abgeschlossen, wenn alle Quizzes abgeschlossen sind
        for (Quiz quiz:quizzes){
            if(!quiz.isCompleted()){
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

    public String getRoomTitle() {
        return roomTitle;
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
    List<Quiz>getQuizzes(){
        return new ArrayList<>(quizzes);
    }
}

