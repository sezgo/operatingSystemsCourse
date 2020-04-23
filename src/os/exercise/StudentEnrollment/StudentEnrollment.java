package os.exercise.StudentEnrollment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class StudentEnrollment {
    public static void main(String[] args) {
        try {
            new EnrollmentRoom().simulate();
        }catch (InterruptedException e) {
            System.out.println("NOT OK!");
        }
    }
}

class EnrollmentRoom {

    private Semaphore cmInRoom;
    private Semaphore candidateDocs;
    private Semaphore candidateEnter;
    private Semaphore candidateExit;
    private int MAX_ENROLLMENT = 10;

    private void init() {
        cmInRoom = new Semaphore(4);
        candidateDocs = new Semaphore(0);
        candidateEnter = new Semaphore(0);
        candidateExit = new Semaphore(0);
    }

    class CommissionMember extends Thread {

        //no need to protect this
        private int enrolledStudents;
        public CommissionMember() {
            this.enrolledStudents = 0;
        }
        @Override
        public void run() {
            try {
                commissionEnrollment();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void enroll() {
            System.out.println(currentThread().getName() + " enrolls candidate " + enrolledStudents);
        }

        private void commissionEnrollment() throws InterruptedException {
            cmInRoom.acquire();
            System.out.println("===Enters " + currentThread().getName());

            while(enrolledStudents != MAX_ENROLLMENT) {
                candidateEnter.release();
                candidateDocs.acquire();
                enroll();
                enrolledStudents++;
                candidateExit.release();
            }
            System.out.println(enrolledStudents + " std enrolled ===Exits " + currentThread().getName());
            cmInRoom.release();
        }
    }
    class Candidate extends Thread {

        private void candidateEnrollment() throws InterruptedException {
            candidateEnter.acquire();
            candidateDocs.release();
            presentDocuments();
            candidateExit.acquire();
        }

        private void presentDocuments() {
            System.out.println("===Presenting docs " + currentThread().getName());
        }

        @Override
        public void run() {
            try {
                candidateEnrollment();
            } catch (InterruptedException e) {}
        }

    }


    void simulate() throws InterruptedException {

        List<Thread> threads = new ArrayList<>();
        for (int i=0; i<5; i++){
            CommissionMember cm = new CommissionMember();
            cm.setName("CommissionMember_" + i);
            threads.add(cm);
        }

        for (int i=0; i<50; i++){
            Candidate c = new Candidate();
            c.setName("Candidate_" + i);
            threads.add(c);
        }

        init();

        for (Thread t : threads)
            t.start();

        for (Thread t : threads)
            t.join(100);

        boolean deadlock = false;

        for(Thread t : threads) {
            if (t.isAlive()) {
                t.interrupt();
                deadlock = true;
            }
        }

        System.out.println(deadlock ? "Deadlock" : "Sync done");
    }
}

