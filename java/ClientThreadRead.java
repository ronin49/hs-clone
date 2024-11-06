import java.io.BufferedReader;
import java.io.IOException;

public class ClientThreadRead extends Thread{
    BufferedReader in;
    String name;

    public ClientThreadRead(BufferedReader in, String name) {
        this.in = in;
        this.name = name;
    }

    public void run(){
        while (true){
            String serverMessage = null;
            try {
                serverMessage = in.readLine();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            System.out.println(this.name + ": " + serverMessage);
        }
    }
}
