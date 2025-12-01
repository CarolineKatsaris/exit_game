package model;

public class Screen {
    private final EnumScreen title;
    private String errorMessage; //generische Fehlermeldung, die auf allen Screens angezeigt werden kann
    private boolean isError = false; //Fehler trat auf

    public Screen(EnumScreen title) {
        this.title = title;
    } // public oder private?

    public boolean isError() {
        return isError;
    }

    /**
     * setzt die Fehlermeldung zurück
     */
    public void clearErrorMessage() {
        this.errorMessage = "";
        isError = false;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        isError = true;
    }

    public EnumScreen getTitle() {
        return title;
    }
}
