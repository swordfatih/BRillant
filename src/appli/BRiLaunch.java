package appli;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;

import bri.ServeurBRi;
import bri.Service;
import bri.ServiceRegistry;

public class BRiLaunch {
	private final static int PORT_SERVICE = 3000;
	
	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		Scanner clavier = new Scanner(System.in);

		// URLClassLoader sur ftp
		String fileDirURL = "ftp://localhost:2121/classes";
		URLClassLoader urlcl = new URLClassLoader(new URL[]{new URL(fileDirURL)});
		
		System.out.println("Bienvenue dans votre gestionnaire dynamique d'activité BRi");
		System.out.println("Pour ajouter une activité, celle-ci doit être présente sur votre serveur ftp");
		System.out.println("A tout instant, en tapant le nom de la classe, vous pouvez l'intégrer");
		System.out.println("Les clients se connectent au serveur 3000 pour lancer une activité");
		
		new Thread(new ServeurBRi(PORT_SERVICE)).start();
		
		while (true){
				try {
					String classeName = clavier.next();
					// charger la classe et la déclarer au ServiceRegistry

					Class<?> classeChargee = urlcl.loadClass(classeName);
					ServiceRegistry.addService(classeChargee.asSubclass(Service.class));
				} catch (Exception e) {
					System.out.println(e);
				}
			}		
	}
}
