package bri;

import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * ServiceRegistry
 *
 * Cette classe est un registre de services
 * partagee en concurrence par les clients
 * et les "ajouteurs" de services
 */
public class ServiceRegistry {
	// Un Vector pour cette gestion est pratique
	private static final List<Class<? extends ServiceClient>> servicesClasses;
	static { servicesClasses = new Vector<>(); }

	// ajoute une classe de service apres controle de la norme BLTi
	public static void addService(Class<? extends ServiceClient > classe) throws ExceptionNorme {
		// heriter de la classe abstraite bri.ServiceClient
		if(!classe.getSuperclass().getName().equals(ServiceClient.class.getName()))
			throw new ExceptionNorme("La classe doit heriter de la classe abstraite bri.ServiceClient");

		// ne pas etre abstract
		if(Modifier.isAbstract(classe.getModifiers()))
			throw new ExceptionNorme("La classe ne doit pas etre abstract");

		// etre publique
		if(!Modifier.isPublic(classe.getModifiers()))
			throw new ExceptionNorme("La classe doit etre publique");

		// avoir un constructeur public (Socket) sans exception
		if(Arrays.stream(classe.getConstructors()).noneMatch(c -> Modifier.isPublic(c.getModifiers()) && c.getParameterCount() == 1 && c.getParameterTypes()[0] == Socket.class && c.getExceptionTypes().length == 0))
			throw new ExceptionNorme("La classe doit avoir un constructeur public (Socket) sans exception");

		// avoir un attribut Socket private final
		if(Arrays.stream(classe.getSuperclass().getDeclaredFields()).noneMatch(f -> f.getType() == Socket.class && Modifier.isPrivate(f.getModifiers()) && Modifier.isFinal(f.getModifiers())))
			throw new ExceptionNorme("La classe parent doit avoir un attribut Socket private final");

		// avoir une méthode public static String toStringue() sans exception
		if(Arrays.stream(classe.getMethods()).noneMatch(m -> m.getName().equals("toStringue") && Modifier.isStatic(m.getModifiers()) && Modifier.isPublic(m.getModifiers()) && m.getReturnType() == String.class && m.getExceptionTypes().length == 0))
			throw new ExceptionNorme("La classe doit avoir une méthode public static String toStringue() sans exception");

		// ne pas etre deja ajoute
		if(!servicesClasses.stream().noneMatch(c -> c.getName().equals(classe.getName())))
			throw new ExceptionNorme("La classe doit ne pas etre deja installe");

		// la classe respecte la norme BRi, on l'ajoute
		servicesClasses.add(classe);
	}
	
	// renvoie la classe du service se trouvant a l'indice (n - 1)
	public static Class<? extends ServiceClient> getServiceClass(int numService) {
		synchronized (servicesClasses) {
			return servicesClasses.get(numService - 1);
		}
	}

	// renvoie le nombre de service installes
	public static int getServiceCount() {
		synchronized (servicesClasses) {
			return servicesClasses.size();
		}
	}
	
	// liste les services installes
	public static String toStringue() {
		StringBuilder result = new StringBuilder();

		result.append("*****************************************\n");
		result.append("Liste des services installes\n");

		synchronized (servicesClasses) {
			for (int i = 0; i < servicesClasses.size(); ++i) {
				result.append("[");
				result.append(i + 1);
				result.append("] ");

				try {
					result.append(servicesClasses.get(i).getMethod("toStringue").invoke(null));
				} catch (ReflectiveOperationException e) {
					result.append(servicesClasses.get(i).getName());
				}

				result.append("\n");
			}

			if(servicesClasses.isEmpty())
				result.append("[INFO] Aucun service n'est encore installe\n");
		}

		result.append("*****************************************");

		return result.toString();
	}
}
