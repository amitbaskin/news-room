package news_room.client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A listener for a client's window
 */
public class WindowListener extends WindowAdapter {
    private final Client client;

    /**
     * Creates a new listener
     * @param client The client associated with this listener
     */
    public WindowListener(Client client){
        this.client = client;
    }

    /**
     * Gets the client associated with this listener
     * @return The client associated with this listener
     */
    public Client getClient() {
        return client;
    }

    /**
     * What to do when the client tries to close its window
     * @param e The event that triggered this window-closing action
     */
    @Override
    public void windowClosed(WindowEvent e) {
        getClient().disconnect();
        getClient().getSocket().close();
        getClient().dispose();
    }
}