import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

//siehe https://docs.oracle.com/javase/tutorial/uiswing/events/propertychangelistener.html
// https://www.baeldung.com/java-observer-pattern
public class MainView extends JFrame implements PropertyChangeListener {
    private JPanel mainPanel;

    private JPanel hubPanel;
    private JButton room1Button;
    private JButton room2Button;
    private JButton room3Button;

    private IntroPanel introPanel;
    private TypeWriterPanel typeWriterPanel1;

    public MainView() {
        setContentPane(mainPanel);
        setTitle("Escape Game");
        setSize(1024, 768);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public void nextView(String cardName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, cardName);

        // Start typing animation when typeWriterPanel1 card is shown
        if ("typeWriterPanel1".equals(cardName) && typeWriterPanel1 != null) {
            Timer startTimer = new Timer(300, e -> typeWriterPanel1.startTypingAnimation());
            startTimer.setRepeats(false);
            startTimer.start();
        }
    }

    public JPanel getCurrentPanel() {
        //ToDo bessere Logik
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
        typeWriterPanel1 = new TypeWriterPanel("prototypes/MVCPropertyChange/resources/intro.png", "Ein Virus verbreitet sich in deiner Schule und diesmal ist es nicht Corona...", 100, 100, 600, 400);
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

    public class TypeWriterPanel extends JPanel {
        private BufferedImage backgroundImage;
        private String fullText;
        private String displayedText = "";
        private int textX;
        private int textY;
        private int boxWidth;
        private int boxHeight;
        private Timer typingTimer;
        private int currentCharIndex = 0;
        private int typingDelay = 50; // milliseconds between each character

        public TypeWriterPanel(String imagePath, String text, int x, int y, int width, int height) {
            this.fullText = text;
            this.textX = x;
            this.textY = y;
            this.boxWidth = width;
            this.boxHeight = height;

            // Load background image
            try {
                backgroundImage = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }

            setPreferredSize(new Dimension(800, 600)); // Adjust as needed
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Enable anti-aliasing for smooth text
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Draw background image
            if (backgroundImage != null) {
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }

            // Draw semi-transparent box
            g2d.setColor(new Color(0, 0, 0, 128)); // Black with 50% transparency (alpha = 128)
            g2d.fillRoundRect(textX, textY, boxWidth, boxHeight, 15, 15); // Rounded corners

            // Optional: Draw border around box
            g2d.setColor(new Color(255, 255, 255, 200)); // White border with more opacity
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(textX, textY, boxWidth, boxHeight, 15, 15);

            // Draw text
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 16));

            // Draw text with word wrapping
            drawWrappedText(g2d, displayedText, textX + 15, textY + 30, boxWidth - 30);
        }

        private void drawWrappedText(Graphics2D g2d, String text, int x, int y, int maxWidth) {
            FontMetrics fm = g2d.getFontMetrics();
            String[] words = text.split(" ");
            StringBuilder line = new StringBuilder();
            int lineY = y;

            for (String word : words) {
                String testLine = line.length() == 0 ? word : line + " " + word;
                int lineWidth = fm.stringWidth(testLine);

                if (lineWidth > maxWidth && line.length() > 0) {
                    g2d.drawString(line.toString(), x, lineY);
                    lineY += fm.getHeight();
                    line = new StringBuilder(word);
                } else {
                    line = new StringBuilder(testLine);
                }
            }

            // Draw the last line
            if (line.length() > 0) {
                g2d.drawString(line.toString(), x, lineY);
            }
        }

        public void startTypingAnimation() {
            currentCharIndex = 0;
            displayedText = "";

            typingTimer = new Timer(typingDelay, e -> {
                if (currentCharIndex < fullText.length()) {
                    displayedText += fullText.charAt(currentCharIndex);
                    currentCharIndex++;
                    repaint();
                } else {
                    typingTimer.stop();
                }
            });

            typingTimer.start();
        }

        public void setTypingDelay(int delay) {
            this.typingDelay = delay;
        }

        public void stopTyping() {
            if (typingTimer != null) {
                typingTimer.stop();
            }
        }

        // Show all text immediately
        public void showAllText() {
            stopTyping();
            displayedText = fullText;
            repaint();
        }
    }
}
