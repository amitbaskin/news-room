package news_room.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerSendBtnListener implements ActionListener {
//    private Server server;
    private final ServerSenderBackground serverSenderBackground;

    public ServerSenderBackground getServerSenderBackground() {
        return serverSenderBackground;
    }

    public ServerSendBtnListener(Server server){
//        this.server = server;
        serverSenderBackground = new ServerSenderBackground(server);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            getServerSenderBackground().doInBackground();
//            server.send();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}