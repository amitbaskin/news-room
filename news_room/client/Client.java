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
import java.util.concurrent.ExecutorService;

/**
 * Client end class
 */
public class Client extends JFrame implements Runnable {
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
    private final ExecutorService executorService;
    private final DatagramPacket receivePacket;
    private final int portNum;

    /**
     * Create new client
     * @param portNum The port number to connect to
     * @param executorService The executor service to process this client's connection in the background
     * @throws SocketException
     */
    public Client(int portNum, ExecutorService executorService) throws SocketException {
        super(FRAME_TITLE);
        this.portNum = portNum;
        this.executorService = executorService;
        isGetNews = false;
        socket = new DatagramSocket();
        formatter = new SimpleDateFormat(TIME_PATTERN);
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        receivePacket = new DatagramPacket(new byte[Server.DEFAULT_MSG_SIZE], Server.DEFAULT_MSG_SIZE);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
    public int getPortNum() { return portNum; }
    public DatagramSocket getSocket() {
        return socket;
    }
    public InetAddress getServerAddress() {
        return serverAddress;
    }
    public JTextArea getDisplayArea() { return displayArea; }
    public SimpleDateFormat getFormatter() { return formatter; }
    public boolean getIsGetNews() {
        return isGetNews;
    }
    public DatagramPacket getReceivePacket() {
        return receivePacket;
    }
    public void setServerAddress(InetAddress serverAddress) {
        this.serverAddress = serverAddress;
    }
    public void setGetNews(boolean getNews) {
        isGetNews = getNews;
    }

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
        setVisible(true);
        setSize(COLS, ROWS);
    }

    @Override
    public void run() {
        try {
//            new ClientWorker(this).doInBackground();
            getNews();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public void disconnect(){
        setGetNews(false);
        try {
            getSocket().send(getPacket(Server.DISCONNECT));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    DatagramPacket getPacket(String msg){
        return new DatagramPacket(msg.getBytes(), msg.length(), getServerAddress(), getPortNum());
    }

    public void getUnsubscribedMsg(){
        JOptionPane.showMessageDialog(this, NOT_SUBSCRIBED_MSG, ERR_TITLE, JOptionPane.ERROR_MESSAGE);
    }
}