package bri;

import java.io.*;
import java.net.*;

public class ServeurBRi implements Runnable {
	private ServerSocket listen_socket;
	private Class<? extends ServiceServeur> classService;
	
	// Cree un serveur TCP - objet de la classe ServerSocket
	public ServeurBRi(int port, Class<? extends ServiceServeur> classService) throws IOException {
		listen_socket = new ServerSocket(port);
		this.classService = classService;
	}

	// Le serveur ecoute et accepte les connections.
	// pour chaque connection, il cree un ServiceInversion, 
	// qui va la traiter.
	public void run() {
		while(true) {
			try {
				new Thread(classService.getConstructor(ServerSocket.class).newInstance(listen_socket.accept())).start();
			}
			catch (Exception e) {
				System.err.println("Problème sur le port d'écoute : " + e);
			}
		}
	}

	 // restituer les ressources --> finalize
	protected void finalize() throws Throwable {
		this.listen_socket.close();
	}
}
