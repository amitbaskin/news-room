package news_room.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A listener for the unsubscribe-button
 */
public class UnsubscribeListener implements ActionListener {
    private final Client client;

    /**
     * Creates a new listener
     * @param client The client associated with this listener
     */
    public UnsubscribeListener(Client client){
        this.client = client;
    }

    /**
     * Gets the client
     * @return The client
     */
    public Client getClient() {
        return client;
    }

    /**
     * The action to perform when this listener is triggered
     * @param e The event that triggered this listener
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!getClient().getIsGetNews()) getClient().getUnsubscribedMsg();
        else {
            getClient().disconnect();
            getClient().setGetNews(false);
        }
    }
}