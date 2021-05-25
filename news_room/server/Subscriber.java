package news_room.server;

import java.net.InetAddress;

public class Subscriber {
    private final InetAddress address;
    private final int port;

    public Subscriber(InetAddress address, int port){
        this.address = address;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Subscriber)) return false;
        Subscriber other = (Subscriber) obj;
        return other.getAddress().equals(this.address) && other.getPort() == this.port;
    }
}