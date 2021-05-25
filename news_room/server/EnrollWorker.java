package news_room.server;

import javax.swing.*;

public class EnrollWorker extends SwingWorker<Object, Object> {
    private final Server server;

    public Server getServer() {
        return server;
    }

    public EnrollWorker(Server server){
        this.server = server;
    }

    @Override
    protected Object doInBackground() {
        getServer().enrollClients();
        return null;
    }
}