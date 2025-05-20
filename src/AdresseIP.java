
public class AdresseIP {
    public String ip;
    public boolean allouee;

    public AdresseIP(String ip) {
        this.ip = ip;
        this.allouee = false;
    }
    public int IntIp(){
        String[] parts = ip.split("\\.");
        return (Integer.parseInt(parts[0]) << 24) |
                (Integer.parseInt(parts[1]) << 16) |
                (Integer.parseInt(parts[2]) << 8)  |
                Integer.parseInt(parts[3]);}

    @Override
    public String toString() {
        if (allouee){
            return ip + " - ALLOCATED";
        }
        else{
            return ip + " - FREE";

        }
    }
}

