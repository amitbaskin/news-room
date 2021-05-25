package news_room.server;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;

/**
 * A server distributing news
 */
public class Server extends JFrame {
    private static final int DEFAULT_MSG_SIZE = 100;
    private static final String CONNECT = "CONNECT";
    private static final String DISCONNECT = "DISCONNECT";
    private static final String STOP = "STOP";
    private static final String ENROLLMENT_ON_MSG = "Clients' enrollment is on";
    private static final String ENROLLMENT_TITLE = "Clients' Enrollment";
    private static final String FRAME_TITLE = "Server";
    private static final String SEND_TITLE = "Send";
    private static final String STOP_TITLE = "Stop\nEnrollments";
    private static final String CONTINUE_TITLE = "Activate\nEnrollments";
    private static final String ENROLLMENT_OFF_MSG = "Clients' enrollment is off";
    private static final String NOT_ACCEPTING_MSG = "Not accepting new subscribers right now";
    private static final String ALREADY_SUB_MSG = "You are subscribed already";
    private static final String HI_MSG = "Hi";
    private static final String BYE_MSG = "Bye";
    private static final String DEFAULT_TEXT = "";
    private static final int ROWS = 500;
    private static final int COLS = 400;
    private final DatagramSocket readSocket;
    private final DatagramSocket writeNewsSocket;
    private final InetAddress myAddress;
    private final SubscribersLst subscribersLst;
    private final JButton sendBtn;
    private final JButton stopBtn;
    private final JButton continueBtn;
    private final JTextArea newsArea;
    private boolean isEnrollmentOff;
    private boolean isOpen;
    private final DatagramSocket writeMsgSocket;
    private final int portNum;

