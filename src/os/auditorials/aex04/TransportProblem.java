package os.auditorials.aex04;

//allowed states//
// 4 people
//2 teamA && 2 teamB
//4 teamA
//4 teamB

//the shared resource would be the taxi

//4 people come in front of the taxi
//if they can enter they do so
//only when taxi takes them home the next 4 can enter

//any fan comes first checks if there are any other waiting
//and blocks

//a blocks
//a blocks
//b blocks cuz allowed
//a comes and waits
//b blocks and notify first three

//taxi waits

//we need counters for both teamA and teamB

//requires us to have 2 different types of threads
// so wee need to different classes within which we ll run the threads

//a class for shared resource taxi too

import java.util.HashSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransportProblem {
    public static void main(String[] args) {
        TransportProblemTest u = new TransportProblemTest();
        try {
            u.simulate();
        } catch (InterruptedException e) {
            System.out.println("NOT OK");
        }
    }
}

class TransportProblemTest {

    void simulate() throws InterruptedException {
        Taxi taxi = new Taxi();

        HashSet<Thread> threads = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            FanA fanA = new FanA(taxi);
            fanA.setName("FanA_" + i);
            threads.add(fanA);
        }

        for (int i = 0; i < 30; i++) {
            FanB fanB = new FanB(taxi);
            fanB.setName("FanB_" + i);
            threads.add(fanB);
        }

        for (Thread t : threads)
            t.start();

        for (Thread t : threads) {
            t.join(1000);
        }

        boolean hasDeadLock = false;
        for (Thread t : threads) {
            if (t.isAlive()) {
                t.interrupt();
                System.out.println("Deadlock");
                hasDeadLock = true;
            }
        }
        if (!hasDeadLock)
            System.out.println("Synced successfully");
    }

    private static class Taxi {


        private static int counterA = 0;
        private static int counterB = 0;

        private static Lock lock = new ReentrantLock();

        private static Semaphore fanACanEnter = new Semaphore(0);
        private static Semaphore fanBCanEnter = new Semaphore(0);

        private static Semaphore isSeated = new Semaphore(0);
        private static Semaphore canExit = new Semaphore(0);

        void seatFanA() throws InterruptedException {
            //TODO: 4/3/20
            //need to sync counterA to allow only one thread at time to reach it
            boolean isLast = false;
            lock.lock();
            counterA++;
            personHere();
            if (counterA == 2 && counterB >= 2) {
                //notify waiting threads to get into the taxi
                //because it is the last one and it is fanA we need
                //1 more fanA and 2 more fanB
                isLast = true;
                counterA -= 2;
                counterB -= 2;
                System.out.println("Available permits for fanA" + fanACanEnter.availablePermits());
                System.out.println("Available permits for fanB" + fanBCanEnter.availablePermits());
                fanACanEnter.release();
                fanBCanEnter.release(2);
                System.out.println("Available permits for fanA" + fanACanEnter.availablePermits());
                System.out.println("Available permits for fanB" + fanBCanEnter.availablePermits());
            } else if (counterA == 4) {
                //notify waiting threads to get into the taxi
                //because it is the last one and it is fanA we need
                //3 more fanA's
                isLast = true;
                counterA -= 4;
                System.out.println("Available permits for fanA" + fanACanEnter.availablePermits());
                System.out.println("Available permits for fanB" + fanBCanEnter.availablePermits());
                fanACanEnter.release(3);
                System.out.println("Available permits for fanA" + fanACanEnter.availablePermits());
                System.out.println("Available permits for fanB" + fanBCanEnter.availablePermits());
            } else {
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + " waiting" + counterA + " more");
                fanACanEnter.acquire();
                System.out.println(Thread.currentThread().getName() + " go for it ");
            }

            seated();
            System.out.println("i sat " + Thread.currentThread().getName());


            //the last one is in control of seating/driving/exiting the car
            if (isLast) {
                //the last one comes and notifys previous three to be seated


                System.out.println("oturabilenler " + isSeated.availablePermits());
                isSeated.acquire(3);

                System.out.println(Thread.currentThread().getName() + " lets " + isSeated.availablePermits() +
                        "others to be seated!");
                drive();
                canExit.release(4);
                lock.unlock();

                //only the last one unlocks only after the taxi has driven
            } else {
                //first three threads will come here and asks for the be seated
                isSeated.release();
            }

            canExit.acquire();
            exit();
        }

        void seatFanB() throws InterruptedException {
            //TODO: 4/3/20
            //need to sync counterA to allow only one thread at time to reach it
            boolean isLast = false;
            lock.lock();
            counterB++;
            personHere();
            if (counterB == 2 && counterA >= 2) {
                //notify waiting threads to get into the taxi
                //because it is the last one and it is fanA we need
                //1 more fanA and 2 more fanB
                isLast = true;
                counterA -= 2;
                counterB -= 2;
                fanBCanEnter.release();
                fanACanEnter.release(2);
            } else if (counterB == 4) {
                //notify waiting threads to get into the taxi
                //because it is the last one and it is fanA we need
                //3 more fanA's
                isLast = true;
                counterB -= 4;
                System.out.println("Available permits for fanA" + fanACanEnter.availablePermits());
                System.out.println("Available permits for fanB" + fanBCanEnter.availablePermits());
                fanBCanEnter.release(3);
                System.out.println("Available permits for fanA" + fanACanEnter.availablePermits());
                System.out.println("Available permits for fanB" + fanBCanEnter.availablePermits());
            } else {
                lock.unlock();
                //can i enter?
                System.out.println(Thread.currentThread().getName() + " waiting" + counterB + " more");
                fanBCanEnter.acquire();
                System.out.println(Thread.currentThread().getName() + " go for it ");
            }

            seated();


            //the last one is in control of seating/driving/exiting the car
            if (isLast) {
                //the last one comes and notifys previous three to be seated
                System.out.println("oturabilenler " + isSeated.availablePermits());
                isSeated.acquire(3);

                System.out.println(Thread.currentThread().getName() + " lets " + isSeated.availablePermits() +
                        "others to be seated!");
                drive();
                canExit.release(4);


                //only the last one unlocks only after the taxi has driven
                lock.unlock();
            } else {
                //first three threads will come here and asks for the be seated
                isSeated.release();
                System.out.println("i sat " + Thread.currentThread().getName());
            }

            canExit.acquire();
            exit();
        }

        void personHere() {
            System.out.println("====HERE: " + Thread.currentThread().getName());
            System.out.flush();
        }

        void seated() {
            System.out.println("====SEATED: " + Thread.currentThread().getName());
            System.out.flush();
        }

        void drive() {
            System.out.println("====DRIVE: " + Thread.currentThread().getName());
        }

        void exit() {
            System.out.println("====EXITING: " + Thread.currentThread().getName());
        }
    }

    private class FanA extends Thread {
        private Taxi taxi;

        public FanA(Taxi taxi) {
            this.taxi = taxi;
        }

        @Override
        public void run() {
            try {
                taxi.seatFanA();
            } catch (InterruptedException e) {
                System.out.println("===ERROR===" + Thread.currentThread().getName());
            }
        }
    }

    private class FanB extends Thread {
        private Taxi taxi;

        public FanB(Taxi taxi) {
            this.taxi = taxi;
        }

        @Override
        public void run() {
            try {
                taxi.seatFanB();
            } catch (InterruptedException e) {
                System.out.println("===ERROR===" + Thread.currentThread().getName());
            }
        }
    }
}
