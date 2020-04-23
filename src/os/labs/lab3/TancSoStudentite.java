package os.labs.lab3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

/*
You are hired by the show „Dancing with the students“, in order to implement a software solution which will automatically control the gates which guide the contestants.

        The entry gate leads directly to one of the changing rooms, where the contestants should change their clothes. You can have at most 10 contestants in each changing room, at the same time.

        After they change, the contestants wait for a random partner from the opposite gender, after which they exit the changing room and enter the dancing room. The dancing room can have a maximum of three pairs dancing at the same time (in parallel). After they finish dancing, the pairs exit the dancing room leaving space for a new pair to enter and start dancing.

        In the starter code, you have the definition of two classes, Masko (Male) and Zensko (Female), which represent contestants from the corresponding gender. Each of the classes has multiple running instances, and each instance can dance only once.

        In your implementation, you can use the following methods from show:

        show.presobleci()
        Represents a contestant entering in the changing room and changing
        Cannot be called in parallel by more than 10 contestants from the same gender
        show.tancuvaj()
        Represents the start of the dance
        Can only be called by male contestants, as they lead the dance
        The male contestant should make sure that a female contestant is present as his partner before he makes the call
        There cannot be more than 3 parallel calls on this method
        These methods manipulate with a shared resource and are not atomic.

        Your task is to implement the methods Masko.ucestvo(), Zensko.ucestvo() and init(). In your implementation, you can not add try-catch blocks in them. You should define the necessary semaphores, global variables and state variables.

        If you have an error, you will get this message:

        The process in not synchronized according to the conditions in the task

        After that you will see the log of actions and errors. Use this log to see what went wrong,

        Note: Due to concurrent execution of the logging, it is possible that some of the messages in the log are not in the position they are supposed to be. Therefore, use them only as guideline information, do not base all of the conclusions on them.

*/
public class TancSoStudentite {
    public static final Random RANDOM = new Random();
    public static final int RANDOM_RANGE = 3;
    // Konstanti
    public static int BROJ_INSTANCI = 1000;
    // Instanca od bafferot
    public Show show;
    public boolean hasException = false;
    //TODO: Definicija na globalni promenlivi i semafori
    Semaphore changingRoomLady;
    Semaphore changingRoomGent;
    Semaphore dancingRoom;
    Semaphore ladyCanDance;

