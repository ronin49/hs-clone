import java.io.PrintWriter;
import java.util.Scanner;

public class ClientThreadSend extends Thread{
    private PrintWriter out;
    private Scanner sc = new Scanner(System.in);

    public ClientThreadSend(PrintWriter out){
        this.out = out;

    }
    public void run(){
        while(true) {
            String message;
            message = sc.nextLine();
            out.println(message);
        }
    }
}
