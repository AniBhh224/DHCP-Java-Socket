public class Main {
    public static void main(String[] args) {
        PoolIP pool = new PoolIP();

        System.out.println("Remplissage du pool...");
        pool.remplirPool("192.168.1.10", "192.168.1.15");
        pool.afficherPool();

        AdresseIP ipAttribuee = pool.allouerAdresse();
        if (ipAttribuee != null) {
            System.out.println("Adresse attribuée : " + ipAttribuee.ip);
        }

        System.out.println("\nÉtat du pool après allocation :");
        pool.afficherPool();

        System.out.println("\nLibération de l'adresse " + ipAttribuee.ip);
        pool.libererAdresse(ipAttribuee.ip);

        System.out.println("\nÉtat final du pool :");
        pool.afficherPool();
    }
}
