import java.io.*;
import java.net.*;

public class Client {
    private String host;
    private int port;
    private Socket socket;
    private PrintWriter out;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connecté au serveur " + host + " sur le port " + port);
            return true;
        } catch (IOException e) {
            System.out.println("Connexion échouée : " + e.getMessage());
            return false;
        }
    }

    public void send(String message) {
        if (socket != null && out != null) {
            out.println(message);
        } else {
            System.out.println("Non connecté. Tapez \"connect\" d'abord.");
        }
    }

    public void close() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}
