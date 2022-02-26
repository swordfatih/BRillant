package client;

import java.io.IOException;

public class ClientListener extends ClientService {
    public ClientListener(ClientBTTP bttp) {
        super(bttp);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String reponse;
                if ((reponse = getBttp().getSocketIn().readLine()) != null) {
                    if (reponse.equals("fin")) {
                        for (ClientService s : getBttp().getServices())
                            s.stop();

                        getBttp().getSocket().close();
                        System.out.println("### Connexion terminée");
                        return;
                    }
                    else {
                        System.out.println("### Serveur >>> " + reponse);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("### Connexion interrompu");
        }
    }
}
