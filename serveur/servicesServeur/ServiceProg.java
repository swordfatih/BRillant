package servicesServeur;

import bri.ExceptionNorme;
import bri.ServiceClient;
import bri.ServiceRegistry;
import bri.ServiceServeur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

class InformationsProgrammeur {
    private final String mdp;
    private String ftp;

    public InformationsProgrammeur(String mdp, String ftp) {
        this.mdp = mdp;
        this.ftp = ftp;
    }

    public boolean validate(String mdp) {
        return this.mdp.equals(mdp);
    }

    public void setFTP(String ftp) {
        this.ftp = ftp;
    }

    public String getFTP() {
        return ftp;
    }
}

public class ServiceProg extends ServiceServeur {
    private final HashMap<String, InformationsProgrammeur> programmeurs;

    public ServiceProg(Socket client) {
        super(client);

        programmeurs = new HashMap<>();
    }

    public void activite(Socket client, BufferedReader in, PrintWriter out) throws IOException {
        out.println("********************");
        out.println("* Authentification *");
        out.println("********************");
        out.println("1: Se connecter");
        out.println("2: S'inscrire");
        out.println("********************");
        out.println("Indiquez votre choix");

        String choix = in.readLine();

        out.println("***************");
        out.println(" " + (choix.startsWith("1") ? "Connexion" : "Inscription"));
        out.println("***************");

        out.println("Entrez votre login");
        String login = in.readLine();

        out.println("Entrez votre mot de passe");
        String mdp = in.readLine();

        if(login.isEmpty() || mdp.isEmpty()) {
            out.println("Vos identifiants ne peuvent pas etre vides");
            return;
        }

        if(choix.startsWith("1")) {
            if(!programmeurs.containsKey(login) || !programmeurs.get(login).validate(mdp)) {
                out.println("Vos identifiants sont incorrects");
                return;
            }
        } else {
            if(choix.startsWith("2") && programmeurs.containsKey(login)) {
                out.println("Vous avez deja un compte");
                return;
            }

            out.println("Entrez l'adresse de votre serveur FTP");
            String ftp = in.readLine();

            programmeurs.put(login, new InformationsProgrammeur(mdp, ftp));
        }

        out.println("********************************************************************************");
        out.println("* Bienvenue dans votre gestionnaire dynamique d'activite BRi                   *");
        out.println("* Pour ajouter une activite, celle-ci doit etre presente sur votre serveur FTP *");
        out.println("* A tout instant, en tapant le nom de la classe, vous pouvez l'integrer        *");
        out.println("* Les clients se connectent au serveur 3000 pour lancer une activite           *");
        out.println("********************************************************************************");

        choix = "";
        while(!choix.startsWith("7")) {
            try {
                out.println("********");
                out.println("* Menu *");
                out.println("********");
                out.println("1: Installer un nouveau service");
                out.println("2: Mettre a jour un service");
                out.println("3: Declarer un changement d'adresse de votre serveur FTP");
                out.println("4: Demarrer un service");
                out.println("5: Arreter un service");
                out.println("6: Desinstaller un service");
                out.println("7: Quitter le gestionnaire");
                out.println("********************");

                out.println("Indiquez votre choix");
                choix = in.readLine();

                if (choix.startsWith("1")) {
                    // URLClassLoader sur ftp
                    URLClassLoader urlcl = new URLClassLoader(new URL[]{new URL(programmeurs.get(login).getFTP())});

                    out.println("Tapez le nom du service a installer");
                    String classeName = in.readLine();

                    Class<?> classeChargee = urlcl.loadClass(classeName);
                    ServiceRegistry.addService(classeChargee.asSubclass(ServiceClient.class));

                    System.out.println("[LOG] La classe " + classeName + " a ete installe");

                    out.println("Le service " + classeName + " a ete installe avec succes");
                } else if (choix.startsWith("2")) {
                    // URLClassLoader sur ftp
                    URLClassLoader urlcl = new URLClassLoader(new URL[]{new URL(programmeurs.get(login).getFTP())});

                    out.println(ServiceRegistry.toStringue(true));
                    out.println("Tapez l'indice du service a mettre a jour");
                    int indice = Integer.parseInt(in.readLine());
                    String classeName = ServiceRegistry.getServiceClass(indice).getName();

                    Class<?> classeChargee = urlcl.loadClass(classeName);
                    ServiceRegistry.updateService(indice, classeChargee.asSubclass(ServiceClient.class));

                    System.out.println("[LOG] La classe " + classeName + " a ete mise a jour");

                    out.println("Le service " + classeName + " a ete mis a jour avec succes");
                } else if (choix.startsWith("3")) {
                    out.println("Indiquez la nouvelle adresse de votre serveur FTP");
                    String ftp = in.readLine();
                    programmeurs.get(login).setFTP(ftp);
                    out.println("Votre adresse FTP a ete mis a jour");
                } else if (choix.startsWith("4")) {
                    out.println(ServiceRegistry.toStringue(false));
                    out.println("Tapez l'indice du service a activer");
                    ServiceRegistry.setActifService(Integer.parseInt(in.readLine()) - 1, true);
                    out.println("Le service a ete active");
                } else if (choix.startsWith("5")) {
                    out.println(ServiceRegistry.toStringue(true));
                    out.println("Tapez l'indice du service a desactiver");
                    ServiceRegistry.setActifService(Integer.parseInt(in.readLine()) - 1, false);
                    out.println("Le service a ete desactive");
                } else if (choix.startsWith("6")) {
                    out.println(ServiceRegistry.toStringue(true));
                    out.println("Tapez l'indice du service a desinstaller");
                    ServiceRegistry.removeService(Integer.parseInt(in.readLine()) - 1);
                    out.println("Le service a ete desinstaller");
                }
            } catch(ExceptionNorme e) {
                out.println("La norme BRi n'est pas respectee (" + e.getMessage() + ")");
            } catch (ReflectiveOperationException e) {
                out.println("La classe fournie ne se trouve pas sur le serveur FTP");
            }
        }
    }

    public String getUtilisateur() {
        return "programmeur";
    }
}
