package os.exercise.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server extends Thread{

    private DatagramSocket server;
    private byte buffer[] = new byte[64];

    public Server(int port) throws SocketException {
        server = new DatagramSocket(port);
    }

    @Override
    public void run() {
        while (true) {
            DatagramPacket packetFromClient = new DatagramPacket(buffer, buffer.length);

            try {
                server.receive(packetFromClient);
                System.out.println("Received message: ");
                String message = new String(packetFromClient.getData(), 0, buffer.length);
                System.out.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws SocketException {
        new Server(9876).start();
    }
}
