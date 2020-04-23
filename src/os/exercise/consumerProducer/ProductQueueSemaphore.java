package os.exercise.consumerProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ProductQueueSemaphore<T> {
    List<T> contents;
    int capacity;

    private final Semaphore produce= new Semaphore(10);
    private final Semaphore consume= new Semaphore(0);

    private final Semaphore sync = new Semaphore(1);

    public ProductQueueSemaphore(int capacity) {
        contents = new ArrayList<>();
        this.capacity = capacity;
    }

    public void add(T item) throws InterruptedException {
        produce.acquire();
        sync.acquire();
        while (contents.size() == capacity) {
            sync.release();
            Thread.sleep(10);
            sync.acquire();
        }
        contents.add(item);
        sync.release();
        consume.release();

    }
    public T get() throws InterruptedException {
        T item = null;
        consume.acquire();
        sync.acquire();
        while (contents.size() == 0) {
            sync.release();
            Thread.sleep(10);
            sync.release();
        }
        item = contents.remove(contents.size() - 1);
        sync.release();
        produce.release();

        return item;
    }
    public  T seek() {
        if (!contents.isEmpty())
            return contents.get(contents.size()-1);
        return null;
    }
}
