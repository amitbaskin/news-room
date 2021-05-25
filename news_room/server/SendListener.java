package news_room.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A listener for sending news
 */
public class SendListener implements ActionListener {
    private final SendWorker sendWorker;

    /**
     * Create a new listener
     * @param server The server associated with this listener
     */
    public SendListener(Server server){
        sendWorker = new SendWorker(server);
    }

    /**
     * Sends the news
     * @param e The send button was clicked
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            sendWorker.execute();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}