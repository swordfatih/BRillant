package bttp;

import java.io.IOException;

public class ServiceBTTPFin extends Service {
    public ServiceBTTPFin(ServiceBTTP bttp) {
        super(bttp);
    }

    @Override
    public String toString() {
        return "Fin";
    }

    @Override
    public boolean run() {
        try {
            String requete = "fin";
            getBttp().getSocketOut().println(getBttp().inputFormat(requete));

            getBttp().getClient().close();

            getBttp().outputFormat("Connexion terminée");

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
