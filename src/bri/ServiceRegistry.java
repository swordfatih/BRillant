package bri;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class ServiceRegistry {
	// cette classe est un registre de services
	// partagée en concurrence par les clients et les "ajouteurs" de services,
	// un Vector pour cette gestion est pratique

	static {
		servicesClasses = new Vector<>();
	}

	private static List<Class<? extends ServiceClient>> servicesClasses;

	// ajoute une classe de service après contrôle de la norme BLTi
	public static void addService(Class<? extends ServiceClient > classe) throws Exception {
		// vérifier la conformité par introspection
		// si non conforme --> exception avec message clair
		// si conforme, ajout au vector

		if(!Modifier.isPublic(classe.getModifiers()))
			throw new Exception("La classe n'est pas publique !!");

		if(Modifier.isAbstract(classe.getModifiers()))
			throw new Exception("La classe est abstract !!");

		if(Arrays.asList(classe.getInterfaces()).stream().filter(i -> i.getName().equals("bri.ServiceClient")).collect(Collectors.toList()).size() == 0)
			throw new Exception("La classe n'implémente pas ServiceClient !!");

		boolean exists = false;
		for(Constructor<?> c : classe.getConstructors()) {
			if(Modifier.isPublic(classe.getConstructors()[0].getModifiers()) && classe.getConstructors()[0].getParameterCount() == 1)
				exists = true;
		}

		if(!exists)
			throw new Exception("IL N'EXISTE PAS DE CONSTRUCTEUR PUBLIC ET SANS PARAMETRE !!");

		exists = false;
		Field[] fields = classe.getDeclaredFields();
		for(Field field : fields) {
			if(field.getClass().getName().equals(Socket.class.getName()))
				exists = true;
		}

		try {
			Method setter = classe.getMethod("toStringue");
		} catch(Exception e) {
			throw new RuntimeException("Il manque le toStringue");
		}

		servicesClasses.add(classe);
		System.out.println("Debug: La classe " + classe.getName() + " a été ajouté");
	}
	
	// renvoie la classe de service (numService -1)
	public static Class<? extends ServiceClient> getServiceClass(int numService) {
		return servicesClasses.get(numService - 1);
	}
	
	// liste les activités présentes
	public static String toStringue() {
		String result = "Activités présentes : ";

		synchronized (servicesClasses) {
			for (int i = 0; i < servicesClasses.size(); ++i) {
				result += " ## " + (i + 1) + " -> " + servicesClasses.get(i).getName();
			}
		}

		result += "\n";

		return result;
	}

}
