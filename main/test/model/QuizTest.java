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
        Question q2 = new Question(
                "Testfrage2",
                java.util.List.of("A", "B", "C", "D"),
                0
        );
        quiz.addQuestion(q1);
        quiz.addQuestion(q2);

        Question result = quiz.getCurrentQuestion();

        System.out.println("Gefundene Frage im Test: " + result.getQuestion());

        assertEquals(q1, result);
    }
    @org.junit.jupiter.api.Test
    void nextQuestionShouldAdvance() {

        Quiz quiz = new Quiz();
        Question q1 = new Question("Frage 1", java.util.List.of("A","B","C","D"), 0);
        Question q2 = new Question("Frage 2", java.util.List.of("A","B","C","D"), 1);

        quiz.addQuestion(q1);
        quiz.addQuestion(q2);
        System.out.println("Start-Index: Erwartet Frage 1, Tatsächlich = " + quiz.getCurrentQuestion().getQuestion());

        // Anfang: sollte Frage 1 sein
        assertEquals(q1, quiz.getCurrentQuestion());

        // Einen Schritt weiter
        quiz.nextQuestion();
        System.out.println("Nach nextQuestion(): Erwartet Frage 2, Tatsächlich = "
                + quiz.getCurrentQuestion().getQuestion());
        assertEquals(q2, quiz.getCurrentQuestion());

        // Noch ein next -> sollte bei Frage 2 bleiben (Ende erreicht)
        quiz.nextQuestion();
        System.out.println("Noch ein next: Erwartet weiterhin Frage 2, Tatsächlich = "
                + quiz.getCurrentQuestion().getQuestion());
        assertEquals(q2, quiz.getCurrentQuestion());
    }
}
