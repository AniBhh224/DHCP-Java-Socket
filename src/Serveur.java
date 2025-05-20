import java.io.*;
import java.net.*;

public class Serveur implements Runnable {
    private int port;

    public Serveur(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("> Serveur à l'écoute sur 0.0.0.0 sur le port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket socket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Message reçu du client : \"" + message + "\"");
            }
        } catch (IOException e) {
            System.out.println("Connexion fermée.");
        }
    }
}
