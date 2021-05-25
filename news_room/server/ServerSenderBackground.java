package news_room.server;

import javax.swing.*;
import java.io.IOException;

public class ServerSenderBackground extends SwingWorker<Object, Object> {
    private final Server server;

    public ServerSenderBackground(Server server){
        this.server = server;
    }

    public Server getServer() {
        return server;
    }

    @Override
    protected Object doInBackground() throws IOException {
        getServer().send();
        return null;
    }
}