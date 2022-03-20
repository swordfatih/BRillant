package appli;

import bri.*;
import servicesServeur.ServiceAmateur;
import servicesServeur.ServiceProg;

public class BRiLaunch {
	private final static int PORT_PROG = 4000; // Port pour les programmeurs
	private final static int PORT_AMA = 3000; // Pour pour les amateurs

	public static void main(String[] args) throws Exception {
		new Thread(new ServeurBRi(PORT_PROG, ServiceProg.class.asSubclass(ServiceServeur.class))).start();
		new Thread(new ServeurBRi(PORT_AMA, ServiceAmateur.class.asSubclass(ServiceServeur.class))).start();
	}
}
