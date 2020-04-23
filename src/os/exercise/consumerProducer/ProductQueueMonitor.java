package os.exercise.consumerProducer;

import java.util.ArrayList;
import java.util.List;

public class ProductQueueMonitor <T>{
    List<T> contents;
    int capacity;

    public ProductQueueMonitor(int capacity) {
        contents = new ArrayList<>();
        this.capacity = capacity;
    }


    public  synchronized void add(T item) throws InterruptedException {

        while (contents.size() == capacity)
            wait();

        if (contents.size() < capacity)
            contents.add(item);

        notifyAll();
    }
    public synchronized T get() throws InterruptedException {
        T item = null;

        while (contents.size() == 0)
            wait();

        item = contents.remove(contents.size() - 1);
        notifyAll();

        return item;
    }
    public  T seek() {
        if (!contents.isEmpty())
            return contents.get(contents.size()-1);
        return null;
    }
}
