package news_room.server;

import javax.swing.*;

/**
 * Enrolls clients in the background
 */
public class EnrollWorker extends SwingWorker<Object, Object> {
    private final Server server;

    public EnrollWorker(Server server){
        this.server = server;
    }

    @Override
    protected Object doInBackground() {
        server.enrollClients();
        return null;
    }
}