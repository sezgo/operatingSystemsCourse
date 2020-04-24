package os.exercise.Election;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread{

    String name;
    private Socket socket;

    public Client(String name, String host, int port) throws IOException {
        this.name = name;
        socket = new Socket(host, port);
    }

    @Override
    public void run() {
        try {

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            String candidateList = inputStream.readUTF();
            System.out.println(candidateList);
            System.out.println(name + ": choose a candidate");

            Scanner scanner = new Scanner(System.in);
            int readVote = scanner.nextInt();
            outputStream.writeInt(readVote);
            System.out.println("Client " + name + " sent vote: " + readVote);

            System.out.println("Client " + name + " waiting for results...");

            String resuts = inputStream.readUTF();
            System.out.println(resuts);


        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Client c1 = new Client("sezgo", "localhost", 9876);
        Client c2 = new Client("damlos", "localhost", 9876);
        Client c3 = new Client("egzon", "localhost", 9876);

        c1.start();
        c2.start();
        c3.start();
    }
}
