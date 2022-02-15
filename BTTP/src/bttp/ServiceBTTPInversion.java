package bttp;

import java.io.IOException;

public class ServiceBTTPInversion extends Service {
    public ServiceBTTPInversion(ServiceBTTP bttp) {
        super(bttp);
    }

    @Override
    public String toString() {
        return "Inversion";
    }

    @Override
    public boolean run() {
        try {
            String requete = "Tapez une chaîne de caractères";
            getBttp().getSocketOut().println(getBttp().inputFormat(requete));

            String reponse = getBttp().getSocketIn().readLine();
            requete = new StringBuffer(getBttp().outputFormat(reponse)).reverse().toString();

            getBttp().getSocketOut().println(getBttp().inputFormat(requete));

            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
