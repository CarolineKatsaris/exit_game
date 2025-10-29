package model;

public class Quiz {

    private String quizTitle;

    public String getRoomTitle() {
        return quizTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.quizTitle = roomTitle;
    }
    //Erklärt das jeweilige Quiz
    private String quizInfo;

    public String getQuizInfo() {
        return quizInfo;
    }

    public void setQuizInfo(String quizInfo) {
        this.quizInfo = quizInfo;
    }
    //Liste der Fragen

    //hier fehlt die komplette Logik dahinter!!!
    //stellt fest, ob Rätsel gelöst wurde
    private boolean quizSolved;

    public boolean isQuizSolved() {
        //Hier fehlt noch die Logik des gelösten Quiz
        return quizSolved;
    }

    public void setQuizSolved(boolean quizSolved) {
        this.quizSolved = quizSolved;
    }

    public boolean isCompleted() {
        return false;
    }
}
