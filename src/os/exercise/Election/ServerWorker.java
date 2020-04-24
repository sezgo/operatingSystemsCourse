package os.exercise.Election;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerWorker extends  Thread{

    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private Server server;

    public ServerWorker (Server server, DataInputStream inputStream, DataOutputStream outputStream) {
        this.server = server;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        try {
            outputStream.writeUTF(server.getCandidatesFromFile());
            System.out.println("Worker: sent list to client");
            int candidateNumber = inputStream.readInt();
            System.out.println("Received vote for: " + candidateNumber);
            server.voteFile(candidateNumber);
            outputStream.writeUTF(server.getResultsFromFile());
            server.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
