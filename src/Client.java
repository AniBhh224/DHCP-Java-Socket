import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private String host;
    private int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String ip;
    private Serveur serveurLocal;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void setServeurLocal(Serveur serveur) {
        this.serveurLocal = serveur;
    }

    public void lancerConsole() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Tapez 'connect' pour demander une IP, 'show' pour voir l'état local, 'exit' pour quitter.");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                close();
                System.exit(0);
            } else if (input.equalsIgnoreCase("show")) {
                if (ip == null) {
                    System.out.println("Aucune IP attribuée par le serveur distant.");
                } else {
                    System.out.println("Adresse IP attribuée : " + ip);
                }

                if (serveurLocal != null) {
                    serveurLocal.afficherPoolLocal();
                } else {
                    System.out.println("(Serveur local non initialisé)");
                }

            } else if (input.equalsIgnoreCase("connect")) {
                envoyerDiscover();

            } else {
                send(input);
            }
        }
    }

    public boolean openConnection() {
        try {
            socket = new Socket(host, port);
            socket.setSoTimeout(3000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connexion TCP établie avec " + host + ":" + port);
            return true;
        } catch (IOException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
            return false;
        }
    }

    private void envoyerDiscover() {
        try {
            if (!isConnected() && !openConnection()) return;

            out.println("DISCOVER");
            String offer = in.readLine();
            if (offer == null) {
                System.out.println("Aucune réponse du serveur.");
                close();
                return;
            }

            System.out.println("Réponse du serveur : " + offer);

            if (offer.startsWith("OFFER ")) {
                ip = offer.substring(6);
                System.out.println("Adresse IP proposée : " + ip);
                out.println("REQUEST " + ip);
                String ack = in.readLine();
                if (ack != null) {
                    System.out.println("Réponse du serveur : " + ack);
                }
            }

        } catch (SocketTimeoutException e) {
            System.out.println("DISCOVER : Timeout.");
        } catch (IOException e) {
            System.out.println("Erreur DISCOVER/REQUEST : " + e.getMessage());
            close();
        }
    }

    public void send(String message) {
        if (socket != null && out != null) {
            try {
                out.println(message);
                String response = in.readLine();

                if (response == null) {
                    System.out.println("Connexion perdue avec le serveur.");
                    close();
                    return;
                }

                if (response.contains("expiré")) {
                    System.out.println(response);
                    ip = null;

                } else if (!response.startsWith("SERVEUR A BIEN REÇU")) {
                    System.out.println("Réponse du serveur : " + response);
                }

            } catch (SocketTimeoutException e) {
                System.out.println("Le serveur n'a pas répondu (timeout).");
            } catch (IOException e) {
                System.out.println("Erreur de lecture : " + e.getMessage());
                close();
            }
        } else {
            System.out.println("Socket invalide.");
        }
    }

    public void close() {
        try {
            if (socket != null) socket.close();
            socket = null;
            out = null;
            in = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public String getIp() {
        return ip;
    }
}
