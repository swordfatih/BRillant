package bttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public final class ServiceBTTP implements Runnable {
    private Socket client;
    private int clientId;

    private BufferedReader socketIn = null;
    private PrintWriter socketOut = null;

    private ArrayList<Service> services;

    public ServiceBTTP(Socket client, int clientId) {
        this.client = client;
        this.clientId = clientId;

        services = new ArrayList<>();

        services.add(new ServiceBTTPInversion(this));
        services.add(new ServiceBTTPFin(this));
    }

    @Override
    public void run() {
        try {
            outputFormat("Connexion reçu");

            socketIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
            socketOut = new PrintWriter(client.getOutputStream(), true);

            while(true) {
                String menu = getMenu();
                socketOut.println(inputFormat(menu));

                String reponse = socketIn.readLine();

                int i = outputFormat(reponse).charAt(0) - '0';
                if(Character.isDigit(reponse.charAt(0)) && i >= 0 && i < services.size())
                    if (services.get(i).run()) return;
            }
        } catch(IOException o) {
            try {
                this.client.close();
            } catch (IOException e) {
                outputFormat("Erreur par rapport à la connexion");
            }
        }
    }

    private String getMenu() {
        StringBuilder affichage = new StringBuilder();
        affichage.append("Choisissez le service : ");

        for(int i = 0; i < services.size(); ++i) {
            affichage.append("(" + i + ") " + services.get(i));

            if(i != services.size() - 1)
                affichage.append(", ");
        }

        return affichage.toString();
    }

    public PrintWriter getSocketOut() {
        return socketOut;
    }

    public BufferedReader getSocketIn() {
        return socketIn;
    }

    public int getClientId() {
        return clientId;
    }

    public String inputFormat(String message) {
        //System.out.println("--- [" + clientId + "] Client <<< " + message);
        return message;
    }

    public String outputFormat(String message) {
        //System.out.println("--- [" + clientId + "] Client >>> " + message);
        return message;
    }

    public Socket getClient() {
        return client;
    }
}
