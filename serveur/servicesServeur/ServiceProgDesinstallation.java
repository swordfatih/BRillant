package servicesServeur;

import bri.ExceptionNorme;
import bri.ServiceRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ServiceProgDesinstallation extends ServiceProg {
    public ServiceProgDesinstallation(Socket client) {
        super(client);
    }

    public void activite(Socket client, BufferedReader in, PrintWriter out) throws IOException, ReflectiveOperationException, ExceptionNorme {
        out.println(ServiceRegistry.toStringue(false));
        out.println("Tapez l'indice du service a desinstalle");
        ServiceRegistry.removeService(Integer.parseInt(in.readLine()) - 1);
        out.println("Le service a ete desinstaller");
    }

    public String toString() {
        return "Desinstaller un service";
    }
}
