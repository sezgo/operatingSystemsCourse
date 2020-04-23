package os.exercise.SmokerSync;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class SmokerSync {
    public static void main(String[] args) throws InterruptedException {
        new SmokingTable().simulate();
    }
}

class SmokingTable {

    int TOBACCO = 0;
    int PAPER = 1;
    int MATCHES = 2;

    String ingredients[] = {"Tobacco", "Paper", "Matches"};


    boolean neededItems[] = {true, true, true};
    int itemsUsed[] = {0, 0, 0};
    boolean onTable[] = {false, false, false};

    private Semaphore emptyTable = new Semaphore(1);
    private Semaphore itemsSet = new Semaphore(0);
    private Semaphore checkTable = new Semaphore(1);

    class Agent extends Thread {
        void setTable() throws InterruptedException {

            emptyTable.acquire();
            while (neededItems[0]  || neededItems[1] || neededItems[2]) {
                int items[] = getTwoRandom();
                System.out.println(currentThread().getName() + "---Putting items: " +
                        ingredients[items[0]] + ", " + ingredients[items[1]]);

                onTable[items[0]] = true;
                onTable[items[1]] = true;
                if (++itemsUsed[items[0]] == 2) neededItems[items[0]] = false;
                if (++itemsUsed[items[1]] == 2) neededItems[items[1]] = false;

                itemsSet.release(3);
                emptyTable.acquire();
            }
            System.out.println("No item is needed anymore!");
        }

        private int[] getTwoRandom() {
            int randomIndexes[] = new int[2];

            randomIndexes[0] = getRandom();
            randomIndexes[1] = getRandom();

            while (randomIndexes[0] == randomIndexes[1] ||
                    !neededItems[randomIndexes[0]] ||
                    !neededItems[randomIndexes[1]])
            {
                randomIndexes[0] = getRandom();
                randomIndexes[1] = getRandom();
            }

            return randomIndexes;
        }
        private int getRandom() {
            return (int) (Math.random() * ingredients.length);
        }

        @Override
        public void run() {
            try {
                setTable();
            } catch (InterruptedException e) {  }
        }
    }

    class Smoker extends  Thread {

        private int item;
        private String itemName;
        private int[] lookingFor;

        public Smoker(int item) {
            this.item = item;
            this.itemName = ingredients[item];

            lookingFor = new int[2];
            int index = 0;
            for (int i = 0; i < 3; i++)
                if (i != item)
                    lookingFor[index++] = i;

        }
        void checkTable() throws InterruptedException {
            itemsSet.acquire();
            checkTable.acquire();
            System.out.println("---Checking table "+ currentThread().getName() + " has " + itemName);
            System.out.println("Looking for: " + ingredients[lookingFor[0]] + ", " + ingredients[lookingFor[1]]);

            while (!onTable[lookingFor[0]] || !onTable[lookingFor[1]]) {
                System.out.println("Smoker.checkTable " + currentThread().getName());
                checkTable.release();
                Thread.sleep(1000);
                checkTable.acquire();
            }

            onTable[lookingFor[0]] = false;
            onTable[lookingFor[1]] = false;
            System.out.println("got them & rolling" + currentThread().getName());
            emptyTable.release();
            checkTable.release();


        }
        @Override
        public void run() {
            try {
                System.out.println("Smoker.run" + currentThread().getName());
                checkTable();
            } catch (InterruptedException e) {  }
        }
    }

    void simulate() throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        Agent agent = new Agent();
        agent.setName("Agent");
        threads.add(agent);

        Smoker s1 = new Smoker(TOBACCO);
        s1.setName("Smoker-1");
        threads.add(s1);
        Smoker s2 = new Smoker(PAPER);
        s2.setName("Smoker-2");
        threads.add(s2);
        Smoker s3 = new Smoker(MATCHES);
        s3.setName("Smoker-3");
        threads.add(s3);


        for (Thread t : threads)
            t.start();

        for (Thread t: threads)
            t.join(10000);

        boolean deadlock = false;

        for (Thread t : threads){
            if (t.isAlive()) {
                t.interrupt();
                deadlock = true;
            }
        }


        System.out.println(deadlock ? "Deadlock" : "Sync done");
    }
}
