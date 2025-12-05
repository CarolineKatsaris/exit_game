package model;

public class Screen {
    private final EnumScreen title;
    private String errorMessage; //generische Fehlermeldung, die auf allen Screens angezeigt werden kann
    private boolean isError = false; //Fehler trat auf
    private boolean isListenersRegistered = false; //Listeners wurden schon registriert?

    private String introText;
    private String outroText;
    private boolean introShown = false;
    private boolean outroShown = false;


    public Screen(EnumScreen title) {
        this.title = title;
    } // public oder private?

    public boolean isListenersRegistered() {
        return isListenersRegistered;
    }

    public void setListenersRegistered(boolean listenersRegistered) {
        isListenersRegistered = listenersRegistered;
    }

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
    // Intro-Text

    public String getIntroText() {
        return introText;
    }

    public void setIntroText(String introText) {
        this.introText = introText;
    }

    public boolean isIntroShown() {
        return introShown;
    }

    public void setIntroShown(boolean introShown) {
        this.introShown = introShown;
    }

    // Outro-Text

    public String getOutroText() {
        return outroText;
    }

    public void setOutroText(String outroText) {
        this.outroText = outroText;
    }

    public boolean isOutroShown() {
        return outroShown;
    }

    public void setOutroShown(boolean outroShown) {
        this.outroShown = outroShown;
    }

}
