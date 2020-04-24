package os.exercise.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread
{
    private Socket socket;
    private String name;

    public Client(String name, InetAddress serverAddress, int serverPort) throws IOException {
        this.name = name;
        socket = new Socket(serverAddress, serverPort);
    }

    @Override
    public void run() {
        //communicate with the server
        try {
            System.out.println("Client thread " + currentThread().getName() + " running.");
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            dataOutputStream.writeUTF("Hello my name is " + name);
            String messageFromServer = dataInputStream.readUTF();

            System.out.println("Received from the server: " + messageFromServer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Client client1 = new Client("Sezgo", InetAddress.getLocalHost(), 9876);
        Client client2 = new Client("Damla", InetAddress.getLocalHost(), 9876);
        Client client3 = new Client("Egzon", InetAddress.getLocalHost(), 9876);

        client1.start();
        client2.start();
        client3.start();

    }
}
