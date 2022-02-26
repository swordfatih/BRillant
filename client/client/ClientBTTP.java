package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public final class ClientBTTP implements Runnable {
    // private static final String HOST = "localhost"; // 172.19.33.231
    private String host;
    private int port;

    private static int cpt = 0;

    private Socket socket = null;
    private BufferedReader socketIn = null;
    private PrintWriter socketOut = null;

    ArrayList<ClientService> services;

    public ClientBTTP(String host, int port) {
        this.host = host;
        this.port = port;

        services = new ArrayList<>();
        services.add(new ClientSender(this));
        services.add(new ClientListener(this));
    }

    @Override
    public void run() {
        try {
            socket = new Socket(host, port);
            System.out.println("### Connecté au serveur " + host + " sur le port " + port);

            socketOut = new PrintWriter(socket.getOutputStream(), true);
            socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            for(ClientService s: services)
                new Thread(s).start();
        } catch(Exception e) {
            System.out.println("### Connexion échouée");
        }
    }

    public PrintWriter getSocketOut() {
        return socketOut;
    }

    public BufferedReader getSocketIn() {
        return socketIn;
    }

    public Socket getSocket() {
        return socket;
    }

    public ArrayList<ClientService> getServices() {
        return services;
    }
}
