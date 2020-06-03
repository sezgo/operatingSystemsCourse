package os.labs.lab4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatWorker extends Thread{

    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private ChatServer chatServer;
    private Socket client;

    public ChatWorker(ChatServer chatServer, Socket client) throws IOException {
        this.dataInputStream = new DataInputStream(client.getInputStream());
        this.dataOutputStream = new DataOutputStream(client.getOutputStream());
        this.chatServer = chatServer;
    }

    void sendMessage(String message) throws IOException {
        dataOutputStream.writeUTF(message);
    }

    void printOnlineUsers() throws IOException {
        if (chatServer.hasUsers()){
            dataOutputStream.writeUTF("Connected users: " + chatServer.getUserNames());
        }else {
            dataOutputStream.writeUTF("No other users connected.");
        }
    }

    @Override
    public void run() {
        System.out.println("new user arrives as " + currentThread().getName());
        try {

            printOnlineUsers();

            String userName = dataInputStream.readUTF();
            chatServer.addUserName(userName);
            String serverMessage = "\n -- New chat user connected " + userName + " -- ";
            chatServer.broadcast(serverMessage, this);

            String clientMessage;
            do {
                clientMessage = dataInputStream.readUTF();
                serverMessage = "[" +userName+"]: " + clientMessage;
                chatServer.broadcast(serverMessage, this);
            }while (!clientMessage.equals("exit"));


            serverMessage = userName + "has quited.";
            chatServer.broadcast(serverMessage, this);


            chatServer.removeUser(userName, this);
            client.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
