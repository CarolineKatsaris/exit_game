package model;
import java.util.List;

public class Question {

    private String question;
    private final List<String> answers; // genau 4
    private final int correctIndex; // 0..3

    public Question(String questionText, List<String> answers, int correctIndex) {
        this.question = questionText;
        this.answers = answers;
        this.correctIndex = correctIndex;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }



}
