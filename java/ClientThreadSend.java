import java.io.PrintWriter;
import java.util.Scanner;

public class ClientThreadSend extends Thread{
    private PrintWriter out;
    private Scanner sc = new Scanner(System.in);
    private String name;

    public ClientThreadSend(PrintWriter out, String name){
        this.out = out;
        this.name = name;

    }
    public void run(){
        //while(true) {
            String message;
            message = sc.nextLine();
            out.println(name + ": " + message);
        //}
    }
}
