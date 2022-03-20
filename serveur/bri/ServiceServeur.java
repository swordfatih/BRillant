package bri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
    Classe de base pour les services de serveur
 */
public abstract class ServiceServeur implements Runnable {
    private final Socket client; 

    public ServiceServeur(Socket socket) {
        client = socket;
    }

    @Override
    public void run() {
        BufferedReader in;
        PrintWriter out = null;

        System.out.println("[LOG] Un " + this.getUtilisateur() + " s'est connecte");

        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);

            activite(client, in, out);
        } catch(ExceptionNorme e) {
            out.println("La norme BRi n'est pas respectee (" + e.getMessage() + ")");
        } catch (IOException e) {
            if(out != null) out.println("Une erreur est survenue et la connexion avec le serveur a ete interrompu");
        } catch (ReflectiveOperationException e) {
            out.println("La classe fournie ne se trouve pas sur le serveur FTP");
        } catch (NumberFormatException e) {
            if(out != null) out.println("Aucun service ne correspond a l'indice donne");
        }

        if(out != null) out.println("fin");
        System.out.println("[LOG] La connexion avec un " + this.getUtilisateur() + " a ete interrompu");
    }

    public abstract void activite(Socket client, BufferedReader in, PrintWriter out) throws ExceptionNorme, IOException, ReflectiveOperationException, NumberFormatException;

    public abstract String getUtilisateur();

    // mettre fin a la connexion avant la destruction
    @SuppressWarnings("deprecation")
    protected void finalize() throws Throwable {
        client.close();
    }
}
