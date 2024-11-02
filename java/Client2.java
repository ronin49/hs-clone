import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client2 {
    public static void main(String[] args) {
        int port = 8345;
        String hostname = "localhost";
        try (Socket socket = new Socket(hostname, port)){
            // Ввод логина для подключения
            System.out.println("Напиши логин для подключения:");
            Scanner scannerLogin = new Scanner(System.in);
            String login = scannerLogin.nextLine();

            // Отправка логина на проверку
            PrintWriter loginOut = new PrintWriter(socket.getOutputStream(), true);
            loginOut.println(login);

            // Получение ответа от сервера
            BufferedReader answerFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String serverAnswer = answerFromServer.readLine();

            // Проверка на существование логина
            if (serverAnswer.equals("Вы подключились!")){
                System.out.println(serverAnswer);
            } else if (serverAnswer.equals("Вы ввели неправельный логин")){
                System.out.println(serverAnswer);
                socket.close();
                loginOut.close();
            }

            // Основная логика
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while(true){
                // Чтение сообщения от сервера
                String messageIn = in.readLine();
                if (messageIn.equals("null")){
                    break;
                }
                System.out.println(messageIn);

                // Отправка Сообщения на сервер
                Scanner sendMessage = new Scanner(System.in);
                String messageOut = sendMessage.nextLine();
                if(messageOut.equals("stop")){
                    break;
                } else {
                    out.println(messageOut);
                }
            }

        } catch (IOException e) {
            System.out.println("Ошибка клиента: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Ошибка, сервер не работает");
        }
    }
}
