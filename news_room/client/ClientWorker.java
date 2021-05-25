package news_room.client;

import javax.swing.*;

public class ClientWorker extends SwingWorker<Object, Object> {
    private final Client client;

    public Client getClient() {
        return client;
    }

    public ClientWorker(Client client){
        this.client = client;
    }

    @Override
    protected Object doInBackground() throws Exception {
        getClient().getNews();
        return null;
    }
}