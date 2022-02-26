package servicesServeur;

import bri.ServiceClient;
import bri.ServiceRegistry;
import bri.ServiceServeur;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
        try {
            System.out.println("## Un programmeur s'est connecté !");

            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            out.println("Bienvenue dans votre gestionnaire dynamique d'activité BRi");
            out.println("Pour ajouter une activité, celle-ci doit être présente sur votre serveur ftp");
            out.println("A tout instant, en tapant le nom de la classe, vous pouvez l'intégrer");
            out.println("Les clients se connectent au serveur 3000 pour lancer une activité");

            //out.println("## Tapez l'adresse de votre serveur FTP : ");
            // String fileDirURL = in.readLine();

            String fileDirURL = "ftp://localhost:2121/";

            // URLClassLoader sur ftp
            URLClassLoader urlcl = new URLClassLoader(new URL[]{new URL(fileDirURL)});

            out.println("## Tapez le nom du service à ajouter : ");
            String classeName = in.readLine();

            Class<?> classeChargee = urlcl.loadClass(classeName);
            ServiceRegistry.addService(classeChargee.asSubclass(ServiceClient.class));

            out.println("Service " + classeName + " a été ajouté avec succès!");
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }
}
