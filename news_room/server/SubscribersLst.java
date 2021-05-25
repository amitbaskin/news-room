package news_room.server;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A list of subscribers
 */
public class SubscribersLst implements Iterable<Subscriber>{
    private final LinkedBlockingQueue<Subscriber> lst;

    /**
     * Create a new list
     */
    public SubscribersLst(){
        lst = new LinkedBlockingQueue<>();
    }

    /**
     * Checks if given subscriber is contained in this list
     * @param subject The subscriber to check
     * @return True iff the given subscriber is contained in this list
     */
    public boolean isContains(Subscriber subject){
        for (Subscriber sub : lst){
            if (sub.equals(subject)) return true;
        } return false;
    }

    /**
     * Adds a subscriber
     * @param subscriber The subscriber to add
     */
    public void add(Subscriber subscriber){
        for (Subscriber sub : lst){
            if (sub.equals(subscriber)) return;
        } lst.add(subscriber);
    }

    /**
     * Removes a subscriber
     * @param subscriber The subscriber to remove
     */
    public void remove(Subscriber subscriber) {
        for (Subscriber sub : lst) {
            if (sub.equals(subscriber)) lst.remove(subscriber);
        }
    }

    /**
     * The iterator for this list
     * @return The iterator
     */
    @Override
    public Iterator<Subscriber> iterator(){
        return lst.iterator();
    }
}