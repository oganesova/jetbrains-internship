package chat;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server {
    private static Set<ClientHandler> clientHandlers = new HashSet<>();

    public static void startServer(int port) {
        System.out.println("Server started on port " + port + "...");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("New client connected");
                    ClientHandler clientHandler = new ClientHandler(socket);
                    clientHandlers.add(clientHandler);
                    new Thread(clientHandler).start();
                } catch (IOException e) {
                    System.out.println("Error accepting client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error starting server: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                username = in.readLine();
                broadcastMessage("User " + username + " has joined the chat");

                String message;
                while ((message = in.readLine()) != null) {
                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    broadcastMessage("[" + timeStamp + "] " + username + ": " + message);
                }
            } catch (SocketException e) {
                System.out.println("User " + username + " has left the chat");
            } catch (IOException e) {
                System.out.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error closing connection: " + e.getMessage());
                }
                clientHandlers.remove(this);
                broadcastMessage("User " + username + " has left the chat");
            }
        }

        private void broadcastMessage(String message) {
            System.out.println(message);
            for (ClientHandler handler : clientHandlers) {
                handler.out.println(message);
            }
        }
    }
}
