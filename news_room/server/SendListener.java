package news_room.server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A listener for sending news
 */
public class SendListener implements ActionListener {
    Server server;
    /**
     * Create a new listener
     * @param server The server associated with this listener
     */
    public SendListener(Server server){
        this.server = server;
    }

    /**
     * Sends the news
     * @param e The send button was clicked
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            new SwingWorker<Object, Object>(){

                @Override
                protected Object doInBackground() throws Exception {
                    server.sendNews();
                    return null;
                }
            }.execute();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}