import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            Socket clientSocket = new Socket("127.0.0.1", 3000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());

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

            Thread receiver = new Thread(new Runnable() {
                String message;

                @Override
                public void run() {
                    try {
                        message = reader.readLine();
                        while (message != null) {
                            System.out.println("Server : " + message);
                            message = reader.readLine();
                        }
                        System.out.println("Server out of service");
                        writer.close();
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

