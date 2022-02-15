package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public final class ServiceInversion implements Runnable {
    private Socket client;
    private int id;

    public ServiceInversion(Socket client, int id) {
        this.client = client;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            System.out.println("--- [" + id + "] Connexion client reçue");

            BufferedReader socketIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter socketOut = new PrintWriter(client.getOutputStream(), true);

            String requete = socketIn.readLine();
            String reponse = new StringBuffer(requete).reverse().toString();
            socketOut.println(reponse);

            System.out.println("--- [" + id + "] Client >>> " + requete);
            System.out.println("--- [" + id + "] Client <<< " + reponse);

            client.close();
            System.out.println("--- [" + id + "] Connexion terminée");
        } catch(IOException o) {
            try {
                this.client.close();
            }
            catch (IOException e) {
                System.out.println("--- [" + id + "] Erreur par rapport à la connexion");
            }
        }
    }
}
