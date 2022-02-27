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

public class ServiceProgUpdate extends ServiceProg {
    private String login;

    public ServiceProgUpdate(Socket client, String login) {
        super(client);
        this.login = login;
    }

    public void activite(Socket client, BufferedReader in, PrintWriter out) throws IOException, ReflectiveOperationException, ExceptionNorme {
        out.println(ServiceRegistry.toStringue(false));
        if (ServiceRegistry.getServiceCount() == 0) return;

        out.println("Tapez l'indice du service a mettre a jour");
        int indice = Integer.parseInt(in.readLine()) - 1;
        String classeName = ServiceRegistry.getServiceClass(indice).getName();

        // URLClassLoader sur ftp
        URLClassLoader urlcl = new URLClassLoader(new URL[]{new URL(ServiceProgAuthentification.getProgrammeur(login).getFTP())});
        Class<?> classeChargee = urlcl.loadClass(classeName);
        ServiceRegistry.updateService(indice, classeChargee.asSubclass(ServiceClient.class));

        System.out.println("[LOG] La classe " + classeName + " a ete mise a jour");

        out.println("Le service " + classeName + " a ete mis a jour avec succes");
    }

    public String toString() {
        return "Mettre a jour un service";
    }
}
