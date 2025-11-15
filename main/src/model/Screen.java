package model;

public class Screen {
    private  EnumScreen title;

    public Screen(EnumScreen title) {
        this.title = title;
    } // public oder private?

    public EnumScreen getTitle() {
        return title;
    }
}
