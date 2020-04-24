package os.exercise.Election;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Server extends Thread{

    private ServerSocket server;
    private Semaphore maxConnections = new Semaphore(100);

    public Server(int port) throws IOException {
        server = new ServerSocket(port);
        initCandidatesFile();
    }

    private void initCandidatesFile() throws IOException {
        DataOutputStream outputStreamVotes = new DataOutputStream(new FileOutputStream("votes.txt"));
        DataOutputStream outputStream = new DataOutputStream(new FileOutputStream("candidates.txt"));
        for (int i=0; i < 4; i++) {
            outputStreamVotes.writeInt(0);
            outputStream.writeInt(i);
            outputStream.writeUTF("Candidate" + i);
        }
    }

    public String getCandidatesFromFile() throws IOException {
        String candidates = "";
        DataInputStream inputStream = new DataInputStream(new FileInputStream("candidates.txt"));
        for (int i=0; i<4; i++) {
            int lineNum = inputStream.readInt();
            candidates += lineNum + " - " + inputStream.readUTF() + "\n";
        }
        System.out.println(candidates);
        return candidates;
    }

    @Override
    public void run() {

        while (true) {
            try {
                maxConnections.acquire();
                Socket accept = server.accept();
                DataInputStream inputStream = new DataInputStream(accept.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(accept.getOutputStream());
                ServerWorker worker = new ServerWorker(this, inputStream, outputStream);
                worker.start();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public synchronized void voteFile(int candidateNumber) throws IOException {
        if (candidateNumber>=0 && candidateNumber <= 3) {
            DataInputStream inputStream = new DataInputStream(new FileInputStream("votes.txt"));
            List<Integer> votes = new ArrayList<>();

            for (int i = 0; i <4; i++) {
                votes.add(inputStream.readInt());
            }

            Integer currentNumberOfVotes = votes.get(candidateNumber);
            votes.set(candidateNumber, currentNumberOfVotes + 1);
            inputStream.close();

            //we initialize it here because whenever it is initialized it wipes off clean the file
            DataOutputStream outputStream = new DataOutputStream(new FileOutputStream("votes.txt"));
            for (int i=0; i<4; i++) {
                outputStream.writeInt(votes.get(i));
            }
            outputStream.flush();
            outputStream.close();
        }
    }

    public synchronized String getResultsFromFile() throws IOException {
        String results = " ";
        DataInputStream inputStream = new DataInputStream(new FileInputStream("votes.txt"));
        for (int i=0; i<4; i++) {
            int votes = inputStream.readInt();
            results += i + " - " + votes + "\n";
        }
        System.out.println(results);
        return results;
    }

    public void closeConnection() throws IOException {
        maxConnections.release();
    }

    public static void main(String[] args) throws IOException {
        new Server(9876).start();
    }
}
