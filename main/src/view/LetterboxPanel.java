package view;

import javax.swing.*;
import java.awt.*;

public class LetterboxPanel extends JPanel {
    private final JComponent content;
    private final Dimension contentSize;

    public LetterboxPanel(JComponent content, Dimension contentSize) {
        this.content = content;
        this.contentSize = contentSize;

        setLayout(null);
        setBackground(Color.BLACK);
        setOpaque(true);

        content.setPreferredSize(contentSize);
        add(content);
    }

    @Override
    public void doLayout() {
        int w = getWidth();
        int h = getHeight();

        int cw = contentSize.width;
        int ch = contentSize.height;

        int x = (w - cw) / 2;
        int y = (h - ch) / 2;

        content.setBounds(x, y, cw, ch);
    }
}
