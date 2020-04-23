package os.exercise;

import java.util.Arrays;

public class ThreadBasicTest {
    public static void main(String[] args) throws InterruptedException {
        ThreadA1 ta = new ThreadA1();
        ThreadB1 tb = new ThreadB1();

        ta.start();
        tb.start();

        //tb.join() if written, main thread
        //waits here for tb to finish
        //then executes the upcoming instruction
        System.out.println("Main done");

    }
}

class ThreadA1 extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 20; i++) {
            System.out.println("A " + i);
        }
        System.out.println("A done");

    }

}

class ThreadB1 extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 20; i++) {
            System.out.println("\t\tB " + i);
        }
        System.out.println("B done");
    }
}

