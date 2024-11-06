import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ServerThread extends Thread{
    private BufferedReader in;
    private PrintWriter out;

    public ServerThread(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }


    public void run(){
        while(true) {
            String clientMessage;
            try {
                clientMessage = this.in.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.out.println(clientMessage);
        }
    }
}
