package os.exercise.RaceConditionScenarios;

import java.util.HashSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ExampleThread extends Thread {

    //would get a reference to some object, which becomes shared
    private IntegerWrapper wrapper;

    //visible by this thread only and is not shared
    //no need for protection
    private int threadLocalField = 0;

    //can be accessed from other threads and should be protected when used
    public int threadPublishedField = 0;

    public ExampleThread(int init, IntegerWrapper iw) {
        //init is primitive variable and thus is not shared
        threadLocalField = init;
        //this object can be shared, since iw is reference
        this.wrapper = iw;
    }

    private void privateFieldIncrement() {
        //only this thread can access this field
        threadLocalField++;
        //this variable is visible only in this method(not shared)
        int localVar = threadLocalField;
        try {
            //added to force thread switching
            Thread.sleep(30);
        } catch (InterruptedException e) {/* DO NOTHING */}
        //check for race condition! Will it ever occur.?
        if (localVar != threadLocalField)
            System.err.println("private-mismatch-%d" + getId());
        else
            System.out.println(String.format("[private-%d", getId(),threadLocalField));
    }

    private void forceSwitch(int sleepTime) {
        try { //added to force thread switching
            Thread.sleep(sleepTime);
        }
        catch (InterruptedException ex) {}
    }

    public void publicFieldIncrement() {
        //increment the public field, and store it to local var
        int localVar = ++threadPublishedField;
        forceSwitch(10);
        //check for race condition! Wil it ever occur
        if (localVar != threadPublishedField)
            System.err.println("public-mismatch-%d" + getId());
        else
            System.out.println(String.format("[public-%d", getId(),threadPublishedField));
    }

    /*
    The lock and the semaphore instances are different for each thread!!!

    public Lock lock = new ReentrantLock();
    public Semaphore binarySemaphore = new Semaphore(1);

    */

    public static Lock lock = new ReentrantLock();
    public static Semaphore binarySemaphore = new Semaphore(1);

    public void publicFieldSafeIncrement() {
        //using 'this' would make this class the monitor
        //which would be different for each thread
        //so something that is same for all threads should be used

        /*
        synchronized (wrapper) {
            publicFieldIncrement();
        }
        //or

        lock.lock();
        publicFieldIncrement();
        lock.unlock();
        //or
*/
        try {
            binarySemaphore.acquire();
            publicFieldIncrement();
        } catch (InterruptedException e) {
        }finally {
            binarySemaphore.release();
        }
    }

    public void wrapperIncrement() {
        //increment the shared variable
        wrapper.increment();
        int localVar = wrapper.getVal();
        forceSwitch(3);
        //check for race condition! Wil it ever occur
        if (localVar != wrapper.getVal())
            System.err.println("wrapper-mismatch-%d" + getId());
        else
            System.out.println(String.format("[wrapper-%d]", getId(),wrapper.getVal()));
    }

    private final static Semaphore x = new Semaphore(1);
    private final static Lock wrapperLock = new ReentrantLock();

    public void wrapperSafeIncrement() {
        /*
        synchronized (wrapper) {
            wrapperIncrement();
        }
        //or

        wrapperLock.lock();
        wrapperIncrement();
        wrapperLock.unlock();
        //or
*/
        try {
            x.acquire();
            wrapperIncrement();
        }
        catch (InterruptedException e) {}
        finally {
            x.release();
        }
    }

    @Override
    public void run() {
        privateFieldIncrement();
        publicFieldSafeIncrement();
        wrapperSafeIncrement();
    }
}
class IntegerWrapper {
    private int val = 0;
    public void increment() {val++;}
    public int getVal() {return  val;}
}

public class ExampleThreadTest {
    public static void main(String[] args) {
        HashSet<ExampleThread> threads = new HashSet<>();
        IntegerWrapper sharedWrapper = new IntegerWrapper();

        //shuffle the threads using HashSet
        for (int i = 0; i < 100; i++) {
            ExampleThread t = new ExampleThread(0, sharedWrapper);
            threads.add(t);
        }
        for (Thread t : threads)
            t.start();      //execute in background

        for (ExampleThread t : threads) {
            t.publicFieldSafeIncrement();
            t.wrapperSafeIncrement();
        }

        //when invoking methods:
        //values of the arguments from primitive types are copied
        //into local variables;
        //arguments that are not primitive are passed as references,
        //and thus the actual object is shared among the threads;


    }
}