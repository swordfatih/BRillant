package bri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class ServiceClient implements Runnable {
    private final Socket client;

    public ServiceClient(Socket socket) {
        client = socket;
    }

    @Override
    public void run() {
        BufferedReader in;
        PrintWriter out = null;

        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);

            activite(in, out);
        } catch(IOException e) {
            if(out != null) out.println("Une erreur est survenue et la connexion avec le serveur a ete interrompu");
        }

        if(out != null) out.println("fin");
    }

    public abstract void activite(BufferedReader in, PrintWriter out) throws IOException;

    // mettre fin a la connexion avant la destruction
    @SuppressWarnings("deprecation")
    protected void finalize() throws Throwable {
        client.close();
    }
}
