package servicesClient;

import java.io.*;
import java.net.*;
import java.util.Locale;

import bri.ServiceClient;

// rien � ajouter ici
public class ServiceInversion implements ServiceClient {
	
	private final Socket client;
	
	public ServiceInversion(Socket socket) {
		client = socket;
	}
	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);

			out.println("Tapez un texte à inverser");
		
			String line = in.readLine();
			line = line.substring(1,2).toUpperCase() + line.substring(2).toLowerCase();
	
			String invLine = new String (new StringBuffer(line).reverse());

			out.println(invLine);
			
			client.close();
			System.out.println("Service d'inversion fini !");
		}
		catch (IOException e) {
			//Fin du service d'inversion
		}
	}
	
	protected void finalize() throws Throwable {
		 client.close(); 
	}

	public static String toStringue() {
		return "Inversion de texte";
	}
}
