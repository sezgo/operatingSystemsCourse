package os.exercise.basicThreadExercises;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UnsafeSequenceTest {
    public static void main(String[] args) throws InterruptedException {

        ArrayList<Thread> sequences = new ArrayList<>(10);

        for (int i=0; i<100; i++) {
            UnsafeSequenceLock us = new UnsafeSequenceLock();
            sequences.add(us);
            us.start();
        }
        for (Thread seq : sequences) {
            //i think  this is a way of syncing
            //seq.start() here then they are executed sequentially
            seq.join();
        }
        System.out.println("UnsafeSequenceLock");
        System.out.println(new UnsafeSequenceLock().getValue());
        //without locks and anything might get
        /*
        UnsafeSequenceTest.main
        99
         */

        sequences.clear();

        for (int i=0; i<100; i++) {
            UnsafeSequenceSync us = new UnsafeSequenceSync();
            sequences.add(us);
            us.start();
        }
        for (Thread seq : sequences) {
            //i think  this is a way of syncing
            //seq.start() here then they are executed sequentially
            seq.join();
        }

        System.out.println("UnsafeSequenceSync");
        System.out.println(new UnsafeSequenceSync().getValue());
    }
}

class UnsafeSequenceLock extends Thread{
    private static int value = 0;
    //lock has to be static.
    private static Lock lock = new ReentrantLock();

    @Override
    public void run() {
        getNext();
    }

    public void getNext() {
        lock.lock();
        value ++;
        lock.unlock();
    }

    public int getValue() {
        return value;
    }
}

class UnsafeSequenceSync extends  Thread {
    private static int value = 0;

    @Override
    public  void run() {
        getNext();
    }
    public synchronized void getNext() {
        this.value++;
    }
    public void getNext2() {
        synchronized (this) {
            this.value++;
        }
    }
    public int getValue(){
        return this.value;
    }
}
