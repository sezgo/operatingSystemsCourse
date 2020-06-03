package os.labs.lab4;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient
{
    private Socket socket;
    private String userName;

    WriteThread wt;
    ReadThread rt;

    public ChatClient(String serverAddress, int serverPort){
        try {
            socket = new Socket(serverAddress, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserName(){
        return userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }

    public void execute() throws IOException {
        //communicate with the server

        System.out.println("Connected to the chat server");
        wt = new WriteThread(socket, this);
        rt = new ReadThread(socket, this);

        wt.start();
        rt.start();


    }

    public static void main(String[] args) throws IOException {
        ChatClient chatClient1 = new ChatClient("localhost", 9876);

        chatClient1.execute();

    }
}

