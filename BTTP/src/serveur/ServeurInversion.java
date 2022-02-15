package serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public final class ServeurInversion implements Runnable {
    private int port;
    private int cpt = 0;

    public ServeurInversion(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket serveur = new ServerSocket(port);

            while(true) {
                Socket client = serveur.accept();
                new Thread(new ServiceInversion(client, cpt++)).start();
            }
        } catch(IOException o) {
            System.out.println("--- Initialisation serveur a échoué");
        }
    }
}
