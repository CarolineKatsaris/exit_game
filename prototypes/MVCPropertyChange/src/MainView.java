import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

//siehe https://docs.oracle.com/javase/tutorial/uiswing/events/propertychangelistener.html
// https://www.baeldung.com/java-observer-pattern
public class MainView extends JFrame implements PropertyChangeListener {
    private JPanel mainPanel;
    private JPanel hub;
    private IntroPanel introPanel;
    private JButton room1Button;
    private JButton room2Button;
    private JButton room3Button;

    public MainView() {
        setContentPane(mainPanel);
        setTitle("Escape Game");
        setSize(1024, 768);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public void nextView(String cardName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, cardName);
    }

    public JPanel getCurrentPanel() {
        return introPanel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String subPanelName = ((String) evt.getNewValue());
        System.out.println(subPanelName);
        if (subPanelName.length() > 0 ) { //muss noch verbessert werden
            nextView(subPanelName);
        }
    }

    /**
     * Initializes and assigns custom UI components for the main view.
     * This method is specifically responsible for creating and associating
     * the `IntroPanel` instance with the designated `introPanel` variable.
     */
    private void createUIComponents() {
        introPanel = new IntroPanel();
    }


    class IntroPanel extends JPanel {
        private Image backgroundImage;

        public IntroPanel() {
            String imagePath = "prototypes/MVCPropertyChange/resources/intro.png";

            // Try to load image
            try {
                backgroundImage = new ImageIcon(imagePath).getImage();
            } catch (Exception e) {
                System.err.println("Could not load image: " + imagePath);
                backgroundImage = null;
            }

            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
