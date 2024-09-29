import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        int port = 8345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен и ждёт подключений на порту " + port);
            // Ожидание клиента
            Socket clientSocket = serverSocket.accept();
            System.out.println("Клиент подключился!");
            while (true) {

                // Потоки для отправки и получения данных
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Чтение сообщения от клиента
                String clientMessage = in.readLine();
                if (clientMessage.equals("null")){
                    break;
                }
                System.out.println(clientMessage);
                // Отправка ответа клиенту
                Scanner sc = new Scanner(System.in);
                String sentence = sc.nextLine();
                if (sentence.equals("stop")){
                    break;
                }
                out.println(sentence);
            }
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Ошибка на сервере: " + e.getMessage());
        } catch (NullPointerException e){
            System.out.println("Клиент отключился");
        }
    }
}
