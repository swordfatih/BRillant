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
                String reponse = null;
                if ((reponse = getBttp().getSocketIn().readLine()) != null) {
                    System.out.println("### [" + getBttp().getClientId() + "] Serveur >>> " + reponse);

                    if (reponse.equals("fin")) {
                        for (ClientService s : getBttp().getServices())
                            s.stop();

                        getBttp().getSocket().close();
                        System.out.println("### [" + getBttp().getClientId() + "] Connexion terminée");
                    }
                }
            }
        } catch (IOException e) {
            return;
        }
    }
}
