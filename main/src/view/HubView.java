package view;

import model.EnumScreen;

import javax.swing.*;

import java.awt.*;

public class HubView extends JLayeredView {

    private JLabel background;

    private JButton graphicsCardBtn;
    private JButton ramBtn;
    private JButton fileBtn;
    private JButton networkBtn;
    private JButton cpuBtn;


    public HubView() {

        setLayout(null);

        ImageIcon img = new ImageIcon(getClass().getResource("/HubViewBackground.png"));
        background = new JLabel(img);
        background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
        add(background, Integer.valueOf(0));  // unterste Ebene

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
}