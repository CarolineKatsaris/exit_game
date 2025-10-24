import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Controller {
    private static MainView view;
    private static Model model;

    public Controller() {
       view = new MainView();
       model = new Model();

       model.addPropertyChangeListener(view);
       model.setGameState("introPanel");

        // Add mouse listener in the controller
        view.getCurrentPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Handle the click and update the model
                model.nextGameState();
            }
        });

       view.setVisible(true);
    }
}
