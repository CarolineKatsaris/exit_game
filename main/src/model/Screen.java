package model;

import java.awt.*;

/**
 * Screen ist Elternklasse für Room, so können Räume und Screens wie Start, Login, Hub im restlichen Code einheitlich verwendet werden.
 */
public class Screen {
    private final EnumScreen title;
    private String errorMessage; //generische Fehlermeldung, die auf allen Screens angezeigt werden kann
    private boolean isError = false; //Fehler trat auf
    private boolean isListenersRegistered = false; //Listeners wurden schon registriert?

    private String introText;
    private String outroText;
    private boolean introShown = false; //true, wenn das Intro angezeigt wurde und nicht noch einmal angezeigt werden soll
    private boolean outroShown = false;//true, wenn das Outro angezeigt wurde und nicht noch einmal angezeigt werden soll
    private boolean showIntro = false; //Sagt der View, dass sie das Intro anzeigen soll
    private boolean showOutro = false; //Sagt der View, dass sie das Outro anzeigen soll
    private String introImagePath;
    private Rectangle[] quizBtnsBounds; //Array aus Rechtecken für Klickbuttons
    private String outroImagePath; //Outro-Ansicht hat keinen Klickbuttons

    /**
     * Erstellt einen Screen mit dem gegebenen Titel.
     */
    public Screen(EnumScreen title) {
        this.title = title;
    } // public oder private?

    public String getIntroImagePath() {
        return introImagePath;
    }

    public void setIntroImagePath(String introImagePath) {
        this.introImagePath = introImagePath;
    }

    public Rectangle[] getQuizBtnsBounds() {
        return quizBtnsBounds;
    }

    public void setQuizBtnsBounds(Rectangle[] quizBtnsBounds) {
        this.quizBtnsBounds = quizBtnsBounds;
    }

    public String getOutroImagePath() {
        return outroImagePath;
    }

    public void setOutroImagePath(String outroImagePath) {
        this.outroImagePath = outroImagePath;
    }

    public boolean isShowIntro() {
        return showIntro;
    }

    public void setShowIntro(boolean showIntro) {
        this.showIntro = showIntro;
    }

    public boolean isShowOutro() {
        return showOutro;
    }

    public void setShowOutro(boolean showOutro) {
        this.showOutro = showOutro;
    }

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

    /**
     * Verwaltung des Intro-Storytexts dieses Screens.
     * Der Introtext wird nur einmal beim ersten Betreten angezeigt.
     */

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

    /**
     * Verwaltung des Outro-Storytexts dieses Screens.
     * Der Outrotext wird angezeigt, wenn der Screen abgeschlossen wurde.
     */

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
