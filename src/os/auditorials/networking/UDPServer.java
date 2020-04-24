package os.auditorials.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer extends Thread{
    private DatagramSocket socket;
    private byte[] buf = new byte[256];

    public UDPServer() {
        try {
            socket = new DatagramSocket(4445);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                //blocks until new datagram packet is received
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("RECEIVED: " + received);
                //get client IP adress
                InetAddress address = packet.getAddress();
                //get client port
                int port = packet.getPort();
                //create new datagram packet
                packet = new DatagramPacket(buf, buf.length, address, port);
                //echoes the created new packet as a response back to the client
                socket.send(packet);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new UDPServer().run();
    }
}
