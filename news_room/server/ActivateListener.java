package news_room.server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listener for activating a server
 */
public class ActivateListener implements ActionListener {
    private static final String ACTIVE_MSG = "Clients' enrollment is already active!";
    private static final String ERR_TITLE = "ERROR";
    private final Server server;

    /**
     * Create a new listener
     * @param server The server associated with this listener
     */
    public ActivateListener(Server server){
        this.server = server;
    }

    /**
     * The action to perform when triggered
     * @param e The triggering event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (server.getIsEnrollmentOff()){
            server.setEnrollmentOff(false);
            JOptionPane.showMessageDialog(server, Server.getEnrollmentOnMsg(),
                     Server.getEnrollmentTitle(), JOptionPane.INFORMATION_MESSAGE);
            server.run();
        } else{
            JOptionPane.showMessageDialog(server, ACTIVE_MSG, ERR_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }
}