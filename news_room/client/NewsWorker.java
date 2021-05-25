package news_room.client;

import javax.swing.*;

/**
 * Gets the news in the background for a client
 */
public class NewsWorker extends SwingWorker<Object, Object> {
    Client client;

    /**
     * Creates a new news-getter-worker
     * @param client The client associated with this worker
     */
    public NewsWorker(Client client){
        this.client = client;
    }

    /**
     * Getting the news in the background
     * @return null
     * @throws Exception In case there's a problem getting the news
     */
    @Override
    protected Object doInBackground() throws Exception {
        client.getNews();
        return null;
    }
}