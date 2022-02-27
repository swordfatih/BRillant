package bri;

import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.*;

class InformationsService {
	private final Class<? extends ServiceClient> classe;
	private boolean actif;

	public InformationsService(Class<? extends ServiceClient> classe, boolean actif) {
		this.classe = classe;
		this.actif = actif;
	}

	public InformationsService(Class<? extends ServiceClient> classe) {
		this(classe, true);
	}

	public boolean estActif() {
		return actif;
	}

	public void setActif(boolean actif) {
		this.actif = actif;
	}

	public Class<? extends ServiceClient> getClasse() {
		return classe;
	}
}

/**
 * ServiceRegistry
 *
 * Cette classe est un registre de services
 * partagee en concurrence par les clients
 * et les "ajouteurs" de services
 */
public class ServiceRegistry {
	private static final List<InformationsService> servicesClasses;
	static { servicesClasses = new Vector<>(); }

	// ajoute une classe de service apres controle de la norme BRi
	public static void addService(Class<? extends ServiceClient> classe) throws ExceptionNorme {
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

		synchronized (servicesClasses) {
			// ne pas etre deja ajoute
			if(servicesClasses.stream().anyMatch(c -> c.getClasse().getName().equals(classe.getName())))
				throw new ExceptionNorme("La classe doit ne pas etre deja installe");

			// la classe respecte la norme BRi, on l'ajoute
			servicesClasses.add(new InformationsService(classe));
		}
	}

	public static void setActifService(int numService, boolean actif) throws ExceptionNorme {
		synchronized (servicesClasses) {
			if (numService < 1 || numService > servicesClasses.size())
				throw new ExceptionNorme("La classe doit exister pour etre activee");

			servicesClasses.get(numService).setActif(actif);
		}
	}

	public static void removeService(int numService) throws ExceptionNorme {
		synchronized (servicesClasses) {
			if (numService < 1 || numService > servicesClasses.size())
				throw new ExceptionNorme("La classe doit exister pour etre desinstalle");

			servicesClasses.remove(numService);
		}
	}

	public static void updateService(Integer indice, Class<? extends ServiceClient> classe) {
		synchronized (servicesClasses) {
			servicesClasses.set(indice, new InformationsService(classe, servicesClasses.get(indice).estActif()));
		}
	}
	
	// renvoie la classe du service se trouvant a l'indice n
	public static Class<? extends ServiceClient> getServiceClass(int numService) {
		synchronized (servicesClasses) {
			if(numService < 1 || numService > servicesClasses.size())
				throw new NumberFormatException();

			return servicesClasses.get(numService).getClasse();
		}
	}

	// renvoie le nombre de service installes
	public static int getServiceCount() {
		synchronized (servicesClasses) {
			return servicesClasses.size();
		}
	}
	
	// liste les services installes
	// parametre actif: si vrai, seuls les services actifs sont retournes
	public static String toStringue(boolean actifs) {
		StringBuilder result = new StringBuilder();

		result.append("*****************************************\n");
		result.append("Liste des services installes\n");

		synchronized (servicesClasses) {
			for (int i = 0; i < servicesClasses.size(); ++i) {
				if(!actifs || servicesClasses.get(i).estActif()) {
					result.append("[");
					result.append(i + 1);
					result.append("] ");

					try {
						result.append(servicesClasses.get(i).getClasse().getMethod("toStringue").invoke(null));
					} catch (ReflectiveOperationException e) {
						result.append(servicesClasses.get(i).getClasse().getName());
					}

					result.append("\n");
				}
			}

			if(servicesClasses.isEmpty())
				result.append("[INFO] Aucun service n'est encore installe\n");
		}

		result.append("*****************************************");

		return result.toString();
	}
}
