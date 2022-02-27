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

public class ServiceProgInstallation extends ServiceProg {
    private String login;

    public ServiceProgInstallation(Socket client, String login) {
        super(client);
        this.login = login;
    }

    public void activite(Socket client, BufferedReader in, PrintWriter out) throws IOException, ReflectiveOperationException, ExceptionNorme {
        // URLClassLoader sur ftp
        URLClassLoader urlcl = new URLClassLoader(new URL[]{new URL(ServiceProgAuthentification.getProgrammeur(login).getFTP())});

        out.println("Tapez le nom du service a installer");
        String classeName = in.readLine();

        Class<?> classeChargee = urlcl.loadClass(classeName);
        ServiceRegistry.addService(classeChargee.asSubclass(ServiceClient.class), login);

        System.out.println("[LOG] La classe " + classeName + " a ete installe");

        out.println("Le service " + classeName + " a ete installe avec succes");
    }

    public String toString() {
        return "Installer un nouveau service";
    }
}
