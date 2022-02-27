package servicesServeur;

import bri.ExceptionNorme;
import bri.ServiceClient;
import bri.ServiceRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

public class ServiceProgFTP extends ServiceProg {
    private String login;

    public ServiceProgFTP(Socket client, String login) {
        super(client);
        this.login = login;
    }

    public void activite(Socket client, BufferedReader in, PrintWriter out) throws IOException, ReflectiveOperationException, ExceptionNorme {
        out.println("Indiquez la nouvelle adresse de votre serveur FTP");
        String ftp = in.readLine();
        ServiceProgAuthentification.getProgrammeur(login).setFTP(ftp);
        out.println("Votre adresse FTP a ete mis a jour");
    }

    public String toString() {
        return "Declarer un changement d'adresse de votre serveur FTP";
    }
}
