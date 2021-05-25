package news_room.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerSendBtnListener implements ActionListener {
    private final ServerSenderBackground serverSenderBackground;

    public ServerSenderBackground getServerSenderBackground() {
        return serverSenderBackground;
    }

    public ServerSendBtnListener(Server server){
        serverSenderBackground = new ServerSenderBackground(server);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            getServerSenderBackground().doInBackground();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}