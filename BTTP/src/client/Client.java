package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public final class Client implements Runnable {
    private static final String HOST = "localhost";
    private int port;

    private static Scanner scanner = new Scanner(System.in);

    private static int cpt = 0;
    private Integer id = null;

    public Client(int port) {
        this.port = port;
        id = cpt++;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(HOST, port);
            System.out.println("### [" + id + "] Connecté au serveur " + HOST + " sur le port " + port);

            PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String requete = null;
            synchronized (scanner) {
                System.out.println("### [" + id + "] Ecrivez votre message à envoyer : ");
                requete = scanner.nextLine();
            }

            socketOut.println(requete);
            String reponse = socketIn.readLine();

            System.out.println("### [" + id + "] Serveur <<< " + requete);
            System.out.println("### [" + id + "] Serveur >>> " + reponse);

            socket.close();
            System.out.println("### [" + id + "] Connexion terminée");
        } catch(Exception e) {
            System.out.println("### [" + id + "] Connexion échouée");
        }
    }
}
