package view;

import model.EnumScreen;

import javax.swing.*;

import java.awt.*;

import javax.sound.sampled.*;


public class HubView extends JLayeredView {
    private JButton graphicsCardBtn;
    private JButton ramBtn;
    private JButton fileBtn;
    private JButton networkBtn;
    private JButton cpuBtn;
    private JLabel finalStatsLabel;
    private JLabel storyLabel;

    private JLabel[] virusLabels;
    private Timer virusTimer;

    private int[] vx = {2, 1, 3}; // unterschiedliche Geschwindigkeiten
    private int[] vy = {1, 2, 1};

    private Clip teleportClip;




    public HubView() {
        super(); //Konstruktor der JLayeredView zuerst aufrufen

        // Invisible Button zum Panel hinzufügen
        graphicsCardBtn = makeInvisibleButton(80, 200, 250, 300, EnumScreen.GraphicRoom.toString());
        ramBtn = makeInvisibleButton(230, 540, 230, 140, EnumScreen.RAMRoom.toString());
        fileBtn = makeInvisibleButton(610, 245, 220, 110, EnumScreen.FileRoom.toString());
        networkBtn = makeInvisibleButton(1044, 250, 330, 90, EnumScreen.NetRoom.toString());
        cpuBtn = makeInvisibleButton(1244, 560, 204, 190, EnumScreen.CPURoom.toString());
        add(graphicsCardBtn, Integer.valueOf(1)); // eine Ebene drüber
        add(ramBtn, Integer.valueOf(1));
        add(fileBtn, Integer.valueOf(1));
        add(networkBtn, Integer.valueOf(1));
        add(cpuBtn, Integer.valueOf(1));

        // Felder für abschließende Botschaft und Gesamtfehlerzahl
        int centerX = 1536 / 2;
        int centerY = 1024 / 2;

        storyLabel = new JLabel();
        storyLabel.setBounds(centerX - 1450/2, centerY - 70, 1450, 40);
        storyLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        storyLabel.setForeground(Color.WHITE);
        storyLabel.setOpaque(true);
        storyLabel.setBackground(new Color(0, 0, 0, 180));
        storyLabel.setHorizontalAlignment(SwingConstants.LEFT);
        storyLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        storyLabel.setVisible(false);
        add(storyLabel, Integer.valueOf(5));

        finalStatsLabel = new JLabel();
        finalStatsLabel.setBounds(centerX - 600/2, centerY - 20, 600, 50);
        finalStatsLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        finalStatsLabel.setForeground(Color.RED);
        finalStatsLabel.setOpaque(true);
        finalStatsLabel.setBackground(new Color(0, 0, 0, 180));
        finalStatsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        finalStatsLabel.setVisible(false);
        add(finalStatsLabel, Integer.valueOf(5));

        // FlyingVirus-Spielerei
        virusLabels = new JLabel[3];

        int[] sizes = {60, 50, 40}; // groß, normal, klein
        int[][] startPos = {
                {200, 150},
                {500, 300},
                {800, 200}
        };

        for (int i = 0; i < virusLabels.length; i++) {
            ImageIcon icon = new ImageIcon(getClass().getResource("/virus.png"));
            Image scaled = icon.getImage().getScaledInstance(
                    sizes[i], sizes[i], Image.SCALE_SMOOTH
            );

            int hit = 20; // extra Klickfläche (10px Rand rundum)

            virusLabels[i] = new JLabel(new ImageIcon(scaled), SwingConstants.CENTER);

            // Bounds größer, damit man leichter trifft (Bild bleibt zentriert)
            virusLabels[i].setBounds(
                    startPos[i][0] - hit / 2,
                    startPos[i][1] - hit / 2,
                    sizes[i] + hit,
                    sizes[i] + hit
            );


            add(virusLabels[i], Integer.valueOf(4)); // unter UI, über Background

            // Klickbar machen
            virusLabels[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            virusLabels[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    teleportVirus((JComponent) e.getSource());
                    playTeleportSound();
                }
            });

        }

// ein Timer für alle Viren
        virusTimer = new Timer(30, e -> moveViruses());
        virusTimer.start();

