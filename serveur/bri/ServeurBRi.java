package bri;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurBRi implements Runnable {
	private final ServerSocket listen_socket;
	private final Class<? extends ServiceServeur> classService;
	
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
				System.out.println("[LOG] Le service " + classService.getName() + " est en attente d'un nouveau client");
				new Thread(classService.getConstructor(Socket.class).newInstance(listen_socket.accept())).start();
			} catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
				System.out.println("[LOG] Le service " + classService.getName() + " n'a pas pu accepter un client");
			}
		}
	}

	// restituer les ressources --> finalize
	@SuppressWarnings("deprecation")
	protected void finalize() throws Throwable {
		this.listen_socket.close();
	}
}
