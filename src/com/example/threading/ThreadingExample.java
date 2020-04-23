package com.example.threading;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ThreadA extends Thread{

    private Counter counter;

    public ThreadA(Counter counter) {
        this.counter = counter;
    }
    public void run() {
        try {
            counter.decrement();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Counter {
    private int count = 0;

    private Lock lock = new ReentrantLock();
    //lock is a mutex

    private Semaphore semaphore = new Semaphore(2);
    //with permits 0 its initially a deadlock situation
    //with permits 1 its initially a mutex

    //public synchronized void increment() { lock.lock() here
        //count++
    //} lock.unlock() here

    //but with synchronized block i can not unlock the lock outside of the block

    public void increment() {
        //this way i can unlock it in other methods
        lock.lock();
        count++;
        lock.unlock();
    }
    public void decrement() throws InterruptedException {
        //lock.lock();
        semaphore.acquire();
        count--;
        semaphore.release();
        //lock.unlock();
    }
    public int getCount() {
        return this.count;
    }
}

public class ThreadingExample {

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        ThreadA t1 = new ThreadA(counter);
        ThreadA t2 = new ThreadA(counter);
        t1.run();
        t2.run();
        System.out.println(counter.getCount());
    }
}

//blocking queye ??


// SYNC Mechanisms: Monitor(synchronized, wait, notify, notifyAll), Lock, Semaphore
/*
montior has a mutex inside
 */