        loadTeleportSound();

    }

    private void moveViruses() {
        if (getWidth() == 0 || getHeight() == 0) return;

        for (int i = 0; i < virusLabels.length; i++) {
            JLabel v = virusLabels[i];

            int x = v.getX() + vx[i];
            int y = v.getY() + vy[i];

            int maxX = getWidth() - v.getWidth();
            int maxY = getHeight() - v.getHeight();

            if (x <= 0 || x >= maxX) vx[i] = -vx[i];
            if (y <= 0 || y >= maxY) vy[i] = -vy[i];

            v.setLocation(
                    Math.max(0, Math.min(x, maxX)),
                    Math.max(0, Math.min(y, maxY))
            );
        }
    }

    private void hideViruses() {
        if (virusTimer != null) virusTimer.stop();

        if (virusLabels != null) {
            for (JLabel v : virusLabels) {
                if (v != null) v.setVisible(false);
            }
        }
    }

    // abschließende Botschaft abhängig von Fehlerzahl im Hub (nach Rückkehr aus CPU)
    public void showFinalStats(int wrongAnswers) {
        hideViruses();

        String story;

        if (wrongAnswers == 0) {
            story = "Perfekt! Das System ist sauber – kein einziger Fehlklick.";
        } else if (wrongAnswers <= 3) {
            story = "Stark! Du hast den Virus fast ohne Umwege gestoppt.";
        } else if (wrongAnswers <= 7) {
            story = "Geschafft! Der Virus ist gestoppt – aber er hat dich ganz schön getestet.";
        } else {
            story = "Du hast es geschafft… knapp. Der Virus hat ordentlich Widerstand geleistet.";
        }

            storyLabel.setText(story);
            finalStatsLabel.setText("Gesamtzahl falscher Antworten: " + wrongAnswers);

            storyLabel.setVisible(true);
            finalStatsLabel.setVisible(true);
    }

    void setButtonsEnabled(boolean enabled) {
        graphicsCardBtn.setEnabled(enabled);
        //if (networkBtn != null) networkBtn.setEnabled(enabled);
        //if (cpuBtn != null) cpuBtn.setEnabled(enabled);
        // ggf. weitere Buttons
    }

    private JButton makeInvisibleButton(int x, int y, int w, int h, String cmd) {

        JButton b = new JButton();
        b.setBounds(x, y, w, h);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setActionCommand(cmd);
        return b;
    }


    // Hover-Highlight (Rand)



    public void setButtonHighlight(JButton button, boolean on) {

        if (on) {
            button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
            button.setBorderPainted(true);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setBorderPainted(false);
            button.setCursor(Cursor.getDefaultCursor());
        }

        repaint();
    }


    public JButton getGraphicsCardButton() {
        return graphicsCardBtn;
    }
    public JButton getRamButton() { return ramBtn; }
    public JButton getFileBtn() {return fileBtn; }
    public JButton getNetworkBtn() {return networkBtn;}
    public JButton getCpuBtn() {return cpuBtn;}

    private void loadTeleportSound() {
    try (AudioInputStream ais = AudioSystem.getAudioInputStream(
            getClass().getResource("/teleport.wav")
    )) {
        teleportClip = AudioSystem.getClip();
        teleportClip.open(ais);
    } catch (Exception e) {
        e.printStackTrace(); // Debug: zeigt dir, wenn Datei nicht gefunden / Format falsch
        teleportClip = null;
    }
}

    private void playTeleportSound() {
    if (teleportClip == null) return;

    if (teleportClip.isRunning()) teleportClip.stop();
    teleportClip.setFramePosition(0);
    teleportClip.start();
}

    private void teleportVirus(JComponent v) {
    int maxX = getWidth() - v.getWidth();
    int maxY = getHeight() - v.getHeight();
    if (maxX <= 0 || maxY <= 0) return;

    int newX = (int) (Math.random() * maxX);
    int newY = (int) (Math.random() * maxY);

    v.setLocation(newX, newY);
}

}