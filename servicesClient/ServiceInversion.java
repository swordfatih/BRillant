package servicesClient;

import java.io.*;
import java.net.*;

import bri.ServiceClient;

/**
 * Service inversant un mot donne
 */
public class ServiceInversion implements ServiceClient {
	
	private final Socket client;
	
	public ServiceInversion(Socket socket) {
		client = socket;
	}

	@Override
	public void run() throws IOException {
		try {BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);

			out.println("Tapez un texte a inverser");
		
			String line = in.readLine();		
	
			String invLine = new String (new StringBuffer(line).reverse());
			
			out.println(invLine);
			
			client.close();
		}
	}

	// restituer les ressources
	@SuppressWarnings("deprecation")
	protected void finalize() throws Throwable {
		 client.close(); 
	}

	public static String toStringue() {
		return "Inversion de texte";
	}
}
