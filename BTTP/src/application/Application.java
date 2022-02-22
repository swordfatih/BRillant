package application;

import bttp.ServeurBTTP;
import client.ClientBTTP;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        System.out.println("### <- Client");
        System.out.println("--- <- Serveur");
        System.out.println("**************");

        Scanner in = new Scanner(System.in);
        System.out.println("Entrez l'adresse du serveur");
        String host = in.nextLine();
        System.out.println("Entrez le port du serveur");
        int port = in.nextInt();

        //new Thread(new ServeurBTTP(PORT)).start();
        new Thread(new ClientBTTP(host, port)).start();
    }
}
