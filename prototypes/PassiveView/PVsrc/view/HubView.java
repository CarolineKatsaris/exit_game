package view;

import javax.swing.*;
import java.awt.*;

public class HubView extends JPanel {

    public HubView() {
        setLayout(new BorderLayout());

        ImageIcon img = new ImageIcon(getClass().getResource("/HubViewBackround.png"));
        JLabel imgLabel = new JLabel(img);

        add(imgLabel, BorderLayout.CENTER);
    }
}
