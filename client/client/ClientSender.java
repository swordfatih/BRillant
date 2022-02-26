package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientSender extends ClientService {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public ClientSender(ClientBTTP bttp) {
        super(bttp);
    }

    @Override
    public void stop() {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String requete = null;
                if (br.ready() && (requete = br.readLine()) != null) {
                    getBttp().getSocketOut().println(requete);
                    System.out.println("### Client <<< " + requete);
                }
            }
        } catch(IOException e) {
            return;
        }
    }
}
