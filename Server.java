import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            ServerSocket serverSocket = new ServerSocket(3000);
            Socket clientSocket = serverSocket.accept();
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            Thread sender = new Thread(new Runnable() {
                String message;

                @Override
                public void run() {
                    while (true) {
                        message = sc.nextLine();
                        writer.println(message);
                        writer.flush();
                    }
                }
            });
            sender.start();

            Thread receive = new Thread(new Runnable() {
                String message;

                @Override
                public void run() {
                    try {
                        message = reader.readLine();
                        while (message != null) {
                            System.out.println("Client : " + message);
                            message = reader.readLine();
                        }

                        System.out.println("Client disconnect");

                        writer.close();
                        clientSocket.close();
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receive.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}