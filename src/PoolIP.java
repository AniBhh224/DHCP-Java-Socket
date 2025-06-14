import java.util.*;

public class PoolIP {
    public List<AdresseIP> pool = new ArrayList<>();

    // Remplit la liste pool avec des adresses IP entre debut et fin
    public void remplirPool(String debut, String fin) {
        int ipDebut = IpUtil.IptoInt(debut);
        int ipFin = IpUtil.IptoInt(fin);
        for (int i = ipDebut; i <= ipFin; i++) {
            pool.add(new AdresseIP(IpUtil.InttoIp(i)));
        }
    }

    // Renvoie une adresse libre, sans encore la marquer comme allouée
    public AdresseIP allouerAdresseTemporairement() {
        for (AdresseIP ip : pool) {
            if (!ip.allouee) return ip;
        }
        return null;
    }

    // Libère les IP dont le bail est expiré
    public void verifierBauxExpirés() {
        long now = System.currentTimeMillis();
        for (AdresseIP ip : pool) {
            if (ip.estExpiree()) {
                ip.allouee = false;
                ip.expiration = 0;
                System.out.println("[INFO] Bail expiré → IP libérée : " + ip.ip);
            }
        }
    }

    // Affiche l’état du pool (alloué / libre)
    public void afficherEtat() {
        System.out.println("\n--- État du pool DHCP ---");
        for (AdresseIP ip : pool) {
            if (ip.allouee) {
                long restant = (ip.expiration - System.currentTimeMillis()) / 1000;
                System.out.println(ip.ip + " - ALLOUÉE - expire dans " + restant + "s");
            } else {
                System.out.println(ip.ip + " - LIBRE");
            }
        }
        System.out.println("--------------------------\n");
    }
}
