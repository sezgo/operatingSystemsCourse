package os.exercise.consumerProducer.HelpingClasses;


import os.exercise.consumerProducer.ProductQueue;
import os.exercise.consumerProducer.ProductQueueSemaphore;

public class Producer extends Thread {

    String name;
    ProductQueue<Product> products;

    public Producer(int id, ProductQueue<Product> products) {
        name = "Producer-" + id;
        this.products = products;
    }

    public void produce() throws InterruptedException {
        Product p = new Product();
        products.add(p);
        System.out.println(name + " produced " + p);
    }

    @Override
    public void run() {
        //System.out.println(name + ".run");
        for (int i = 0; i < 5; i++) {
            try {
                produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }
}