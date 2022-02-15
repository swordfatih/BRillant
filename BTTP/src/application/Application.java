package application;

import bttp.ServeurBTTP;
import client.ClientBTTP;

public class Application {
    private static final int PORT = 3000;

    public static void main(String[] args) {
        System.out.println("### <- Client");
        System.out.println("--- <- Serveur");
        System.out.println("**************");

        //new Thread(new ServeurBTTP(PORT)).start();
        new Thread(new ClientBTTP(PORT)).start();
    }
}
