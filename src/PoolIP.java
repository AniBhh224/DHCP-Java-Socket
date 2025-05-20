import java.util.*;

public class PoolIP {
    public List<AdresseIP> pool = new ArrayList<>();

    public void remplirPool(String adresseDebut, String adresseFin) {
        int intIpDebut = IpUtil.IptoInt(adresseDebut);
        int intIpFin = IpUtil.IptoInt(adresseFin);
        for (int i = intIpDebut; i <= intIpFin; i++) {
            pool.add(new AdresseIP(IpUtil.InttoIp(i)));
        }
    }
    public void remplirPool(int adresseDebut, int adresseFin) {
        for (int i = adresseDebut; i <= adresseFin; i++) {
            pool.add(new AdresseIP(IpUtil.InttoIp(i)));
        }
    }

    public void afficherPool() {
        for (AdresseIP ip : pool) {
            System.out.println(ip);
        }
    }

    public AdresseIP allouerAdresse() {
        for (AdresseIP ip : pool) {
            if (!ip.allouee) {
                ip.allouee = true;
                return ip;
            }
        }
        return null;
    }

    public void libererAdresse(String ipStr) {
        for (AdresseIP ip : pool) {
            if (ip.ip.equals(ipStr)) {
                ip.allouee = false;
            }
        }
    }
}
