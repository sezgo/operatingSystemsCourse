package os.labs.lab4;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ReadThread extends Thread{
    private DataInputStream reader;
    private Socket socket;
    private ChatClient chatClient;

    public ReadThread(Socket socket, ChatClient chatClient){
        this.socket = socket;
        this.chatClient = chatClient;
        try {
            reader = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Error getting input stream" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String message = reader.readUTF();
                System.out.println("\n" + message);
                if (chatClient.getUserName() != null) {
                    System.out.print("[" + chatClient.getUserName() + "]: ");
                }
            }catch (IOException e) {
                System.out.println("Error reading from server: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }
}
