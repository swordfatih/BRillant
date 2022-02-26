package appli;

import java.util.Scanner;

import bri.*;
import servicesServeur.ServiceAmateur;
import servicesServeur.ServiceProg;

public class BRiLaunch {
	private final static int PORT_PROG = 4000;
	private final static int PORT_AMA = 3000;

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		Scanner clavier = new Scanner(System.in);
		
		new Thread(new ServeurBRi(PORT_PROG, ServiceProg.class.asSubclass(ServiceServeur.class))).start();
		new Thread(new ServeurBRi(PORT_AMA, ServiceAmateur.class.asSubclass(ServiceServeur.class))).start();
	}
}
