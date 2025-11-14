package model;

public class Screen {
    EnumScreen title;

    Screen(EnumScreen title) {
        this.title = title;
    }

    public EnumScreen getTitle() {
        return title;
    }
}
