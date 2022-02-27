package servicesServeur;

import bri.ExceptionNorme;
import bri.ServiceClient;
import bri.ServiceRegistry;
import bri.ServiceServeur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

public class ServiceProg extends ServiceServeur {
    public ServiceProg(Socket client) {
        super(client);
    }

    public void activite(Socket client, BufferedReader in, PrintWriter out) throws IOException, ReflectiveOperationException, ExceptionNorme {
        out.println("********************************************************************************");
        out.println("* Bienvenue dans votre gestionnaire dynamique d'activite BRi                   *");
        out.println("* Pour ajouter une activite, celle-ci doit etre presente sur votre serveur FTP *");
        out.println("* A tout instant, en tapant le nom de la classe, vous pouvez l'integrer        *");
        out.println("* Les clients se connectent au serveur 3000 pour lancer une activite           *");
        out.println("********************************************************************************");

        out.println("Tapez l'adresse de votre serveur FTP");
        String fileDirURL = in.readLine();

        // URLClassLoader sur ftp
        URLClassLoader urlcl = new URLClassLoader(new URL[]{new URL(fileDirURL)});

        out.println("Tapez le nom du service a ajouter");
        String classeName = in.readLine();

        Class<?> classeChargee = urlcl.loadClass(classeName);
        ServiceRegistry.addService(classeChargee.asSubclass(ServiceClient.class));

        System.out.println("[LOG] La classe " + classeName + " a ete ajoute");

        out.println("Le service " + classeName + " a ete ajoute avec succes");
    }

    public String getUtilisateur() {
        return "programmeur";
    }
}
