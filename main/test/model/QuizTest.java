package model;

import static org.junit.jupiter.api.Assertions.*;

class QuizTest {

    @org.junit.jupiter.api.Test
    void getCurrentQuestion() {
        Quiz quiz = new Quiz();
        Question q1 = new Question(
                "Testfrage",
                java.util.List.of("A", "B", "C", "D"),
                0
        );
        quiz.addQuestion(q1);

        Question result = quiz.getCurrentQuestion();

        System.out.println("Gefundene Frage im Test: " + result.getQuestion());

        assertEquals(q1, result);
    }
}