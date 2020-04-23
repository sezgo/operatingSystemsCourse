package os.exercise.consumerProducer;

import os.exercise.consumerProducer.HelpingClasses.Consumer;
import os.exercise.consumerProducer.HelpingClasses.Producer;
import os.exercise.consumerProducer.HelpingClasses.Product;

import java.util.ArrayList;


public class ConsumerProducerTest extends Thread {



    public static void main(String[] args) {


        ArrayList<Thread> threads = new ArrayList<>();
        //this is the shared resource
        ProductQueue<Product> products = new ProductQueue<>(10);

        for (int i = 0; i < 100; i++) {
            Consumer cons = new Consumer(i, products);
            Producer prod = new Producer(i, products);
            threads.add(cons);
            threads.add(prod);
            cons.start();
            prod.start();
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("ConsumerProducer.run");
        //for (Product p : products)
            //System.out.println(p);
        boolean deadlock = false;
        for (Thread t: threads){
            if (t.isAlive()){
                System.out.println(t.getName() + "is still alive");
                deadlock = true;
            }
        }

        System.out.println(deadlock ? "DeadLock!!!" : "Sync is over");
    }
}

