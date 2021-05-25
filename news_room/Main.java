package news_room;

import news_room.client.Client;
import news_room.server.Server;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Main {
    private static final int CLIENTS_AMOUNT = 3;
    private static final int PORT_NUM = 7777;

    public static void main(String[] args) {
        run();
    }

    private static void run() {
        try{
            runServer();
            for (int i = 0; i< CLIENTS_AMOUNT; i++){
                runNewClient();
            }
        } catch (SocketException | UnknownHostException exception) {
            exception.printStackTrace();
        }
    }

    private static void runServer() throws UnknownHostException,
            SocketException {
        Server server = new Server(InetAddress.getLocalHost(), PORT_NUM);
        server.initialize();
        server.run();
    }

    private static void runNewClient() throws SocketException,
            UnknownHostException {
        Client client = new Client(PORT_NUM);
        client.initialize();
    }
}