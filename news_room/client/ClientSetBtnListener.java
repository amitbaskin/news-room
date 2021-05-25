package news_room.client;

import news_room.server.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * A listener button for setting a server for the client to connect to
 */
public class ClientSetBtnListener implements ActionListener {
    private final static String HOST_NAME_REQUEST_MSG = "Please enter a host name:";
    private final static String DEFAULT_HOST = "127.0.0.1";
    private final static String UNKNOWN_HOST_ERR_MSG = "Unknown host!\nHost remains unchanged (default is " +
            "local host)";
    private final static String ERR_TITLE = "ERROR";
    private final Client client;

    /**
     * Creates a new listener
     * @param Client The client associated with this listener
     */
    public ClientSetBtnListener(Client Client){
        this.client = Client;
    }

    /**
     * Gets the client
     * @return The client
     */
    public Client getClient() {
        return client;
    }

    /**
     * Gets the address of the server according to the client's input
     * @return The address of the server according to the client's input
     * @throws UnknownHostException In case the address provided is invalid
     */
    private InetAddress getInputHost() throws UnknownHostException {
        String hostName = JOptionPane.showInputDialog(client, HOST_NAME_REQUEST_MSG, DEFAULT_HOST);
        return InetAddress.getByName(hostName);
    }

    /**
     * The action to perform when the client clicks on the set-server button
     * @param e The event that triggered this listener
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            final InetAddress host = getInputHost();
            getClient().setServerAddress(host);
            getClient().getSocket().send(getClient().getPacket(Server.getConnectMsg()));
            if (!getClient().getIsGetNews()){
                getClient().setGetNews(true);
                getClient().run();
            }
        } catch (UnknownHostException unknownHostException) {
            JOptionPane.showMessageDialog(getClient(), UNKNOWN_HOST_ERR_MSG, ERR_TITLE,
                     JOptionPane.ERROR_MESSAGE );
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}