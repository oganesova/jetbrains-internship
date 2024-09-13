import chat.Client;
import chat.Server;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter '1' to create a new chat, '2' to join an existing chat, or '0' to exit:");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter the port number to create a new chat:");
                    int port = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Starting server on port " + port + "...");
                    new Thread(() -> Server.startServer(port)).start();
                    break;

                case 2:
                    System.out.println("Enter the port number to connect to an existing chat:");
                    int connectPort = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Enter your username:");
                    String username = scanner.nextLine();

                    System.out.println("Starting client to connect to port " + connectPort + "...");
                    Client.startClient(connectPort, username);
                    break;

                case 0:
                    System.out.println("Exiting program.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
