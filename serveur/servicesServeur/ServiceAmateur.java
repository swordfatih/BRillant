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
		String line = "";
		while(!line.startsWith("end")) {
			out.println(ServiceRegistry.toStringue(true));

			// il n'y a pas de service installe
			if (ServiceRegistry.getServiceCount() == 0) break;

			out.println("Ecrivez 'end' pour quitter la platforme");
			out.println("Tapez le numero du service desire");

			line = in.readLine();

			if (!line.startsWith("end")) {
				int choix = Integer.parseInt(line);

				if (choix < 1 || choix > ServiceRegistry.getServiceCount()) {
					out.println("L'indice ne correspond a aucun service");
					continue;
				}

				// instancier le service numéro "choix" en lui passant la socket "client"
				Class<? extends ServiceClient> classe = ServiceRegistry.getServiceClass(choix - 1);
				ServiceClient s = classe.getDeclaredConstructor(Socket.class).newInstance(client);

				// invoquer run() pour cette instance ou la lancer dans un thread à part
				s.run();
			}
		}

		out.println("fin");
	}

	public String getUtilisateur() {
		return "amateur";
	}
}
