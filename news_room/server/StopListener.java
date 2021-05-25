package news_room.server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class StopListener implements ActionListener {
    private static final String ACTIVE_MSG = "Clients' enrollment is not activated!";
    private static final String ERR_TITLE = "Clients' enrollment is already active!";
    private final Server server;

    public StopListener(Server server){
        this.server = server;
    }

    public Server getServer() {
        return server;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (getServer().getIsEnrollmentOff()){
            JOptionPane.showMessageDialog(server, ACTIVE_MSG, ERR_TITLE, JOptionPane.ERROR_MESSAGE);
        } else{
            try {
                getServer().sendStop();
                getServer().setEnrollmentOff(true);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}