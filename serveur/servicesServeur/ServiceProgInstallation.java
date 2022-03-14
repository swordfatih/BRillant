package servicesServeur;

import bri.ExceptionNorme;
import bri.ServiceClient;
import bri.ServiceRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URLClassLoader;

public class ServiceProgInstallation extends ServiceProg {
    private String login;

    public ServiceProgInstallation(Socket client, String login) {
        super(client);
        this.login = login;
    }

    public void activite(Socket client, BufferedReader in, PrintWriter out) throws IOException, ExceptionNorme, ClassNotFoundException {
        // URLClassLoader sur ftp
        URLClassLoader loader = ServiceProgAuthentification.getProgrammeur(login).getClassLoader();

        out.println("Tapez le nom du service a installer");
        String classeName = in.readLine();

        Class<?> classeChargee = loader.loadClass(classeName);

        try {
            out.println("La methode d'initialisation de votre service est en cours d'execution.");
            out.println("Cela peut prendre un certain temps.");

            Method init = classeChargee.getMethod("init", URLClassLoader.class);
            init.invoke(null, loader);
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }

        ServiceRegistry.addService(classeChargee.asSubclass(ServiceClient.class), login);

        System.out.println("[LOG] La classe " + classeName + " a ete installe");
        out.println("Le service " + classeName + " a ete installe avec succes");
    }

    public String toString() {
        return "Installer un nouveau service";
    }
}
