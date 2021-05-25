package news_room.server;

import javax.swing.*;

public class ServerEnrollBackground extends SwingWorker<Object, Object> {
    private final Server server;

    public Server getServer() {
        return server;
    }

    public ServerEnrollBackground(Server server){
        this.server = server;
    }

    @Override
    protected Object doInBackground() {
        getServer().enrollClients();
        return null;
    }
}