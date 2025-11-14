package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HoverAdapter extends MouseAdapter {

    private final Runnable onEnter;
    private final Runnable onExit;

    public HoverAdapter(Runnable onEnter, Runnable onExit) {
        this.onEnter = onEnter;
        this.onExit = onExit;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        onEnter.run();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        onExit.run();
    }
}
