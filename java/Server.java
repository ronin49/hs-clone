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
            System.out.println("Напиши логин для подключения");
            Scanner login = new Scanner(System.in);
            String loginKey = login.nextLine();

            // Ожидание клиента
            Socket clientSocket = serverSocket.accept();

            // Чтение входящего логина
            BufferedReader loginIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String loginOut = loginIn.readLine();

            // Отправка на проверку логина
            PrintWriter writeOutLogin = new PrintWriter(clientSocket.getOutputStream(), true);
            writeOutLogin.println(loginKey);

            // Проверка входящего логина
            if (loginKey.equals(loginOut)) {
                System.out.println("Клиент подключился!");
            } else {
                clientSocket.close();
                serverSocket.close();
            }
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
