package news_room.server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ServerWindowListener extends WindowAdapter {
    private final Server server;

    public ServerWindowListener(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            getServer().setEnrollmentOff(false);
            getServer().setIsOpen(false);
            getServer().sendToAll(Server.DISCONNECT);
            getServer().sendStop();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }
}