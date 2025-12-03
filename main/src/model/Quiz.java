package model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {

    private final List<Question> questions;
    private int currentIndex = 0;

    private boolean finished = false;


    public Quiz() {
        this.questions = new ArrayList<>();
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Question getCurrentQuestion() {

        if (currentIndex >= 0 && currentIndex < questions.size()) {
            return questions.get(currentIndex);
        }
        return null;
    }

    public void nextQuestion() {

        if (currentIndex < questions.size() - 1) {
            currentIndex++;
        }
    }

        public boolean isCompleted () {
            return currentIndex >= questions.size()-1;
        }

    // Abfragen, ob das Quiz schon komplett gespielt wurde
    public boolean isFinished() {
        return finished;
    }

    // Markiert das Quiz als komplett durchgespielt
    public void markFinished() {
        this.finished = true;
    }


}
