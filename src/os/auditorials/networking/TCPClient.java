package os.auditorials.networking;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    private Socket socket;
    private Scanner scanner;
    private TCPClient(InetAddress serverAddress, int serverPort) throws Exception {
        this.socket = new Socket(serverAddress, serverPort);
        this.scanner = new Scanner(System.in);
    }

    private void start() throws Exception {
        String input;
        while (true) {
            input = scanner.nextLine();
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            out.println(input);
            out.flush();
        }
    }

    public static void main(String[] args) throws Exception {
        TCPClient client = new TCPClient(InetAddress.getByName("localhost"), 9876);
        System.out.println("\r\nConnected to Server: " + client.socket.getInetAddress());
        client.start();
    }
}
