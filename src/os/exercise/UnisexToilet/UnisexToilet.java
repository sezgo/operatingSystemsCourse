package os.exercise.UnisexToilet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UnisexToilet {
    public static void main(String[] args) {
        try {
            new UnisexToiletTest().simulate();
        }catch (InterruptedException e) {
            System.out.println("NOT OK!");
        }
    }
}

class UnisexToiletTest {

    private static final Semaphore toilet= new Semaphore(1);

    private static final Object mLock = new Object();
    private static final Object wLock = new Object();

    private static int menInside = 0;
    private static int womenInside = 0;

    private class Woman extends Thread {
        @Override
        public void run() {
            try {
                woman_enter(currentThread().getName());
            } catch (InterruptedException e) {}
            woman_exit(currentThread().getName());
        }
    }
    private class Man extends Thread {
        @Override
        public void run() {
            try {
                man_enter(currentThread().getName());
            } catch (InterruptedException e) {}
            man_exit(currentThread().getName());
        }
    }

    private void woman_enter(String name) throws InterruptedException {

        synchronized (wLock) {
            if (womenInside == 0) {
                toilet.acquire();
                System.out.println("----------------- woman inside ------------");

            }
            womenInside++;
            System.out.println("===ENTERING: " + name);
        }

    }
    private synchronized void man_enter(String name) throws InterruptedException {
        synchronized (mLock) {
            if (menInside == 0) {
                toilet.acquire();
                System.out.println("----------------- man inside ------------");
            }
            menInside++;
        }
        System.out.println("===ENTERING: " + name);
    }
    private void woman_exit(String name) {
        synchronized (wLock) {
            System.out.println("===EXITING: " + name);
            womenInside--;
            if (womenInside == 0) {
                toilet.release();
            }

        }
    }
    private void man_exit(String name) {
        synchronized (mLock) {

            System.out.println("===EXITING: " + name);
            menInside--;
            if (menInside == 0)
                toilet.release();
        }
    }


    //semaphores
    void simulate() throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (int i=0; i<15; i++){
            Woman woman = new Woman();
            woman.setName("Woman_" + i);
            threads.add(woman);
        }
        for (int i=0; i<10; i++){
            Man man = new Man();
            man.setName("Man_" + i);
            threads.add(man);
        }

        for (Thread t : threads)
            t.start();

        for (Thread t: threads)
            t.join(100);

        boolean deadlock = false;
        for (Thread t : threads) {
            if (t.isAlive()) {
                t.interrupt();
                deadlock = true;
            }
        }
        System.out.println(deadlock ? "Deadlock!!" : "Sync done!");
    }
}
