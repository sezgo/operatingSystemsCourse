package os.labs.lab2;

/*

Извршете го примерот од TwoThreads.java. Потоа, модифицирајте ја програмата така што ќе користите само една класа за нитки, ThreadAB. Во конструкторот на класата ќе се предадат двата стринга кои соодветната инстанца треба да ги отпечати. Нитката не треба да ја наследува класата Thread. Однесувањето на новата програма треба да биде исто како на оригиналната, односно повторно треба да имате две нитки кои ќе го извршуваат посебно методот run(): едната нитка ќе печати A и B, додека другата ќе печати 1 и 2.

Што би се случило доколку едната нитка треба да ја испечати целата азбука, а другата нитка броевите од 1 до 26? Дали можете да го предвидите изгледот на излезот од програмата?

-------



Execute the TwoThreads.java example. Then modify the program so that it uses only one thread class, ThreadAB. In the constructor of the class you should pass the two strings which should be printed by the corresponding instance. The thread should not inherit from the Thread class. The new program should behave the same way as the original one, i.e. you must have two threads again which will separately execute the run() method: one thread will print A and B, while the other one will print 1 and 2.

What would happen if one thread needs to print out the entire alphabet, and the other the numbers from 1 to 26? Can you predict the program output correctly?


public class TwoThreads {
    public static class Thread1 extends Thread {
        public void run() {
            System.out.println("A");
            System.out.println("B");
        }
    }

    public static class Thread2 extends Thread {
        public void run() {
            System.out.println("1");
            System.out.println("2");
        }
    }

    public static void main(String[] args) {
        new Thread1().start();
        new Thread2().start();
    }

}

 */


// What would happen if one thread needs to print out the entire alphabet, and the other the numbers from 1 to 26? Can you predict the program output correctly?
// In that case, we can not predict the output of the program because alphabet thread may start and before it is done B as well maybe let to run by the schedular,
// which may change the output every time.

public class TwoThreads {
    static class ThreadAB extends Thread {
        String str1;
        String str2;

        public ThreadAB(String str1, String str2) {
            this.str1 = str1;
            this.str2 = str2;
        }

        public void run() {
            System.out.println(str1);
            System.out.println(str2);
        }
    }

    static class ThreadA extends Thread {
        public void run(){
            System.out.println("Thread A starts");
            for(int i=0; i<26; i++){
                System.out.println(i);
            }
            System.out.println("Thread A is done");
        }
    }
    static class ThreadB extends Thread {
        public void run(){
            System.out.println("Thread B starts");
            for(int i=0; i<26; i++){
                System.out.println((char) ('A' + i));
            }
            System.out.println("Thread B is done");
        }
    }

    public static void main(String[] args) {
        new ThreadAB("A", "B").start();
        new ThreadAB("1", "2").start();

        //new ThreadA().start();
        //new ThreadB().start();
    }
}
