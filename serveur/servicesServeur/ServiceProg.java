package servicesServeur;

import bri.ExceptionNorme;
import bri.ServiceClient;
import bri.ServiceRegistry;
import bri.ServiceServeur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

public class ServiceProg implements ServiceServeur {
    private Socket client;

    public ServiceProg(Socket socket) {
        client = socket;
    }

    @Override
    public void run() {
        BufferedReader in;
        PrintWriter out = null;

        try {
            System.out.println("[LOG] Un programmeur s'est connecte");

            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);

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
        catch(ExceptionNorme e) {
            out.println("La norme BRi n'est pas respectee");
            out.println(e.getMessage());
        } catch (MalformedURLException e) {
            out.println("L'URL du serveur FTP est mal forme");
        } catch (ClassNotFoundException e) {
            out.println("La classe fournie ne se trouve pas sur le serveur FTP");
        } catch (IOException e) {}

        out.println("fin");
        System.out.println("[LOG] La connection avec un programmeur a ete interrompu");
    }
}
