package chat;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the port number to connect the client:");
        int port = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter your username:");
        String username = scanner.nextLine();

        startClient(port, username);
    }

    public static void startClient(int port, String username) {
        try (Socket socket = new Socket("localhost", port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(username);

            new Thread(() -> {
                String message;
                try {
                    while ((message = in.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (SocketException e) {
                    System.out.println("Connection to the server closed.");
                } catch (IOException e) {
                    System.out.println("Error receiving messages: " + e.getMessage());
                }
            }).start();

            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    String text = scanner.nextLine();
                    if (text.equalsIgnoreCase("exit")) {
                        System.out.println("You have left the chat.");
                        break;
                    }
                    out.println(text);
                }
            }
        } catch (IOException e) {
            System.out.println("Connection error with the server: " + e.getMessage());
        }
    }
}
