package os.labs.lab2;

import java.io.File;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

/*
Да се имплементира класа FileScanner која што ќе се однесува како thread. Во класата FileScanner се чуваат податоци за : - патеката на директориумот што треба да се скенира - статичка променлива counter што ќе брои колку нишки од класата FileScanner ќе се креираат Во класата FileScanner да се имплементираа статички методот што ќе печати информации за некоја датотека од следниот формат:

        dir: C:\Users\185026\Desktop\lab1 - reshenija 4096 (dir за директориуми, апсолутна патека и големина)

        file: C:\Users\Stefan\Desktop\spisok.pdf 29198 (file за обични фајлови, апсолутна патека и големина)

        Дополнително да се преоптовари методот run() од класата Thread, така што ќе печати информации за директориумот за којшто е повикан. Доколку во директориумот има други под директориуми, да се креира нова нишка од тип FileScanner што ќе ги прави истите работи како и претходно за фајловите/директориумите што се наоѓаат во тие директориуми (рекурзивно).

        На крај да се испечати вредноста на counter-от, односно колку вкупно нишки биле креирани.  Користете го следниот почетен код.


        -----



        Implement the class FileScanner that will act as a thread. Inside the FileScanner class the following data is saved: - path to a directory that has to be scanned, - a static variable counter that will count the total number of threads that will be created. Implement a static method that will print the info for the file in the following format:

        dir: C:\Users\111111\Desktop\lab1 - solutions 4096 (dir for directory, absolute path and size)

        file: C:\Users\Stefan\Desktop\spisok.pdf 29198 (file for files, absolute path and size)

        The run() method should be overridden in a way that it will print the information for the file that it is scanning. If the current directory has sub-directories, a new FileScanner thread should be started for each of these files, and will act the same as previously described (recursively)

        When the entire directory tree is scanned, print the counter value. Use the following started code:


*/
public class FileScanner  extends Thread{

    private String fileToScan;
    //TODO: Initialize the start value of the counter
    private static Long counter = (long) 0;;
    public static Semaphore semaphore = new Semaphore(1);;


    public FileScanner (String fileToScan) {
        this.fileToScan=fileToScan;
        //TODO: Increment the counter on every creation of FileScanner object
        try {
            semaphore.acquire();
            counter++;
            semaphore.release();
            System.out.println(counter + " - Created fs thread!");
        } catch (InterruptedException e) {
            e.printStackTrace();
            semaphore.release();
        }
    }
    
    public static void printInfo(File file)  {

        /*
         * TODO: Print the info for the @argument File file, according to the requirement of the task
         * */
        String type;
        type = file.isDirectory() ? "dir" : "file";
        System.out.println(type + ": " + file.getAbsolutePath() + " " + file.length());
    }

    public static Long getCounter () {
        return counter;
    }


    public void run() {

        //TODO Create object File with the absolute path fileToScan.
        File file = new File(fileToScan);
        HashSet<Thread> threads = new HashSet<Thread>();

        //TODO Create a list of all the files that are in the directory file.
        File [] files = file.listFiles();


        for (File f : files) {

            /*
             * TODO If the File f is not a directory, print its info using the function printInfo(f)
             * */

            printInfo(f);

            /*
             * TODO If the File f is a directory, create a thread from type FileScanner and start it.
             * */
            if(f.isDirectory()) {
                FileScanner fs = new FileScanner(f.getAbsolutePath());
                threads.add(fs);
                fs.start();
            }

            for (Thread t : threads) {
                try {
                    t.join();
                    System.out.println("Thread done!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    semaphore.notifyAll();
                }
            }
            //TODO: wait for all the FileScanner-s to finish
        }

    }

    public static void main (String [] args) throws InterruptedException {

        //String FILE_TO_SCAN = "C:\\Users\\189075\\Desktop\\lab";

        String FILE_TO_SCAN = "C:\\Users\\szgnm\\Desktop";

        //TODO Construct a FileScanner object with the fileToScan = FILE_TO_SCAN
        FileScanner fileScanner = new FileScanner(FILE_TO_SCAN);

        //TODO Start the thread from type FileScanner
        fileScanner.start();

        //TODO wait for the fileScanner to finish
        fileScanner.join();

        //TODO print a message that displays the number of thread that were created
        System.out.println(fileScanner.getCounter());

    }
}


