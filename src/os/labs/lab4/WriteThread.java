package os.labs.lab4;

import java.io.Console;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class WriteThread extends Thread{
    private DataOutputStream dataOutputStream;
    private Socket socket;
    private ChatClient client;
    private Scanner scanner;

    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;
        scanner = new Scanner(System.in);
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.print("Enter your name: ");
            String userName = scanner.nextLine();

            client.setUserName(userName);
            dataOutputStream.writeUTF(userName);

            String text;
            do {
                System.out.print("[" + userName + "]: ");
                text = scanner.nextLine();
                dataOutputStream.writeUTF(text);
            }while(!text.equals("exit"));

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
