public class AdresseIP {
    public String ip;               // L'adresse IP sous forme de String (ex: 192.168.1.3)
    public boolean allouee;         // Indique si l'adresse est allouée ou non
    public long expiration;         // Timestamp de l'expiration du bail en millisecondes

    public AdresseIP(String ip) {
        this.ip = ip;
        this.allouee = false;
        this.expiration = 0;
    }

    // Vérifie si l'adresse est expirée (bail dépassé)
    public boolean estExpiree() {
        long now = System.currentTimeMillis();
        if (!allouee || expiration == 0) return false;

        boolean expiree = now > expiration;
        return expiree;
    }

    @Override
    public String toString() {
        return ip + " - " + (allouee ? "ALLOCATED" : "FREE");
    }
}
