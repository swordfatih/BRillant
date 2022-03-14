package servicesServeur;

import bri.ExceptionNorme;
import bri.ServiceRegistry;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ServiceProgJar extends ServiceProg {
    private String login;

    public ServiceProgJar(Socket client, String login) {
        super(client);
        this.login = login;
    }

    public void activite(Socket client, BufferedReader in, PrintWriter out) throws IOException, ReflectiveOperationException, ExceptionNorme {
        out.println("Entrez le chemin relative de l'archive jar a partir de l'url: " + ServiceProgAuthentification.getProgrammeur(login).getFTP());

        String chemin = in.readLine();

        loadJar(ServiceProgAuthentification.getProgrammeur(login).getClassLoader(), chemin);

        out.println("L'archive a ete bien installe");
    }

    private void loadJar(URLClassLoader loader, String chemin) throws IOException {
        String ftp = ServiceProgAuthentification.getProgrammeur(login).getFTP();
        ServiceProgAuthentification.getProgrammeur(login).addURL(ftp + chemin);
    }

    public String toString() {
        return "Charger une bibliotheque externe";
    }
}
