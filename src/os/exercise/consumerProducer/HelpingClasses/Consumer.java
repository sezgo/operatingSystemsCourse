package os.exercise.consumerProducer.HelpingClasses;

import os.exercise.consumerProducer.ProductQueue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Consumer extends Thread {

    String name;
    ProductQueue<Product> products;
    public static Lock consuming = new ReentrantLock();

    public Consumer(int id, ProductQueue<Product> products) {
        name = "Consumer-" + id;
        this.products = products;
    }

    public void consume() throws InterruptedException {

        Product p = products.get();
        System.out.println("--" + name + " is getting " + p);

    }

    @Override
    public void run() {
        //System.out.println(name + ".run");
        for (int i = 0; i< 5; i++) {
            try {
                consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

