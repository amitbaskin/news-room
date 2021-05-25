package news_room.client;

import news_room.server.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientSetBtnListener implements ActionListener {
    private final static String HOST_NAME_REQUEST_MSG = "Please enter a host name:";
    private final static String DEFAULT_HOST = "127.0.0.1";
    private final static String UNKNOWN_HOST_ERR_MSG = "Unknown host!\nHost remains unchanged (default is " +
            "local host)";
    private final static String ERR_TITLE = "ERROR";
    private final Client client;

    public ClientSetBtnListener(Client cLient){
        this.client = cLient;
    }

    public Client getClient() {
        return client;
    }

    private InetAddress getInputHost() throws UnknownHostException {
        String hostName = JOptionPane.showInputDialog(client, HOST_NAME_REQUEST_MSG, DEFAULT_HOST);
        return InetAddress.getByName(hostName);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            final InetAddress host = getInputHost();
            getClient().setServerAddress(host);
            getClient().getSocket().send(getClient().getPacket(Server.CONNECT));
            if (!getClient().getIsGetNews()){
                getClient().setGetNews(true);
                getClient().getExecutorService().execute(getClient());
            }
        } catch (UnknownHostException unknownHostException) {
            JOptionPane.showMessageDialog(getClient(), UNKNOWN_HOST_ERR_MSG, ERR_TITLE,
                     JOptionPane.ERROR_MESSAGE );
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}