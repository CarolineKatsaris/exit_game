package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private String backgroundImagePath; //Model muss festlegen, welches Bild als Hintergrund verwendet werden soll, nur dieses wird von View angezeigt

    /**
     * Erstellt einen Screen mit dem gegebenen Titel.
     */
    public Screen(EnumScreen title) {
        this.title = title;
    } // public oder private?

    public String getBackgroundImagePath() {
        return backgroundImagePath;
    }

    public void setBackgroundImagePath(String backgroundImagePath) {
        this.backgroundImagePath = backgroundImagePath;
    }

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
        if (introShown) showIntro = false; //showIntro zurücksetzen, wenn Intro angezeigt wurde
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
        if (outroShown) showOutro = false; //showOutro zurücksetzen, wenn Outro angezeigt wurde
    }

    /**
     * Parst einen JSON-String wie "[[400,400,120,290],[1000,330,240,180],[1130,640,260,160]]"
     * in ein Rectangle[] Array ohne externe Libraries
     */
    Rectangle[] parseQuizButtonBounds(String jsonString) {
        if (jsonString == null || jsonString.isBlank()) {
            return new Rectangle[0];
        }

        try {
            List<Rectangle> rectangles = new ArrayList<>();

            // Pattern findet alle [Zahl,Zahl,Zahl,Zahl] Kombinationen
            Pattern pattern = Pattern.compile("\\[(\\d+),(\\d+),(\\d+),(\\d+)\\]");
            Matcher matcher = pattern.matcher(jsonString);

            while (matcher.find()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                int width = Integer.parseInt(matcher.group(3));
                int height = Integer.parseInt(matcher.group(4));
                rectangles.add(new Rectangle(x, y, width, height));
            }

            return rectangles.toArray(new Rectangle[0]);
        } catch (Exception e) {
            System.err.println("Fehler beim Parsen der Quiz-Button-Bounds: " + jsonString);
            e.printStackTrace();
            return new Rectangle[0];
        }
    }
}
