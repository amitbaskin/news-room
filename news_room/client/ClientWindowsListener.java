package news_room.client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientWindowsListener extends WindowAdapter {
    private final Client client;

    public ClientWindowsListener(Client client){
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public void windowClosed(WindowEvent e) {
        getClient().disconnect();
        getClient().getSocket().close();
        getClient().dispose();
    }
}