import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientServer {
    public static void main(String[] args) {
        String hostname = "localhost";  // Имя хоста или IP-адрес сервера
        int port = 8345;  // Порт, на котором работает сервер

        try (Socket socket = new Socket(hostname, port)) {
            // Ввод логина для подключения
            System.out.println("Напиши логин для подключения");
            Scanner login = new Scanner(System.in);
            String loginKey = login.nextLine();
            // Отправка логина для подключения
            PrintWriter loginOut = new PrintWriter(socket.getOutputStream(), true);
            loginOut.println(loginKey);

            //Получение логина от сервера
            BufferedReader getServerLogin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String serverLogin = getServerLogin.readLine();

            // Проверка на существование логина
            if (!serverLogin.equals(loginKey)){
                System.out.println("Вы ввели неправельный логин, такого логина не существует");
                socket.close();
                loginOut.close();
            } else {
                System.out.println("Вы подключились!");
            }

            while (true){
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Отправка сообщения на сервер
                Scanner sc = new Scanner(System.in);
                String sentence = sc.nextLine();
                if(sentence.equals("stop")){
                    break;
                } else {
                    out.println(sentence);
                }
                // Чтение ответа от сервера
                String serverResponse = in.readLine();
                if (serverResponse.equals("null")){
                    break;
                }
                System.out.println(serverResponse);
            }
        } catch (IOException e) {
            System.out.println("Ошибка клиента: " + e.getMessage());
        } catch (NullPointerException e){
            System.out.println("Ошибка, сервер не работает");
        }
    }
}