    /**
     * Creating a new server
     * @param myAddress The address
     * @param portNum The port number
     * @throws SocketException In case there's a problem opening a datagram-socket
     */
    public Server(InetAddress myAddress, int portNum) throws SocketException {
        super(FRAME_TITLE);
        isOpen = true;
        this.portNum = portNum;
        readSocket = new DatagramSocket(portNum);
        writeNewsSocket = new DatagramSocket();
        writeMsgSocket = new DatagramSocket();
        this.myAddress = myAddress;
        isEnrollmentOff = true;
        subscribersLst = new SubscribersLst();
        newsArea = new JTextArea();
        newsArea.setEditable(true);
        add(new JScrollPane(newsArea), BorderLayout.CENTER);
        sendBtn = new JButton(SEND_TITLE);
        add(sendBtn, BorderLayout.SOUTH);
        JPanel enrollmentPanel = new JPanel();
        stopBtn = new JButton(STOP_TITLE);
        continueBtn = new JButton(CONTINUE_TITLE);
        enrollmentPanel.add(stopBtn, BorderLayout.WEST);
        enrollmentPanel.add(continueBtn, BorderLayout.EAST);
        add(enrollmentPanel, BorderLayout.NORTH);
        setSize(COLS, ROWS);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Gets the default size of a message
     * @return The default size of a message
     */
    public static int getDefaultMsgSize(){
        return DEFAULT_MSG_SIZE;
    }

    /**
     * Gets the message signaling connection request
     * @return The message signaling connection request
     */
    public static String getConnectMsg(){
        return CONNECT;
    }

    /**
     * Gets the message signaling disconnection request
     * @return The message
     */
    public static String getDisconnectMsg(){
        return DISCONNECT;
    }

    /**
     * Gets the message notifying the server that the enrollments are on
     * @return The message
     */
    static String getEnrollmentOnMsg(){
        return ENROLLMENT_ON_MSG;
    }

    /**
     * gets the title to give to the enrollments message
     * @return The title
     */
    static String getEnrollmentTitle(){
        return ENROLLMENT_TITLE;
    }

    /**
     * Gets the area where the server can write the news to distribute
     * @return The area
     */
    JTextArea getNewsArea() {
        return newsArea;
    }

    /**
     * Gets the port number
     * @return The port number
     */
    int getPortNum() {
        return portNum;
    }

    /**
     * Gets the continue button
     * @return The button
     */
    JButton getContinueBtn() {
        return continueBtn;
    }

    /**
     * Gets the send button
     * @return The button
     */
    JButton getSendBtn() {
        return sendBtn;
    }

    /**
     * Gets the stop button
     * @return The button
     */
    JButton getStopBtn() {
        return stopBtn;
    }

    /**
     * Gets the read socket
     * @return The socket
     */
    DatagramSocket getReadSocket() {
        return readSocket;
    }

    /**
     * Get the subscribers list
     * @return The list
     */
    SubscribersLst getSubscribersLst() {
        return subscribersLst;
    }

    /**
     * Gets the write messages socket
     * @return The socket
     */
    DatagramSocket getWriteMsgSocket() {
        return writeMsgSocket;
    }

    /**
     * Gets whether or not this server is open for connections
     * @return True iff this server is open for connections
     */
    boolean getIsOpen() {
        return isOpen;
    }

    /**
     * Gets whether or not this server is closed for enrollments
     * @return True iff this server is closed for enrollments
     */
    boolean getIsEnrollmentOff() {
        return isEnrollmentOff;
    }

    /**
     * Gets the write-news-socket
     * @return The socket
     */
    DatagramSocket getWriteNewsSocket() {
        return writeNewsSocket;
    }

    /**
     * Gets this server's address
     * @return The address
     */
    InetAddress getMyAddress() {
        return myAddress;
    }

    /**
     * Sets this server as closed
     */
    void setClosed() {
        isOpen = false;
    }

    /**
     * Sets the status of the receiving enrollments
     * @param isEnrollmentOff The status to set
     */
    void setEnrollmentOff(boolean isEnrollmentOff) {
        this.isEnrollmentOff = isEnrollmentOff;
    }

    /**
     * Initializes this server
     */
    public void initialize(){
        JOptionPane.showMessageDialog(this, ENROLLMENT_OFF_MSG, ENROLLMENT_TITLE,
                JOptionPane.INFORMATION_MESSAGE);
        getSendBtn().addActionListener(new SendListener(this));
        getStopBtn().addActionListener(new StopListener(this));
        getContinueBtn().addActionListener(new ActivateListener(this));
        addWindowListener(new WindowListener(this));
    }

    /**
     * Runs this server in the background
     */
    public void run(){
        new SwingWorker<Object, Object>(){
            @Override
            protected Object doInBackground() {
                enrollClients();
                return null;
            }
        }.execute();
    }

    /**
     * Enrolls clients
     */
    public void enrollClients(){
        while(getIsOpen()){
            try{
                byte[] data = new byte[DEFAULT_MSG_SIZE];
                DatagramPacket receivePacket = new DatagramPacket(data, data.length);
                getReadSocket().receive(receivePacket);
                String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());
                switch (msg) {
                    case STOP:
                        JOptionPane.showMessageDialog(this, ENROLLMENT_OFF_MSG, ENROLLMENT_TITLE,
                                JOptionPane.INFORMATION_MESSAGE);
                        setEnrollmentOff(true);
                        break;
                    case CONNECT:
                        Subscriber curSub = new Subscriber(receivePacket.getAddress(),
                                 receivePacket.getPort());
                        if (subscribersLst.isContains(curSub)){
                            getWriteMsgSocket().send(new DatagramPacket(ALREADY_SUB_MSG.getBytes(),
                                    ALREADY_SUB_MSG.length(), receivePacket.getAddress(),
                                    receivePacket.getPort()));
                            break;
                        }
                        if (getIsEnrollmentOff() && getIsOpen()) {
                            getWriteMsgSocket().send(new DatagramPacket(NOT_ACCEPTING_MSG.getBytes(),
                                    NOT_ACCEPTING_MSG.length(), receivePacket.getAddress(),
                                    receivePacket.getPort()));
                            break;
                        } getSubscribersLst().add(new Subscriber(receivePacket.getAddress(),
                                receivePacket.getPort()));
                        getWriteMsgSocket().send(new DatagramPacket(HI_MSG.getBytes(), HI_MSG.length(),
                                receivePacket.getAddress(), receivePacket.getPort()));
                        break;
                    case DISCONNECT:
                        getWriteMsgSocket().send(new DatagramPacket(BYE_MSG.getBytes(), BYE_MSG.length(),
                                receivePacket.getAddress(), receivePacket.getPort()));
                        getSubscribersLst().remove(new Subscriber(receivePacket.getAddress(),
                                receivePacket.getPort()));
                        break;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } getReadSocket().close();
        getWriteNewsSocket().close();
        getWriteMsgSocket().close();
        dispose();
    }

    /**
     * Sends the news
     * @throws IOException In case an error occurs while trying to send the news
     */
    public void sendNews() throws IOException {
        String news = getNewsArea().getText();
        sendToAll(news);
        getNewsArea().setText(Server.DEFAULT_TEXT);
    }

    /**
     * Sends a message to all subscribers
     * @param msg The message to send
     * @throws IOException In case an error occurs while trying to send the message
     */
    public void sendToAll(String msg) throws IOException {
        for (Subscriber subscriber : getSubscribersLst()) {
            getReadSocket().send(new DatagramPacket(msg.getBytes(), msg.length(), subscriber.getAddress(),
                     subscriber.getPort()));
        }
    }

    /**
     * Tells the background process of this server to stop
     * @throws IOException In case there's a problem sending the message to the background process
     */
    public void sendStop() throws IOException {
        getWriteNewsSocket().send(getStopPacket());
    }

    /**
     * Gets the packet to transmit a stop message to the background message enrolling clients for this server
     * @return The packet
     */
    private DatagramPacket getStopPacket(){
        int length = Server.STOP.length();
        return new DatagramPacket(Server.STOP.getBytes(), length, getMyAddress(), getPortNum());
    }
}