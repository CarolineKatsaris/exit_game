package view;

import java.awt.Rectangle;

public class GraphicsCardRoomView extends AbstractRoomView {

    @Override
    protected String getBackgroundImagePath() {
        return "/GraphicsCardRoomView_elements.png";
    }

    @Override
    protected Rectangle[] getQuizButtonBounds() {
        return new Rectangle[] {
                // quiz_1
                new Rectangle(400, 400, 120, 290),
                // quiz_2
                new Rectangle(1000, 330, 240, 180),
                // quiz_3
                new Rectangle(1130, 640, 260, 160)
        };
    }
}
