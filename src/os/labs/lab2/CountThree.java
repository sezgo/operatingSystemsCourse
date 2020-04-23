package os.labs.lab2;

import javax.swing.text.StyleContext;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

/*

    Со помош на синхронизациските методи да се реши проблемот за определување на бројот на појавувања на бројот 3 во огромна низа и негово запишување во глобална променлива count.

Секвенцијалното решение не е прифатливо поради тоа што трае многу долго време (поради големината на низата). За таа цел, потребно е да се паралелизира овој процес, при што треба да се напише метода која ќе ги брои појавувањата на бројот 3 во помал фрагмент од низата, при што резултатот повторно се чува во глобалната заедничка променлива count.

Напомена: Почетниот код е даден во почетниот код CountThree. Задачата да се тестира над низа од минимум 1000 елементи.


-----

Solve the issue of detecting the number of occurrences of the number 3 in a large array by using thread synchronization methods. The counts are written / incremented in the global variable count on each find.

The standard sequential solution is not acceptable as it takes a long time (because the array is very large). Therefore, you need to implement this process and write a method which will count the occurrences of 3 in smaller fragments of the array, while the result is still kept in the global count variable.

Note: The starting code for the solutions is given in CountThree. You need to test it with an array of at least 1.000 elements.

*/
public class CountThree {

    public static int NUM_RUNS = 100;
    /**
     * Promenlivata koja treba da go sodrzi brojot na pojavuvanja na elementot 3
     */
    int count = 0;
/**
 * TODO: definirajte gi potrebnite elementi za sinhronizacija
 */ public static Semaphore semaphore;

    public void init() {
        semaphore = new Semaphore(1);
    }

    class Counter extends Thread {

        public void count(int[] data) throws InterruptedException {
            // da se implementira
            for (int i=0; i<data.length; i++) {
                if (data[i] == 3) {
                    CountThree.semaphore.acquire();
                    count++;
                    CountThree.semaphore.release();
                }
            }
        }
        private int[] data;

        public Counter(int[] data) {
            this.data = data;
        }

        @Override
        public void run() {
            try {
                count(data);
            } catch (InterruptedException e) {
                CountThree.semaphore.release();
            }
        }
    }

    public static void main(String[] args) {
        try {
            CountThree environment = new CountThree();
            environment.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void start() throws Exception {

        init();

        HashSet<Thread> threads = new HashSet<Thread>();
        Scanner s = new Scanner(System.in);
        int total=s.nextInt();

        for (int i = 0; i < NUM_RUNS; i++) {
            int[] data = new int[total];
            for (int j = 0; j < total; j++) {
                data[j] = s.nextInt();
            }
            Counter c = new Counter(data);
            threads.add(c);
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }
        System.out.println(count);


    }
}

