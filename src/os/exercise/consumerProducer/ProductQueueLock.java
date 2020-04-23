package os.exercise.consumerProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProductQueueLock<T> {
    List<T> contents;
    int capacity;

    private Lock lock = new ReentrantLock();

    public ProductQueueLock(int capacity) {
        contents = new ArrayList<>();
        this.capacity = capacity;
    }

    public void add(T item) throws InterruptedException {
        while(true) {
            lock.lock();
            if (contents.size() < capacity) {
                contents.add(item);
                lock.unlock();
                break;
            }
            lock.unlock();
        }
    }
    public T get() throws InterruptedException {
        T item = null;

        while (true) {
            lock.lock();
            if (contents.size() > 0) {
                item = contents.remove(contents.size() - 1);
                lock.unlock();
                break;
            }
            lock.unlock();
        }

        return item;
    }
    public  T seek() {
        if (!contents.isEmpty())
            return contents.get(contents.size()-1);
        return null;
    }
}
