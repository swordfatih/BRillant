package servicesServeur;

import bri.ServiceClient;
import bri.ServiceRegistry;
import bri.ServiceServeur;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;

/**
 * Le service des amateurs
 * Permet d'utiliser un service installe
 */
public class ServiceAmateur implements ServiceServeur {
	private final Socket client;

	public ServiceAmateur(Socket socket) {
		client = socket;
	}

	public void run() {
		BufferedReader in;
		PrintWriter out = null;

		try {
			System.out.println("[LOG] Un amateur s'est connecte");

			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);

			out.println(ServiceRegistry.toStringue());

			// il n'y a pas de service installe
			if (ServiceRegistry.getServiceCount() == 0) {
				out.println("fin");
				return;
			}

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
		} catch(NumberFormatException e) {
			out.println("Veuillez entrer un nombre");
		} catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
			out.println("Le service n'a pas pu etre invoque");
		}

		out.println("fin");
		System.out.println("[LOG] La connection avec un amateur a ete interrompu");
	}

	// mettre fin a la connexion avant la destruction
	@SuppressWarnings("deprecation")
	protected void finalize() throws Throwable {
		 client.close(); 
	}
}
