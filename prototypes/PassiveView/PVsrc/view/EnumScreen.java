package view;

public enum EnumScreen {
    START("start"),
    HUB("hub");

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
