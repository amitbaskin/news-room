package news_room.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientUnsubscribeBtnListener implements ActionListener {
    private final Client client;

    public ClientUnsubscribeBtnListener(Client client){
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!getClient().getIsGetNews()) getClient().getUnsubscribedMsg();
        else {
            getClient().disconnect();
            getClient().setGetNews(false);
        }
    }
}