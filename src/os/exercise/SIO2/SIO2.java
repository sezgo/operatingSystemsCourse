package os.exercise.SIO2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class SIO2 {
    public static void main(String[] args) {
        SIO2Test sio2 = new SIO2Test();
        try {
            sio2.simulate();
        }
        catch (InterruptedException e) {
            System.out.println("NOT OK!!");
        }
    }
}

class SIO2Test {

    private static Semaphore si;
    private static Semaphore o;

    private static Semaphore siHere;
    private static Semaphore oHere;

    private static Semaphore ready;

    void init() {
        si = new Semaphore(1);
        o = new Semaphore(2);

        siHere = new Semaphore(0);
        oHere = new Semaphore(0);

        ready = new Semaphore(0);
    }

    void bond(String called) {
        System.out.println("Bonded new SIO2 called " + called);
    }

    class Silicon extends Thread{
        private void proc_si() throws InterruptedException {
            si.acquire();
            siHere.release(2);
            oHere.acquire(2);

            //all the atoms are here
            ready.release(2);
            bond(currentThread().getName());
            si.release();
        }

        @Override
        public void run() {
            try {
                System.out.println("Silicon.run");
                proc_si();
            } catch (InterruptedException e) {}
        }
    }

    class Oxygen extends Thread{
        private void proc_o() throws InterruptedException {
            o.acquire();
            siHere.acquire();
            oHere.release();
            ready.acquire();
            bond(currentThread().getName());
            o.release();
        }

        @Override
        public void run() {
            try {
                System.out.println("Oxygen.run");
                proc_o();
            }catch (InterruptedException e) {}
        }
    }

    void simulate() throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (int i=0; i<3; i++) {
            Silicon s = new Silicon();
            threads.add(s);
            Oxygen o = new Oxygen();
            threads.add(o);
            o = new Oxygen();
            threads.add(o);
        }

        init();

        for (Thread t : threads)
            t.start();

        for (Thread t : threads)
            t.join(100);

        boolean deadlock = false;

        for (Thread t : threads) {
            if(t.isAlive()){
                t.interrupt();
                System.out.println("Deadlock: " + t.getName());
                deadlock = true;
            }
        }
        System.out.println(deadlock ? "" + "" : "Sync done");

    }

}








