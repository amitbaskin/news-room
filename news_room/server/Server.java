package news_room.server;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;

public class Server extends JFrame implements Runnable{
    public static final String FRAME_TITLE = "Server";
    public static final int DEFAULT_MSG_SIZE = 100;
    public static final String SEND_TITLE = "Send";
    public static final String STOP_TITLE = "Stop\nEnrollments";
    public static final String CONTINUE_TITLE = "Activate\nEnrollments";
    public static final String ENROLLMENT_OFF_MSG = "Clients' enrollment is off";
    public static final String ENROLLMENT_ON_MSG = "Clients' enrollment is on";
    public static final String ENROLLMENT_TITLE = "Clients' Enrollment";
    public static final String NOT_ACCEPTING_MSG = "Not accepting new subscribers right now";
    public static final String ALREADY_SUB_MSG = "You are subscribed already";
    public static final String HI_MSG = "Hi";
    public static final String BYE_MSG = "Bye";
    public static final String CONNECT = "CONNECT";
    public static final String DISCONNECT = "DISCONNECT";
    public static final String STOP = "STOP";
    public static final String DEFAULT_TEXT = "";
    public static final int ROWS = 500;
    public static final int COLS = 400;
    private final DatagramSocket readSocket;
    private final DatagramSocket writeNewsSocket;
    private final InetAddress myAddress;
    private final SubscribersLst subscribersLst;
    private final JButton sendBtn;
    private final JButton stopBtn;
    private final JButton continueBtn;
    private final JTextArea newsArea;
    private final ExecutorService executorService;
    private boolean isEnrollmentOff;
    private boolean isOpen;
    private ServerEnrollBackground serverEnrollBackground;
    private final DatagramSocket writeMsgSocket;
    private final int portNum;

    public Server(InetAddress myAddress, int portNum, ExecutorService executorService) throws SocketException {
        super(FRAME_TITLE);
        isOpen = true;
        this.portNum = portNum;
        this.executorService = executorService;
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

    public JTextArea getNewsArea() { return newsArea; }
    public int getPortNum() { return portNum; }
    public JButton getContinueBtn() { return continueBtn; }
    public JButton getSendBtn() { return sendBtn; }
    public JButton getStopBtn() { return stopBtn; }
    public DatagramSocket getReadSocket() { return readSocket; }
    public SubscribersLst getSubscribersLst() { return subscribersLst; }
    public ServerEnrollBackground getServerEnrollBackground() { return serverEnrollBackground; }
    public DatagramSocket getWriteMsgSocket() { return writeMsgSocket; }
    public boolean getIsOpen() { return isOpen; }
    public boolean getIsEnrollmentOff() {
        return isEnrollmentOff;
    }
    public DatagramSocket getWriteNewsSocket() { return writeNewsSocket; }
    public InetAddress getMyAddress() { return myAddress; }
    public ExecutorService getExecutorService() {
        return executorService;
    }
    public void setIsOpen(boolean open) { isOpen = open; }
    public void setEnrollmentOff(boolean enrollmentOff) {
        isEnrollmentOff = enrollmentOff;
    }

    public void initialize(){
        JOptionPane.showMessageDialog(this, ENROLLMENT_OFF_MSG, ENROLLMENT_TITLE,
                JOptionPane.INFORMATION_MESSAGE);
        serverEnrollBackground = new ServerEnrollBackground(this);
        getSendBtn().addActionListener(new ServerSendBtnListener(this));
        getStopBtn().addActionListener(new ServerStopBtnListener(this));
        getContinueBtn().addActionListener(new ServerActivateBtnListener(this));
        addWindowListener(new ServerWindowListener(this));
    }

    public void run(){
        try {
            getServerEnrollBackground().doInBackground();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
            } catch (IOException ignore) {}
        } getReadSocket().close();
        getWriteNewsSocket().close();
        getWriteMsgSocket().close();
        dispose();
    }

    public void sendToAll(String msg) throws IOException {
        for (Subscriber subscriber : getSubscribersLst()) {
            getReadSocket().send(new DatagramPacket(msg.getBytes(), msg.length(), subscriber.getAddress(),
                     subscriber.getPort()));
        }
    }

    public void sendStop() throws IOException {
        getWriteNewsSocket().send(getStopPacket());
    }

    private DatagramPacket getStopPacket(){
        int length = Server.STOP.length();
        return new DatagramPacket(Server.STOP.getBytes(), length, getMyAddress(), getPortNum());
    }
}