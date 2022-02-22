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
        int port = in.nextInt();

        //new Thread(new ServeurBTTP(PORT)).start();
        new Thread(new ClientBTTP(port)).start();
    }
}
