package os.labs.lab2;

public class osLab2_1 {
    public static void main(String[] args) {
        //new ThreadAB("A", "B").start();
        //new ThreadAB("1", "2").start();

        new ThreadA().start();
        new ThreadB().start();
    }

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
        public void run() {
            System.out.println("Thread A starts");
            for (int i = 0; i < 26; i++) {
                System.out.println(i);
            }
            System.out.println("Thread A is done");
        }
    }

    static class ThreadB extends Thread {
        public void run() {
            System.out.println("Thread B starts");
            for (int i = 0; i < 26; i++) {
                System.out.println((char) ('A' + i));
            }
            System.out.println("Thread B is done");
        }
    }

}