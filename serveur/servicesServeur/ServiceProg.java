package servicesServeur;

import bri.ExceptionNorme;
import bri.ServiceServeur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServiceProg extends ServiceServeur {
    public ServiceProg(Socket client) {
        super(client);
    }

    public void activite(Socket client, BufferedReader in, PrintWriter out) throws IOException, ReflectiveOperationException, ExceptionNorme {
        ServiceProgAuthentification authentification = new ServiceProgAuthentification(client);
        authentification.activite(client, in, out);
        String login = authentification.getProgrammeur();
        if(login == null) return;

        out.println("********************************************************************************");
        out.println("* Bienvenue dans votre gestionnaire dynamique d'activite BRi                   *");
        out.println("* Pour ajouter une activite, celle-ci doit etre presente sur votre serveur FTP *");
        out.println("* A tout instant, en tapant le nom de la classe, vous pouvez l'integrer        *");
        out.println("* Les clients se connectent au serveur 3000 pour lancer une activite           *");
        out.println("********************************************************************************");

        List<ServiceProg> services = new ArrayList<>();
        services.add(new ServiceProgInstallation(client, login));
        services.add(new ServiceProgDesinstallation(client));
        services.add(new ServiceProgActivation(client));
        services.add(new ServiceProgDesactivation(client));
        services.add(new ServiceProgUpdate(client, login));
        services.add(new ServiceProgFTP(client, login));
        services.add(new ServiceProgJar(client, login));

        int choix = -1;
        while(choix != services.size()) {
            try {
                out.println("********");
                out.println("* Menu *");
                out.println("********");

                for(int i = 0; i < services.size(); ++i)
                    out.println("[" + i + "] " + services.get(i).toString());

                out.println("[" + services.size() + "] Quitter le gestionnaire");
                out.println("********");

                out.println("Indiquez votre choix");
                choix = Integer.parseInt(in.readLine());

                if(choix < 0 || choix > services.size())
                    throw new NumberFormatException();
                else if(choix != services.size())
                    services.get(choix).activite(client, in, out);
            } catch(ExceptionNorme e) {
                out.println("La norme BRi n'est pas respectee (" + e.getMessage() + ")");
            } catch (ReflectiveOperationException e) {
                out.println("La classe fournie ne se trouve pas sur le serveur FTP");
            } catch (NumberFormatException e) {
                out.println("Aucun service ne correspond a l'indice donne");
            } catch (NoClassDefFoundError e) {
                out.println("Une classe n'a pas ete trouve, veuillez verifiez que vous avez bien charge les bibliotheques requises pour ce service");
            }
        }
    }

    public String getUtilisateur() {
        return "programmeur";
    }
}
