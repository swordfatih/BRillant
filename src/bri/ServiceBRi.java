package bri;


import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;


class ServiceBRi implements Runnable {
	
	private Socket client;
	
	ServiceBRi(Socket socket) {
		client = socket;
	}

	public void run() {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
			out.println(ServiceRegistry.toStringue()+"## Tapez le numéro de service désiré : ");
			int choix = Integer.parseInt(in.readLine());
			
			// instancier le service numéro "choix" en lui passant la socket "client"
			// invoquer run() pour cette instance ou la lancer dans un thread à part
			Class<? extends Service> classe = ServiceRegistry.getServiceClass(choix);
			Service s = classe.getDeclaredConstructor(Socket.class).newInstance(client);
			s.run();
		}
		catch (IOException e) {
			//Fin du service
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		try {client.close();} catch (IOException e2) {}
	}
	
	protected void finalize() throws Throwable {
		 client.close(); 
	}

	// lancement du service
	public void start() {
		(new Thread(this)).start();		
	}

}
