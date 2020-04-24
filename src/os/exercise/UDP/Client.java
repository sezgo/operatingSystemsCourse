package os.exercise.UDP;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client extends Thread{

    private DatagramSocket socket;

    public Client() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                String message = new Scanner(System.in).nextLine();
                byte messageBytes[] = message.getBytes();
                DatagramPacket datagramPacket = new DatagramPacket(messageBytes, messageBytes.length);

                datagramPacket.setAddress(InetAddress.getLocalHost());
                datagramPacket.setPort(9876);

                socket.send(datagramPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Client c = new Client();
        c.start();
    }
}
