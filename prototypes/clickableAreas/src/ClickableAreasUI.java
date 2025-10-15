import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ClickableAreasUI extends JFrame {
    private JPanel panel1;
    private GamePanel gamePanel;

    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    public ClickableAreasUI() {
        setTitle("Escape Game");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create custom game panel
        gamePanel = new GamePanel();
        setContentPane(gamePanel);
    }

    // Inner class to represent clickable areas
    static class ClickableArea {
        int x, y, width, height;
        String label;
        Color color;

        public ClickableArea(int x, int y, int width, int height, String label) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.label = label;
            this.color = new Color(255, 0, 0, 80); // Semi-transparent red
        }

        public boolean contains(int px, int py) {
            return px >= x && px <= x + width && py >= y && py <= y + height;
        }
    }

    // Inner class for game panel
    class GamePanel extends JPanel {
        private GameState currentState;
        private int currentRoom;
        private Image backgroundImage;
        private JDialog riddleDialog;
        private boolean showClickableAreas = true; // Toggle to show/hide areas
        private List<ClickableArea> currentClickableAreas;
        private ClickableArea hoveredArea = null;

        public GamePanel() {
            currentState = GameState.INTRO;
            currentRoom = 0;
            currentClickableAreas = new ArrayList<>();
            loadBackground();

            // Add mouse listener for clickable areas
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleClick(e.getX(), e.getY());
                }
            });

            // Add mouse motion listener for hover effects
            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    ClickableArea previousHovered = hoveredArea;
                    hoveredArea = null;

                    for (ClickableArea area : currentClickableAreas) {
                        if (area.contains(e.getX(), e.getY())) {
                            hoveredArea = area;
                            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                            break;
                        }
                    }

                    if (hoveredArea == null) {
                        setCursor(Cursor.getDefaultCursor());
                    }

                    // Repaint only if hover state changed
                    if (hoveredArea != previousHovered) {
                        repaint();
                    }
                }
            });

            // Add key listener to toggle clickable areas visibility
            setFocusable(true);
            addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_H) {
                        showClickableAreas = !showClickableAreas;
                        repaint();
                    }
                }
            });
        }

        private void loadBackground() {
            // Load background based on current state
            String imagePath = "";

            if (currentState == GameState.INTRO) {
                imagePath = "prototypes/clickableAreas/resources/intro.png";
            } else if (currentState == GameState.ROOM) {
                imagePath = "resources/room" + currentRoom + ".png";
            }

            // Try to load image
            try {
                backgroundImage = new ImageIcon(imagePath).getImage();
            } catch (Exception e) {
                System.err.println("Could not load image: " + imagePath);
                backgroundImage = null;
            }

            // Load clickable areas for current state
            loadClickableAreas();

            repaint();
        }

        private void loadClickableAreas() {
            currentClickableAreas.clear();

            if (currentState == GameState.INTRO) {
                // Intro screen - continue arrow area
                currentClickableAreas.add(new ClickableArea(900, 650, 100, 100, "Continue"));
            } else if (currentState == GameState.ROOM) {
                // Define clickable areas for each room
                switch (currentRoom) {
                    case 1:
                        currentClickableAreas.add(new ClickableArea(200, 300, 150, 150, "Riddle 1-1"));
                        currentClickableAreas.add(new ClickableArea(500, 400, 100, 100, "Riddle 1-2"));
                        break;
                    case 2:
                        currentClickableAreas.add(new ClickableArea(300, 200, 120, 120, "Riddle 2-1"));
                        currentClickableAreas.add(new ClickableArea(600, 450, 100, 100, "Riddle 2-2"));
                        break;
                    case 3:
                        currentClickableAreas.add(new ClickableArea(400, 350, 100, 150, "Riddle 3-1"));
                        currentClickableAreas.add(new ClickableArea(150, 250, 120, 120, "Riddle 3-2"));
                        break;
                    case 4:
                        currentClickableAreas.add(new ClickableArea(250, 250, 130, 130, "Riddle 4-1"));
                        currentClickableAreas.add(new ClickableArea(700, 300, 110, 110, "Riddle 4-2"));
                        break;
                    case 5:
                        currentClickableAreas.add(new ClickableArea(600, 300, 110, 110, "Riddle 5-1"));
                        currentClickableAreas.add(new ClickableArea(350, 500, 100, 100, "Riddle 5-2"));
                        break;
                    case 6:
                        currentClickableAreas.add(new ClickableArea(350, 450, 140, 100, "Riddle 6-1"));
                        currentClickableAreas.add(new ClickableArea(550, 200, 120, 120, "Riddle 6-2"));
                        break;
                    case 7:
                        currentClickableAreas.add(new ClickableArea(450, 350, 150, 150, "Riddle 7-1"));
                        currentClickableAreas.add(new ClickableArea(200, 400, 130, 130, "Riddle 7-2"));
                        break;
                }
            }
        }

        private void handleClick(int x, int y) {
            if (currentState == GameState.INTRO) {
                // Check if clicked on continue arrow area
                for (ClickableArea area : currentClickableAreas) {
                    if (area.contains(x, y)) {
                        currentState = GameState.ROOM;
                        currentRoom = 1;
                        loadBackground();
                        return;
                    }
                }
            } else if (currentState == GameState.ROOM) {
                // Check clickable hotspots for current room
                checkRoomHotspots(x, y);
            }
        }

        private void checkRoomHotspots(int x, int y) {
            // Check all clickable areas in current room
            for (int i = 0; i < currentClickableAreas.size(); i++) {
                ClickableArea area = currentClickableAreas.get(i);
                if (area.contains(x, y)) {
                    showRiddle(currentRoom, i + 1);
                    return;
                }
            }
        }

        private void showRiddle(int roomNumber, int riddleNumber) {
            // Create riddle dialog
            riddleDialog = new JDialog(ClickableAreasUI.this, "Riddle " + roomNumber + "-" + riddleNumber, true);
            riddleDialog.setSize(600, 400);
            riddleDialog.setLocationRelativeTo(this);
            riddleDialog.setUndecorated(false);

            JPanel riddlePanel = new JPanel();
            riddlePanel.setLayout(new BorderLayout());
            riddlePanel.setBackground(new Color(0, 0, 0, 200));

            // Riddle content
            JTextArea riddleText = new JTextArea();
            riddleText.setText(getRiddleText(roomNumber, riddleNumber));
            riddleText.setEditable(false);
            riddleText.setWrapStyleWord(true);
            riddleText.setLineWrap(true);
            riddleText.setFont(new Font("Arial", Font.BOLD, 16));
            riddleText.setBackground(Color.WHITE);
            riddleText.setMargin(new Insets(20, 20, 20, 20));

            JScrollPane scrollPane = new JScrollPane(riddleText);
            riddlePanel.add(scrollPane, BorderLayout.CENTER);

            // Answer input
            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new FlowLayout());
            JTextField answerField = new JTextField(20);
            JButton submitButton = new JButton("Submit");
            JButton closeButton = new JButton("Close");

            submitButton.addActionListener(e -> {
                String answer = answerField.getText().trim();
                if (checkAnswer(roomNumber, riddleNumber, answer)) {
                    JOptionPane.showMessageDialog(riddleDialog, "Correct! Well done!");
                    riddleDialog.dispose();
                    // Progress to next room if all riddles solved
                    progressGame();
                } else {
                    JOptionPane.showMessageDialog(riddleDialog, "Incorrect. Try again!");
                    answerField.setText("");
                }
            });

            closeButton.addActionListener(e -> riddleDialog.dispose());

            inputPanel.add(new JLabel("Answer: "));
            inputPanel.add(answerField);
            inputPanel.add(submitButton);
            inputPanel.add(closeButton);

            riddlePanel.add(inputPanel, BorderLayout.SOUTH);

            riddleDialog.setContentPane(riddlePanel);
            riddleDialog.setVisible(true);
        }

        private String getRiddleText(int roomNumber, int riddleNumber) {
            // Define riddles for each room
            return "Room " + roomNumber + " - Riddle " + riddleNumber + ":\n\n" +
                    "I speak without a mouth and hear without ears.\n" +
                    "I have no body, but I come alive with wind.\n" +
                    "What am I?";
        }

        private boolean checkAnswer(int roomNumber, int riddleNumber, String answer) {
            // Define correct answers for each riddle
            // This is a simple example - you would expand this
            String correctAnswer = "echo"; // Example answer
            return answer.equalsIgnoreCase(correctAnswer);
        }

        private void progressGame() {
            // Check if player should move to next room
            if (currentRoom < 7) {
                int response = JOptionPane.showConfirmDialog(
                        this,
                        "Ready to proceed to the next room?",
                        "Progress",
                        JOptionPane.YES_NO_OPTION
                );

                if (response == JOptionPane.YES_OPTION) {
                    currentRoom++;
                    if (currentRoom <= 7) {
                        loadBackground();
                    } else {
                        // Game completed
                        JOptionPane.showMessageDialog(
                                this,
                                "Congratulations! You've escaped!",
                                "Victory",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Draw background image
            if (backgroundImage != null) {
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Fallback if no image
                g2d.setColor(Color.DARK_GRAY);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 24));

                if (currentState == GameState.INTRO) {
                    g2d.drawString("ESCAPE GAME", getWidth()/2 - 100, getHeight()/2 - 50);
                    g2d.drawString("Click bottom-right to continue ->", getWidth()/2 - 200, getHeight()/2);
                } else {
                    g2d.drawString("Room " + currentRoom, getWidth()/2 - 50, 50);
                }
            }

            // Draw clickable areas overlay
            if (showClickableAreas) {
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                for (ClickableArea area : currentClickableAreas) {
                    // Highlight hovered area differently
                    if (area == hoveredArea) {
                        g2d.setColor(new Color(0, 255, 0, 120)); // Brighter green when hovered
                        g2d.fillRect(area.x, area.y, area.width, area.height);
                        g2d.setColor(new Color(0, 200, 0, 255));
                        g2d.setStroke(new BasicStroke(3));
                    } else {
                        g2d.setColor(area.color);
                        g2d.fillRect(area.x, area.y, area.width, area.height);
                        g2d.setColor(new Color(255, 0, 0, 200));
                        g2d.setStroke(new BasicStroke(2));
                    }

                    // Draw border
                    g2d.drawRect(area.x, area.y, area.width, area.height);

                    // Draw label
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Arial", Font.BOLD, 14));
                    FontMetrics fm = g2d.getFontMetrics();
                    int labelWidth = fm.stringWidth(area.label);
                    int labelX = area.x + (area.width - labelWidth) / 2;
                    int labelY = area.y + (area.height + fm.getAscent()) / 2;

                    // Draw background for text
                    g2d.setColor(new Color(0, 0, 0, 180));
                    g2d.fillRect(labelX - 5, labelY - fm.getAscent() - 2,
                            labelWidth + 10, fm.getHeight() + 4);

                    // Draw text
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(area.label, labelX, labelY);
                }
            }

            // Draw help text
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRect(5, getHeight() - 25, 250, 20);
            g2d.setColor(Color.WHITE);
            g2d.drawString("Press 'H' to toggle clickable areas", 10, getHeight() - 10);
        }
    }

    enum GameState {
        INTRO,
        ROOM
    }
}