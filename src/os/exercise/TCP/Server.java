package os.exercise.TCP;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{

    private ServerSocket server;

    public Server(int port) throws IOException {
        server = new ServerSocket(port);
    }

    private void listen() throws IOException {
        while (true) {
            System.out.println("Server thread " + currentThread().getName() + " running.");
            Socket client = server.accept();
            DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream());


            String messageFromClient = dataInputStream.readUTF();
            System.out.println("Received message from client: " + messageFromClient);
            dataOutputStream.writeUTF("Hello, I am the server");

            //server communicate with the client
            //receive some message and send some response
            //ServerWorker serverWorkerThread = new ServerWorker(dataOutputStream, dataInputStream);
            //serverWorkerThread.start();

        }
    }

    @Override
    public void run() {
        try {
            listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        //here the entire main thread gets blocked because of the server.accept()
        //can solve it by making the server class a thread
        Server server = new Server(9876);
        server.start();
    }
}
