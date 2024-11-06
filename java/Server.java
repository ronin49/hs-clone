
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class Server extends Thread{
    public static void main(String[] args) {
        int port = 8345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен и ждёт подключений на порту " + port);

            // Ожидание клиентов
            Socket client1 = serverSocket.accept();
            Socket client2 = serverSocket.accept();

            // Чтение входящего логина 1
            BufferedReader readLogin1 = new BufferedReader(new InputStreamReader(client1.getInputStream()));
            String loginOut1 = readLogin1.readLine();

            //Чтение входящего логина 2
            BufferedReader readLogin2 = new BufferedReader(new InputStreamReader(client2.getInputStream()));
            String loginOut2 = readLogin2.readLine();

            // Проверка логинов
            if (loginOut1.equals(loginOut2)){
                System.out.println("Клиенты подключились");

                // Отправка ответа клиентам
                PrintWriter answerOut1 = new PrintWriter(client1.getOutputStream(),true);
                answerOut1.println("Вы подключились!");
                PrintWriter answerOut2 = new PrintWriter(client2.getOutputStream(),true);
                answerOut2.println("Вы подключились!");
            } else {
                System.out.println("Клиенты ввели неправельный логин");
                PrintWriter answerOut1 = new PrintWriter(client1.getOutputStream(),true);
                answerOut1.println("Вы ввели неправельный логин");
                PrintWriter answerOut2 = new PrintWriter(client2.getOutputStream(),true);
                answerOut2.println("Вы ввели неправельный логин");
                client1.close();
                client2.close();
            }

            // Потоки для отправки и получения данных
            if (serverSocket.isClosed()){
                client1.close();
                client2.close();
            }
            BufferedReader in1 = new BufferedReader(new InputStreamReader(client1.getInputStream()));
            BufferedReader in2 = new BufferedReader(new InputStreamReader(client2.getInputStream()));
            PrintWriter out1 = new PrintWriter(client1.getOutputStream(), true);
            PrintWriter out2 = new PrintWriter(client2.getOutputStream(), true);


            ServerThread thread1 = new ServerThread(in1, out2);
            ServerThread thread2 = new ServerThread(in2, out1);
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();

        } catch (SocketException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Ошибка на сервере: " + e.getMessage());
        } catch (NullPointerException e){
            System.out.println("Клиент отключился");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

