package os.labs.lab3;


//Multiple threads are trying to access the same Singleton instance. However,
//you need to synchronize the creation of the singleton instance, in a way that only one instance would exist.

public class Singleton {

    private static volatile Singleton singleton;

    private Singleton() {

    }

    public static Singleton getInstance() {
        // TODO: 3/29/20 Synchronize this
        singleton = new Singleton();

        return singleton;
    }

    public static void main(String[] args) {
        // TODO: 3/29/20 Simulate the scenario when multiple threads call the method getInstance
    }

}