    public static void main(String[] args) {
        try {
            TancSoStudentite environment = new TancSoStudentite();
            environment.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void init() {
        //TODO: da se implementira
        //initialize semaphores locks etc.
        changingRoomLady = new Semaphore(10);
        changingRoomGent = new Semaphore(10);
        dancingRoom = new Semaphore(3);
        ladyCanDance = new Semaphore(0);


    }

    public void start() throws Exception {
        show = new Show();
        init();
        HashSet<Thread> threads = new HashSet<Thread>();
        for (int i = 0; i < BROJ_INSTANCI; i++) {
            Zensko z = new Zensko();
            Masko m = new Masko();
            threads.add(z);
            threads.add(m);
        }

        for (Thread t : threads) {
            t.start();
        }

        boolean valid = true;
        for (Thread t : threads) {
            if (!hasException) {
                t.join();
            } else {
                t.interrupt();
            }
        }
        show.printStatus();

    }

    class Masko extends Thread {
        //TODO: Definicija  na promenlivi za sostojbata

        public Exception exception = null;

        public void ucestvo() throws InterruptedException {
            //TODO: da se implementira
            changingRoomGent.acquire();
            show.presobleci();
            changingRoomGent.release();
            // samo maskoto go povikuva metodot tancuvaj
            ladyCanDance.acquire();
            dancingRoom.acquire();
            show.tancuvaj();
            dancingRoom.release();

        }

        @Override
        public void run() {
            try {
                ucestvo();
            } catch (InterruptedException e) {
                // Do nothing
            } catch (Exception e) {
                exception = e;
                hasException = true;
            }
        }

        @Override
        public String toString() {
            return String.format("m\t%d", getId());
        }
    }

    class Zensko extends Thread {
        //TODO: Definicija  na promenlivi za sostojbata

        public Exception exception = null;

        public void ucestvo() throws InterruptedException {
            //TODO: da se implementira

            changingRoomLady.acquire();
            show.presobleci();
            changingRoomLady.release();
            ladyCanDance.release();
        }

        @Override
        public void run() {
            try {
                ucestvo();
            } catch (InterruptedException e) {
                // Do nothing
            } catch (Exception e) {
                exception = e;
                hasException = true;
            }
        }

        @Override
        public String toString() {
            return String.format("z\t%d", getId());
        }
    }

    public class Show {

        public static final int BROJ_GARDEROBA = 10;
        public static final int BROJ_TEREN = 3;
        public static final int TYPE_MASKO = 1;
        public static final int TYPE_ZENSKO = 2;
        public static final int TYPE_UNKNOWN = -1;
        public int brojMaskiGarderoba = 0;
        public int brojZenskiGarderoba = 0;
        public int brojTancuvanja = 0;
        public int maxMaskiGarderoba = 0;
        public int maxZenskiGarderoba = 0;
        public int maxTancuvanja = 0;
        private List<String> actions = new ArrayList<String>();
        private boolean hasError = false;

        public Show() {
        }

        public void presobleci() throws RuntimeException {
            log(null, "presobleci start");
            Thread t = Thread.currentThread();
            if (t instanceof Masko) {
                synchronized (RANDOM) {
                    brojMaskiGarderoba++;
                    if (brojMaskiGarderoba > 10) {
                        exception("Ne moze da ima poveke od 10 maski vo maskata garderoba.");
                    }
                    if (brojMaskiGarderoba > maxMaskiGarderoba) {
                        maxMaskiGarderoba = brojMaskiGarderoba;
                    }
                }
                waitRandom();
                synchronized (RANDOM) {
                    brojMaskiGarderoba--;
                }
            } else {
                synchronized (RANDOM) {
                    brojZenskiGarderoba++;
                    if (brojZenskiGarderoba > 10) {
                        exception("Ne moze da ima poveke od 10 zenski vo zenskata garderoba.");
                    }
                    if (brojZenskiGarderoba > maxZenskiGarderoba) {
                        maxZenskiGarderoba = brojZenskiGarderoba;
                    }
                }
                waitRandom();
                synchronized (RANDOM) {
                    brojZenskiGarderoba--;
                }
            }
            log(null, "presobleci kraj");
        }

        public void tancuvaj() throws RuntimeException {
            log(null, "tancuvaj start");
            synchronized (RANDOM) {
                brojTancuvanja++;
                if (brojTancuvanja > BROJ_TEREN) {
                    exception("Ne moze paralelno da tancuvaat poveke od 3 para.");
                }

                if (brojTancuvanja > maxTancuvanja) {
                    maxTancuvanja = brojTancuvanja;
                }
            }
            waitRandom();
            synchronized (RANDOM) {
                brojTancuvanja--;
            }
            log(null, "tancuvaj kraj");
        }

        private void waitRandom() {
            try {
                int r;
                synchronized (RANDOM) {
                    r = RANDOM.nextInt(RANDOM_RANGE);
                }
                Thread.sleep(r);
            } catch (Exception e) {
                //do nothing
            }
        }

        private void exception(String message) {
            RuntimeException e = new RuntimeException(message);
            log(e, null);
            hasError = true;
            throw e;
        }

        public int getType() {
            Thread t = Thread.currentThread();
            if (t instanceof Masko) {
                return TYPE_MASKO;
            } else if (t instanceof Zensko) {
                return TYPE_ZENSKO;
            } else {
                return TYPE_UNKNOWN;
            }
        }

        private synchronized void log(RuntimeException e, String action) {
            Thread t = Thread.currentThread();
            if (e == null) {
                actions.add(t.toString() + "\t(a): " + action);
            } else {
                actions.add(t.toString() + "\t(e): " + e.getMessage());
            }
        }

        public synchronized void printLog() {
            System.out.println("Poradi konkurentnosta za pristap za pecatenje, mozno e nekoja od porakite da ne e na soodvetnoto mesto.");
            System.out.println("Log na izvrsuvanje na akciite:");
            System.out.println("=========================");
            System.out.println("(tip m<=>Masko, tip z<=>Zensko)");
            System.out.println("tip\tid\takcija/error");
            System.out.println("=========================");
            for (String l : actions) {
                System.out.println(l);
            }
        }

        public void printStatus() {
            if (!hasError) {
                int poeni = 25;
                System.out.println("Procesot e uspesno sinhroniziran");
                if (show.maxMaskiGarderoba == 1 || show.maxZenskiGarderoba == 1) {
                    System.out.println("\t-no ima maksimum eden ucesnik vo garderobata.");
                    poeni -= 5;
                }
                if (show.maxTancuvanja == 1) {
                    System.out.println("\t-no ima maksimum edna proverka vo eden moment.");
                    poeni -= 5;
                }

                System.out.println("Osvoeni poeni: " + poeni);

            } else {
                System.out.println("Procesot ne e sinhroniziran spored uslovite na zadacata");
                show.printLog();
                System.out.println("Maksimum mozni poeni: 15");
            }

        }
    }
}