import java.io.BufferedReader;
import java.io.IOException;

public class ClientThreadRead extends Thread{
    BufferedReader in;

    public ClientThreadRead(BufferedReader in) {
        this.in = in;
    }

    public void run(){
        //while (true){
            String serverMessage = null;
            try {
                serverMessage = in.readLine();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            System.out.println(serverMessage);
        //}
    }
}
