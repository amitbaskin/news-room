package news_room.server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerActivateBtnListener implements ActionListener {
    private static final String ACTIVE_MSG = "Clients' enrollment is already active!";
    private static final String ERR_TITLE = "ERROR";
    private final Server server;

    public ServerActivateBtnListener(Server server){
        this.server = server;
    }

    public Server getServer() {
        return server;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (getServer().getIsEnrollmentOff()){
            getServer().setEnrollmentOff(false);
            JOptionPane.showMessageDialog(getServer(), Server.ENROLLMENT_ON_MSG, Server.ENROLLMENT_TITLE,
                    JOptionPane.INFORMATION_MESSAGE);
            getServer().getExecutorService().execute(getServer());
        } else{
            JOptionPane.showMessageDialog(server, ACTIVE_MSG, ERR_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }
}