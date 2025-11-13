package model;

public enum EnumScreen {
    START("start"),
    HUB("hub"),
    LOGIN("login"),
    ROOM("room");

    private final String cardName;

    EnumScreen(String cardName) {
        this.cardName = cardName;
    }

    public String getCardName() {
        return cardName;
    }

    @Override
    public String toString() {
        return cardName;
    }
}
