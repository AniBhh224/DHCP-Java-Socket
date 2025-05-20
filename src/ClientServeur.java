import java.util.Scanner;

public class ClientServeur {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Usage: java ClientServeur <host> <portServeur> <portClient>");
            return;
        }

        String host = args[0];
        int portServeur = Integer.parseInt(args[1]);
        int portClient = Integer.parseInt(args[2]);

        // Lance le serveur en parallèle
        new Thread(new Serveur(portClient)).start();

        // Prépare le client
        Client client = new Client(host, portServeur);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("connect")) {
                client.connect();
            } else if (input.equalsIgnoreCase("exit")) {
                client.close();
                System.exit(0);
            } else {
                if (client.isConnected()) {
                    client.send(input);
                } else {
                    System.out.println("Non connecté. Tapez \"connect\".");
                }
            }
        }
    }
}
