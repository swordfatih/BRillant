package servicesServeur;

import bri.ExceptionNorme;
import bri.ServiceRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ServiceProgDesactivation extends ServiceProg {
    public ServiceProgDesactivation(Socket client) {
        super(client);
    }

    public void activite(Socket client, BufferedReader in, PrintWriter out) throws IOException, ReflectiveOperationException, ExceptionNorme {
        out.println(ServiceRegistry.toStringue(true));
        if (ServiceRegistry.getActiveCount() == 0) return;
        out.println("Tapez l'indice du service a desactiver");
        ServiceRegistry.setActifService(Integer.parseInt(in.readLine()) - 1, false);
        out.println("Le service a ete desactive");
    }

    public String toString() {
        return "Desactiver un service";
    }
}
