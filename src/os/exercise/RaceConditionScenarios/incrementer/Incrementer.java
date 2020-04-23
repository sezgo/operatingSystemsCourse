package os.exercise.RaceConditionScenarios.incrementer;

public class Incrementer {
    private int x;
    public Incrementer(int i) {x=i;}
    public void increment() {x++;}

    private static Incrementer shared = new Incrementer(1);

    static Thread t = new Thread() {
        //no need to protect
        private int threadLocal =0;

        @Override
        public void run() {
            threadLocal++;
            //will be executed by every thread
            //have to protect
            shared.increment();
        }
    };

    public static void main(String[] args) {
        t.start();
    }
}
