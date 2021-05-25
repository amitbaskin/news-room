package news_room.server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * A listener for server's windows
 */
public class WindowListener extends WindowAdapter {
    private final Server server;

    /**
     * Create a new listener
     * @param server The server associated with this listener
     */
    public WindowListener(Server server) {
        this.server = server;
    }

    /**
     * The action to do when closing the window
     * @param e The closing button was clicked
     */
    @Override
    public void windowClosing(WindowEvent e) {
        try {
            server.setEnrollmentOff(false);
            server.setClosed();
            server.sendToAll(Server.getDisconnectMsg());
            server.sendStop();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }
}