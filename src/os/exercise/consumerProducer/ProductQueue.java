package os.exercise.consumerProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ProductQueue<T> {
    List<T> contents;
    int capacity;

    public ProductQueue(int capacity) {
        contents = new ArrayList<>();
        this.capacity = capacity;
    }

    public void add(T item) throws InterruptedException {

        contents.add(item);

    }
    public T get() throws InterruptedException {
        T item = null;

        item = contents.remove(contents.size() - 1);

        return item;
    }
    public  T seek() {
        if (!contents.isEmpty())
            return contents.get(contents.size()-1);
        return null;
    }
}
