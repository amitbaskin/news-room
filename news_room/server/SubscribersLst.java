package news_room.server;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class SubscribersLst implements Iterable<Subscriber>{
    private final LinkedBlockingQueue<Subscriber> lst;

    public SubscribersLst(){
        lst = new LinkedBlockingQueue<>();
    }

    public boolean isContains(Subscriber subject){
        for (Subscriber sub : lst){
            if (sub.equals(subject)) return true;
        } return false;
    }

    public void add(Subscriber subscriber){
        for (Subscriber sub : lst){
            if (sub.equals(subscriber)) return;
        } lst.add(subscriber);
    }

    public void remove(Subscriber subscriber) {
        for (Subscriber sub : lst) {
            if (sub.equals(subscriber)) lst.remove(subscriber);
        }
    }

    public Iterator<Subscriber> iterator(){
        return lst.iterator();
    }
}