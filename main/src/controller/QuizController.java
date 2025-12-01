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
                    model.handleQuizAnswer(e.getActionCommand())
            );
        }
        model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "quizShown" -> {
                Question q = (Question) evt.getNewValue();
                view.showQuiz(q);
            }
            case "quizHidden" -> view.hideQuiz();
            // andere Events: Screen-Wechsel usw.
        }
    }
}
