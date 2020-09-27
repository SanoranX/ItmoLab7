package server.collection;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Routes extends LinkedHashSet<Route> {
    private String path;
    public ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    public Lock readLock = readWriteLock.writeLock();
    public Lock writeLock = readWriteLock.writeLock();

    public Routes() {

    }

    public void removeRoute(long id) {
        Route[] array = this.toArray(new Route[0]);
        List<Route> list = new ArrayList<>(Arrays.asList(array));

        for (Route p : list) {
            if (p.getId() == id) {
                list.remove(p);
                this.clear();
                this.addAll(list);
                return;
            }
        }
    }

    public boolean checkExistence(int id) {
        for (Route route : this) {
            if (route.getId() == id)
                return true;
        }
        return false;
    }

    public void removeWithId(int id){
        for(Route route : this){
            if (route.getId() == id){
                this.remove(route);
            }
        }
    }

}
