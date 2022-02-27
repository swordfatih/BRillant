package servicesServeur;

import bri.ServiceClient;
import bri.ServiceRegistry;
import bri.ServiceServeur;

import java.io.*;
import java.net.*;

/**
 * Le service des amateurs
 * Permet d'utiliser un service installe
 */
public class ServiceAmateur extends ServiceServeur {
	public ServiceAmateur(Socket client) {
		super(client);
	}

	public void activite(Socket client, BufferedReader in, PrintWriter out) throws NumberFormatException, IOException, ReflectiveOperationException {
		out.println(ServiceRegistry.toStringue(true));

		// il n'y a pas de service installe
		if (ServiceRegistry.getServiceCount() == 0) return;

		out.println("Tapez le numero du service desire");
		int choix = Integer.parseInt(in.readLine());

		if (choix < 1 || choix > ServiceRegistry.getServiceCount()) {
			out.println("Le numero donne ne correspond a aucun service");
			out.println("fin");
			return;
		}

		// instancier le service numéro "choix" en lui passant la socket "client"
		Class<? extends ServiceClient> classe = ServiceRegistry.getServiceClass(choix);
		ServiceClient s = classe.getDeclaredConstructor(Socket.class).newInstance(client);

		// invoquer run() pour cette instance ou la lancer dans un thread à part
		s.run();
	}

	public String getUtilisateur() {
		return "amateur";
	}
}
