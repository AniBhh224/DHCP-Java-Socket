import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Serveur implements Runnable {
    private int port;
    private PoolIP pool = new PoolIP();
    private final long dureeBailMs = 30_000; // Bail de 20 secondes

    public Serveur(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("> Serveur à l'écoute sur le port " + port);
            logToFile("[SERVEUR en écoute sur " + port + "]");

            pool.remplirPool("192.168.1.1", "192.168.1.5");

            // Thread pour libérer les IP expirées toutes les 5s
            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(5000);
                        pool.verifierBauxExpirés();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

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
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            AdresseIP ipOfferte = null;
            AdresseIP ipAttribuee = null;

            while (true) {
                String message = in.readLine();
                if (message == null) break;

                if (ipAttribuee != null && !ipAttribuee.allouee) {
                    ipAttribuee = null;
                }

                if (message.equalsIgnoreCase("DISCOVER")) {
                    logToFile("DISCOVER reçu");

                    if (ipAttribuee != null) {
                        out.println("ERREUR: Une IP vous a déjà été attribuée.");
                        logToFile("DISCOVER refusé : IP déjà attribuée = " + ipAttribuee.ip);
                        continue;
                    }

                    ipOfferte = pool.allouerAdresseTemporairement();
                    if (ipOfferte != null) {
                        out.println("OFFER " + ipOfferte.ip);
                        logToFile("OFFER envoyé : " + ipOfferte.ip);
                    } else {
                        out.println("NO_AVAILABLE_IP");
                        logToFile("Aucune IP disponible.");
                    }

                } else if (message.startsWith("REQUEST")) {
                    String[] parts = message.split(" ");
                    if (parts.length == 2) {
                        String requestedIp = parts[1];
                        logToFile("REQUEST reçu : " + requestedIp);

                        if (ipOfferte != null && requestedIp.equals(ipOfferte.ip) && !ipOfferte.allouee) {
                            ipOfferte.allouee = true;
                            ipOfferte.expiration = System.currentTimeMillis() + dureeBailMs;
                            ipAttribuee = ipOfferte;
                            ipOfferte = null;

                            out.println("ACK " + requestedIp + " LEASE=30s");
                            logToFile("ACK envoyé : " + ipAttribuee.ip);
                        } else {
                            out.println("NACK");
                            logToFile("REQUEST invalide ou IP déjà utilisée.");
                        }
                    } else {
                        out.println("ERREUR_FORMAT_REQUEST");
                        logToFile("ERREUR_FORMAT_REQUEST");
                    }

                } else {
                    if (ipAttribuee != null && ipAttribuee.allouee && !ipAttribuee.estExpiree()) {
                        System.out.println("Message de " + ipAttribuee.ip + " : " + message);
                        out.println("SERVEUR A BIEN REÇU : " + message);
                        logToFile("Message reçu de " + ipAttribuee.ip + " : " + message);
                    } else if (ipAttribuee == null) {
                        out.println("ERREUR: Aucune IP attribuée. Tapez 'connect'.");
                        logToFile("Message refusé : aucune IP attribuée.");
                    } else {
                        out.println("ERREUR: Votre IP n'est plus valide. Tapez 'connect'.");
                        logToFile("Message refusé : IP expirée.");
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Connexion client fermée.");
            logToFile("Connexion client fermée.");
        }
    }

    private synchronized void logToFile(String message) {
        try (FileWriter fw = new FileWriter("dhcp.log", true)) {
            String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
            fw.write("[" + timestamp + "][PORT " + port + "] " + message + "\n");
        } catch (Exception e) {
            System.out.println("Erreur d'écriture dans le log : " + e.getMessage());
        }
    }

    public void afficherPoolLocal() {
        pool.afficherEtat();
    }
}
