package os.exercise.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerWorker extends Thread{

    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    public ServerWorker(DataOutputStream dataOutputStream, DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }

    @Override
    public void run() {
        try {
            System.out.println("server worker running");
            String messageFromClient = dataInputStream.readUTF();
            System.out.println("Received message from client: " + messageFromClient);
            dataOutputStream.writeUTF("Hello, I am the server");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
