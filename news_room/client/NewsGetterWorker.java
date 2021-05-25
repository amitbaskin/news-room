package news_room.client;

import javax.swing.*;

public class NewsGetterWorker extends SwingWorker<Object, Object> {
    Client client;

    public NewsGetterWorker(Client client){
        this.client = client;
    }

    @Override
    protected Object doInBackground() throws Exception {
        client.getNews();
        return null;
    }
}
