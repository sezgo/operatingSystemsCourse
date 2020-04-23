package os.exercise.RaceConditionScenarios;

public class Composite {
    private Composite c;
    int x;
    public Composite(int b) {x=b;}
    public Composite(Composite a, int b) {
        c=a;
        x=b;
    }
    public void move() {
        if (c!=null) {
            c.move();
            x++;
        }
    }
    private static Composite shared = new Composite(1);

    Thread  t = new Thread() {
        @Override
        public void run() {
            Composite local = new Composite(shared, 1);
            //should be synced because of 'shared'
            local.move();
        }
    };
}
