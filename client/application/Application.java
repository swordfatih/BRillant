package application;

import client.ClientBTTP;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("-> entrez l'adresse du serveur : ");
        String host = in.nextLine();

        System.out.print("-> entrez le port du serveur : ");
        int port = in.nextInt();

        new Thread(new ClientBTTP(host, port)).start();
    }
}
