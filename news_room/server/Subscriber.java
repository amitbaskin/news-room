package news_room.server;

import java.net.InetAddress;

/**
 * A subscriber to a news server
 */
public class Subscriber {
    private final InetAddress address;
    private final int port;

    /**
     * Create a new subscriber
     * @param address the address of this subscriber
     * @param port The port of this subscriber
     */
    public Subscriber(InetAddress address, int port){
        this.address = address;
        this.port = port;
    }

    /**
     * Gets the port
     * @return The port
     */
    public int getPort() {
        return port;
    }

    /**
     * Gets the address
     * @return The address
     */
    public InetAddress getAddress() {
        return address;
    }

    /**
     * Two subscribers are equal if they have the same address and port
     * @param obj The object to compare with
     * @return True iff they are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Subscriber)) return false;
        Subscriber other = (Subscriber) obj;
        return other.getAddress().equals(this.address) && other.getPort() == this.port;
    }
}