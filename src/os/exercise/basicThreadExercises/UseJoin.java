package os.exercise.basicThreadExercises;

public class UseJoin {
    public static void main(String[] args) {
        Count c = new Count();
        c.start();
        try {
            //main thread always waits for c thread
            //to finish executing
            c.join();

            System.out.println("Result = " + c.getResult());
        }
        catch (InterruptedException e) {
            System.out.println("interrupted");
        }
    }
}

class Count extends Thread {
    private long result;

    @Override
    public void run() {
        result = count();
    }

    public long getResult() {
        return result;
    }

    public long count() {
        long r = 0;
        for (r =0; r<1000000; r++);
        return r;
    }
}
