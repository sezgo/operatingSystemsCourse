package os.labs.lab4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

/**
 * This is a chat server program
 * For the 4 laboratory exercise of operating systems course in finki
 *
 * @author 175052 Sezgin Mustafa
 *
 */


public class ChatServer {

    private ServerSocket server;
    int port;
    HashSet<String> userNames;
    HashSet<ChatWorker> userThreads;

    public ChatServer(int port) throws IOException {
        this.port = port;
        server = new ServerSocket(port);
        userNames = new HashSet<>();
        userThreads = new HashSet<>();
    }

    String getUserNames(){
        String str="";
        for (String user : userNames){
            str += user + " ";
        }
        return str;
    }

    boolean hasUsers(){
        return !userNames.isEmpty();
    }

    /**
     * a user gets removed when types exit
     * @param username
     * @param thread
     */
    void removeUser(String username, ChatWorker thread){
        userNames.remove(username);
        userThreads.remove(thread);
    }

    /**
     * creates worker thread for each connected client
     * @throws IOException
     */
    public void listen() throws IOException {
        System.out.println("Server is listening on port " + port);

        while (true) {
            Socket client = server.accept();
            //a thread for each connected client so that server does not get blocked here
            ChatWorker chatWorkerThread = new ChatWorker(this, client);
            userThreads.add(chatWorkerThread);
            chatWorkerThread.start();
        }
    }

    /**
     * Stores username of the newly connected chatClient
     * @param userName (typed in by the user in chatWorker thread)
     */
    void addUserName(String userName) {
        userNames.add(userName);
    }

    /**
     * Excluding the chatWorker thread of the user who send the message,
     * delivers the message to others
     * @param message
     */
    void broadcast(String message, ChatWorker sendingThread) throws IOException {
        for (ChatWorker thread : userThreads){
            if (thread != sendingThread) {
                thread.sendMessage(message);
            }
        }
    }


    public static void main(String[] args) throws IOException
    {
        ChatServer chatServer = new ChatServer(9876);
        chatServer.listen();
    }
}
