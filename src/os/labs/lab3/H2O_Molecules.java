package os.labs.lab3;

//Synchronize a machine that creates H2O molecules.

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class H2OMachine {

    private static Semaphore hHere = new Semaphore(2);
    private static Semaphore oHere = new Semaphore(1);
    private static Semaphore canBond = new Semaphore(0);
    private static Semaphore canExit = new Semaphore(0);
    String[] molecule;
    int count;
    Lock lock = new ReentrantLock();

    public H2OMachine() {
        molecule = new String[3];
        count = 0;
    }

    public void hydrogen() throws InterruptedException {
        // TODO: 3/29/20 synchronized logic here
        hHere.acquire();
        lock.lock();
        count++;
        if (count == 3) {
            canBond.release(3);
            lock.unlock();
        } else {
            lock.unlock();
        }

        canBond.acquire();
        System.out.println("The molecule is formed");

        lock.lock();
        count--;
        if (count == 0) {
            canExit.release();
            lock.unlock();
        } else
            lock.unlock();

        canExit.acquire();
        hHere.release();
    }

    public void oxygen() throws InterruptedException {
        // TODO: 3/29/20 synchronized logic here
        oHere.acquire();
        lock.lock();
        count++;
        if (count == 3) {
            canBond.release(3);
            lock.unlock();
        } else {
            lock.unlock();
        }

        canBond.acquire();
        System.out.println("The molecule is formed");

        lock.lock();
        count--;
        if (count == 0) {
            canExit.release();
            lock.unlock();
        } else
            lock.unlock();

        canExit.acquire();
        oHere.release();
    }
}

class H2OThread extends Thread {

    H2OMachine molecule;
    String atom;

    public H2OThread(H2OMachine molecule, String atom) {
        this.molecule = molecule;
        this.atom = atom;
    }

    public void run() {
        if ("H".equals(atom)) {
            try {
                molecule.hydrogen();
            } catch (Exception e) {
            }
        } else if ("O".equals(atom)) {
            try {
                molecule.oxygen();
            } catch (Exception e) {
            }
        }
    }
}

public class H2O_Molecules {
    public static void main(String[] args) {

        // TODO: 3/29/20 Simulate with multiple scenarios
        H2OMachine molecule = new H2OMachine();

        Thread t1 = new H2OThread(molecule, "H");
        Thread t2 = new H2OThread(molecule, "O");
        Thread t3 = new H2OThread(molecule, "H");
        Thread t4 = new H2OThread(molecule, "O");

        t2.start();
        t1.start();
        t4.start();
        t3.start();
    }
}
