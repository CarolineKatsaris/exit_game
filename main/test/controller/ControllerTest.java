package controller;

import model.EnumScreen;
import model.Model;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.MainView;

import java.awt.event.ActionListener;
import java.util.Arrays;

import static java.awt.AWTEventMulticaster.getListeners;
import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    Model model;
    MainView view;
    Controller controller;

    /**
     * Initializes the necessary objects required for testing the Controller class.
     * This method is executed before each test case to ensure a clean setup.
     *
     * Responsibilities:
     * - Instantiates a new Model object.
     * - Creates a new MainView object.
     * - Constructs a Controller instance by passing the Model and MainView objects as dependencies.
     *
     * This setup ensures the test environment is consistent and isolates the tests
     * from each other, preventing side effects from shared states.
     */
    @BeforeEach
    void setUp() {
        model  = new Model();
        view = new MainView();
        controller = new Controller(model, view);
    }

    @AfterEach
    void tearDown() {

    }

    /**
     * Tests the functionality of the `loadScreen` method in the `Controller` class.
     * Ensures that the method correctly loads the specified screen and updates action listeners
     * for interactive elements within the view.
     *
     * The test performs the following checks:
     * - Verifies the number of action listeners attached to the start button before and after calling `loadScreen`.
     * - Confirms that the appropriate action listeners are added when loading the "Start" screen.
     *
     * This test helps ensure that the controller properly integrates with the view and sets up the necessary
     * event-handling mechanisms for user interactions.
     */
    @Test
    void loadScreen() {
        //Erwartet: 1 Listener, da der Konstruktor von Controller model.setStartState() aufruft was dadurch den Start Screen lädt und den ersten Listener hinzufügt.
        assertEquals(1, view.getStartButton().getActionListeners().length);

        //Erwartet: 0 Listener, da der Controller noch keinen an den SubmitButton angehängt hat
        assertEquals(0, view.getSubmitButton().getActionListeners().length);

        //Weiterschalten zu Login Screen
        controller.loadScreen(EnumScreen.Login);

        //Erwartet: Immer noch 1 Listener, da der Login Screen keine Listener mehr an den StartButton hängen soll
        assertEquals(1, view.getStartButton().getActionListeners().length);

        //Erwartet: 1 Listener, da der Controller jetzt einen an den SubmitButton angehängt hat
        assertEquals(1, view.getSubmitButton().getActionListeners().length);

    }
}