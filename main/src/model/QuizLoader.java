package model;

import java.sql.*;
import java.util.List;


public class QuizLoader {

    private final String dbUrl;

    public QuizLoader(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    // Lädt alle Quizze für einen Raum aus der DB
    public List<Quiz> loadQuizzesForRoom(EnumScreen roomScreen) {

        // quizNr als Schlüssel, Maps sortiert jede Zeile dem richtigen Quiz zu
        // geht mit einer HashMap besser als mit List
        java.util.Map<Integer, Quiz> quizByNumber = new java.util.LinkedHashMap<>();

        //die drei Anführungszeichen sind ein Java-Feature für Textblock, alles dazwischen ist T
        // Text so wie der da steht
        // WHERE q.room = ? -> nur Zeilen, bei denen room dem Wert entspricht, der später eingesetzt wird
        String sql = """ 
                SELECT q.id              AS question_id,
                       q.quiz_nr         AS quiz_nr,
                       q.text            AS question_text,
                       a.answer_index    AS answer_index,
                       a.text            AS answer_text,
                       a.is_correct      AS is_correct
                FROM question q
                JOIN answer a ON a.question_id = q.id
                WHERE q.room = ?
                ORDER BY q.quiz_nr, q.id, a.answer_index
                """;

        // Verbindung zur Datenbank öffnen
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // hier wird der Raum festgelegt
            ps.setString(1, roomScreen.name());

            // Ergebnis wird zurückgegeben
            try (ResultSet rs = ps.executeQuery()) {

                // alles Startwerte --> hier wird erst noch alles mit null befüllt
                Integer currentQuestionId = null; //aktuelle Frage
                String currentQuestionText = null; // Text der aktuellen Frage
                int currentQuizNr = 0; // aktuelles Quiz
                java.util.List<String> answers = null; //Liste mit Antworten der aktuellen Frage
                int correctIndex = -1; //an welcher Position befindet sich die richtige Antwort
                // zunächst noch keine richtige gefunden

                // solange es Zeilen für das Resulat gibt: durchlaufen und ID zu der Frage holen
                while (rs.next()) {
                    int questionId = rs.getInt("question_id");

                    // ist es eine neue Frage?
                    if (currentQuestionId == null || questionId != currentQuestionId) {
                        // Frage wird gebaut
                        if (currentQuestionId != null) {
                            Question qObj = new Question(currentQuestionText, answers, correctIndex);
                            Quiz quiz = quizByNumber.computeIfAbsent(currentQuizNr, n -> new Quiz());
                            quiz.addQuestion(qObj);
                        }

                        // Frage erhält die Variablen
                        currentQuestionId = questionId;
                        currentQuestionText = rs.getString("question_text");
                        currentQuizNr = rs.getInt("quiz_nr");
                        answers = new java.util.ArrayList<>();
                        correctIndex = -1;
                    }

                    // Daten aus Ergebniszeile holen (Index, Antworttext, richtig/falsch)
                    int answerIndex = rs.getInt("answer_index");
                    String answerText = rs.getString("answer_text");
                    boolean isCorrect = rs.getInt("is_correct") == 1;

                    // Liste groß genug machen, falls Antworten Lücken haben oder so
                    // brauchen wir das wirklich?
                    while (answers.size() <= answerIndex) {
                        answers.add(null);
                    }
                    answers.set(answerIndex, answerText);

                    //wenn die Antwort korrekt ist, wird der Index gespeichert
                    if (isCorrect) {
                        correctIndex = answerIndex;
                    }
                }

                // letzte Frage abschließen, hier gibt es kein "neue Frage"-Signal, Abschluss der Schleife
                if (currentQuestionId != null) {
                    Question qObj = new Question(currentQuestionText, answers, correctIndex);
                    Quiz quiz = quizByNumber.computeIfAbsent(currentQuizNr, n -> new Quiz());
                    quiz.addQuestion(qObj);
                }
            }
//TODO: Logger einbauen?
        } catch (SQLException e) {
            e.printStackTrace(); //schreibt die Fehlermeldung in die Konsole
        }

        //alle Quizobjekte aus der Map werden in eine Liste gepackt, die zurückgegeben wird
        return new java.util.ArrayList<>(quizByNumber.values());
    }
}

