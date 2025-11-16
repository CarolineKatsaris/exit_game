import javax.swing.*;
import java.awt.EventQueue;

import controller.QuizController;
import model.Model;
import view.MainView;
import controller.Controller;

public class ExitGame {
    public static void main(String[] args) {

        // hübsches Look & Feel (optional)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        EventQueue.invokeLater(() -> {
            Model model  = new Model();
            MainView view = new MainView();
            Controller controller = new Controller(model, view);
            QuizController quizController = new QuizController(model, view);
        });
    }
}
