import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

// observable
public class Model {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private String GameState;

    public  Model() {

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public String getGameState() {
        return GameState;
    }

    //momentan: GameState muss gleich Name der Card des Unterpanels in mainPanel sein, das muss generischer implementiert werden
    public void setGameState(String text) {
        String oldGameState = GameState;
        GameState = text;
        pcs.firePropertyChange("GameState", oldGameState, GameState);
    }

    public void nextGameState() {
        //hubPanel fehlt noch
        if (GameState.equals("introPanel")) {
            setGameState("typeWriterPanel1");
        } else {
            setGameState("introPanel");
        }
    }

}
