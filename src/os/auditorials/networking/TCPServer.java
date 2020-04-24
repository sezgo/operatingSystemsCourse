package os.auditorials.networking;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    private ServerSocket server;

    public TCPServer() throws Exception {
        this.server = new ServerSocket(9876);
    }

    private void listen() throws Exception {
        String data = null;
        Socket client = this.server.accept();
        String clientAddress = client.getInetAddress().getHostAddress();
        System.out.println("\r\nNew Connection from " + clientAddress);

        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        while ((data = in.readLine()) != null) {
            System.out.println("\r\nMessage from " + clientAddress + ": " + data);
        }
    }

    public static void main(String[] args) throws Exception {
        TCPServer app = new TCPServer();
        System.out.println("\r\nRunning Server: " +
                "Host= " + app.server.getInetAddress() +
                " Port = " + app.server.getLocalPort());

        app.listen();
    }

}
