package news_room.client;

import news_room.server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Client end class
 */
public class Client extends JFrame {
    private static final String FRAME_TITLE = "Client";
    private static final String SET_BTN = "Set Server";
    private static final String UNSUBSCRIBE = "Unsubscribe";
    private static final String CLEAR = "Clear";
    private static final String DEFAULT_TEXT = "";
    private static final String ERR_TITLE = "ERROR";
    private static final String NOT_SUBSCRIBED_MSG = "You are currently not subscribed!";
    private static final String FROM_MSG = "\nFrom: %s\n";
    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String NEWS_PADDING = "\n\n\n";
    private static final int ROWS = 300;
    private static final int COLS = 400;
    private final DatagramSocket socket;
    private final JTextArea displayArea;
    private final SimpleDateFormat formatter;
    private InetAddress serverAddress;
    private boolean isGetNews;
    private final DatagramPacket receivePacket;
    private NewsGetterWorker newsGetterWorker;
    private final int portNum;

    /**
     * Create new client
     * @param portNum The port number to connect to
     * @throws SocketException In case an error occurs while trying to create a datagram socket
     */
    public Client(int portNum) throws SocketException {
        super(FRAME_TITLE);
        this.portNum = portNum;
        isGetNews = false;
        socket = new DatagramSocket();
        formatter = new SimpleDateFormat(TIME_PATTERN);
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        receivePacket = new DatagramPacket(new byte[Server.getDefaultMsgSize()], Server.getDefaultMsgSize());
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
    }

    /**
     * Get the port number
     * @return The port number
     */
    public int getPortNum() {
        return portNum;
    }

    /**
     * Get the datagram socket
     * @return The datagram socket
     */
    public DatagramSocket getSocket() {
        return socket;
    }

    /**
     * Gets the address of the server
     * @return The address of the server
     */
    public InetAddress getServerAddress() {
        return serverAddress;
    }

    /**
     * Gets the display area of this client
     * @return The display area of this client
     */
    public JTextArea getDisplayArea() {
        return displayArea;
    }

    /**
     * Gets the formatter for displaying the date and time
     * @return The formatter for displaying the date and time
     */
    public SimpleDateFormat getFormatter() {
        return formatter;
    }

    /**
     * Indication for enrollment to the server
     * @return Whether or not this client is subscribed to get the news from the server
     */
    public boolean getIsGetNews() {
        return isGetNews;
    }

    /**
     * Gets the datagram packet in which this client is going to get the news
     * @return The datagram packet in which this client is going to get the news
     */
    public DatagramPacket getReceivePacket() {
        return receivePacket;
    }

    /**
     * Sets the address for the server
     * @param serverAddress The address to set
     */
    public void setServerAddress(InetAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * Sets the enrollment of this client
     * @param isGetNews Whether or not this client is enrolled
     */
    public void setGetNews(boolean isGetNews) {
        this.isGetNews = isGetNews;
    }

    /**
     * Initializes this client
     */
    public void initialize(){
        getUnsubscribedMsg();
        addWindowListener(new ClientWindowsListener(this));
        JButton setServerBtn = new JButton(SET_BTN);
        JButton unsubscribeBtn = new JButton(UNSUBSCRIBE);
        JButton clearBtn = new JButton(CLEAR);
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getDisplayArea().setText(DEFAULT_TEXT);
            }
        }); unsubscribeBtn.addActionListener(new ClientUnsubscribeBtnListener(this));
        JPanel panel = new JPanel();
        panel.add(setServerBtn, BorderLayout.EAST);
        panel.add(unsubscribeBtn, BorderLayout.WEST);
        setServerBtn.addActionListener(new ClientSetBtnListener(this));
        add(panel, BorderLayout.SOUTH);
        add(clearBtn, BorderLayout.NORTH);
        newsGetterWorker = new NewsGetterWorker(this);
        setVisible(true);
        setSize(COLS, ROWS);
    }

    /**
     * Process the clients connection in the background
     */
    public void run() {
        try {
            newsGetterWorker.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the news from the server
     * @throws IOException In case there's a problem getting the news
     */
    public void getNews() throws IOException {
        while(getIsGetNews()) {
            getSocket().receive(getReceivePacket());
            getDisplayArea().append(getFormatter().format(new Date(System.currentTimeMillis())));
            getDisplayArea().append(String.format(FROM_MSG, getReceivePacket().getAddress()));
            getDisplayArea().append(new String(getReceivePacket().getData(), 0,
                     getReceivePacket().getLength()));
            getDisplayArea().append(Client.NEWS_PADDING);
        }
    }

    /**
     * Disconnecting this client from the server
     */
    public void disconnect(){
        setGetNews(false);
        try {
            getSocket().send(getPacket(Server.getDisconnectMsg()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Gets a new datagram packet filled with the input data
     * @param msg The msg to fill in the packet
     * @return A new datagram packet filled with the input data
     */
    DatagramPacket getPacket(String msg){
        return new DatagramPacket(msg.getBytes(), msg.length(), getServerAddress(), getPortNum());
    }

    /**
     * Informs This client that he is not currently subscribed
     */
    public void getUnsubscribedMsg(){
        JOptionPane.showMessageDialog(this, NOT_SUBSCRIBED_MSG, ERR_TITLE, JOptionPane.ERROR_MESSAGE);
    }
}