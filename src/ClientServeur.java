public class ClientServeur {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Usage: java ClientServeur <host> <portServeur> <portClient>");
            return;
        }

        String host = args[0];
        int portServeur = Integer.parseInt(args[1]);
        int portClient = Integer.parseInt(args[2]);

        Serveur serveur = new Serveur(portClient);
        new Thread(serveur).start();
        Thread.sleep(5000); // Laisse le temps au serveur de démarrer

        Client client = new Client(host, portServeur);
        client.setServeurLocal(serveur); // Permet à show d'accéder au pool local
        client.lancerConsole();
    }
}
