package controller;

import model.Model;
import model.Question;
import view.MainView;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.*;
import java.awt.*;

public class QuizController implements PropertyChangeListener {
    private Model model;
    private MainView view;

    public QuizController(Model model, MainView view) {
        this.model = model;
        this.view = view;

        // Quiz-Buttons registrieren
        for (JButton btn : view.getQuizAnswerButtons()) {
            btn.addActionListener(e ->
                    model.handleQuizAnswer(e.getActionCommand()));


        }
        view.getQuizStopButton().addActionListener(e ->model.cancelQuiz());


        this.model.addPropertyChangeListener(this);
    }



    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "quizShown" -> {
                Question q = (Question) evt.getNewValue();
                view.showQuiz(q);
            break;}

            case "quizHidden" -> view.hideQuiz();
            // andere Events: Screen-Wechsel usw.

            case "incorrectAnswer" ->  {
               int chosenIndex = (int) evt.getNewValue();
                view.highlightIncorrectAnswer(chosenIndex);
                view.requestFocus();
            }
            case "progress" -> {
               view.updateProgress((int) evt.getNewValue());
            }
            case "correctAnswer" ->  {
                int chosenIndex = (int) evt.getNewValue();
                view.highlightCorrectAnswer(chosenIndex);
                view.requestFocus();
            }

        }
    }
}
