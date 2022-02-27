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

public class ServiceProgActivation extends ServiceProg {
    public ServiceProgActivation(Socket client) {
        super(client);
    }

    public void activite(Socket client, BufferedReader in, PrintWriter out) throws IOException, ReflectiveOperationException, ExceptionNorme {
        out.println(ServiceRegistry.toStringue(false));
        if (ServiceRegistry.getServiceCount() == 0) return;
        out.println("Tapez l'indice du service a activer");
        ServiceRegistry.setActifService(Integer.parseInt(in.readLine()) - 1, true);
        out.println("Le service a ete active");
    }

    public String toString() {
        return "Activer un service";
    }
}
