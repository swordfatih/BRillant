package servicesServeur;


import bri.ServiceClient;
import bri.ServiceRegistry;
import bri.ServiceServeur;

import java.io.*;
import java.net.*;

public class ServiceAmateur implements ServiceServeur {
	private Socket client;

	public ServiceAmateur(Socket socket) {
		client = socket;
	}

	public void run() {
		try {
			System.out.println("## Un amateur s'est connecté !");

			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);

			out.println(ServiceRegistry.toStringue() + "## Tapez le numéro du service désiré : ");

			int choix = Integer.parseInt(in.readLine());
			
			// instancier le service numéro "choix" en lui passant la socket "client"
			// invoquer run() pour cette instance ou la lancer dans un thread à part
			Class<? extends ServiceClient> classe = ServiceRegistry.getServiceClass(choix);
			ServiceClient s = classe.getDeclaredConstructor(Socket.class).newInstance(client);
			s.run();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void finalize() throws Throwable {
		 client.close(); 
	}
}
