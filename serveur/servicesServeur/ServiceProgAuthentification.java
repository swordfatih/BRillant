package servicesServeur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
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

public class ServiceProgAuthentification extends ServiceProg {
    private static final HashMap<String, InformationsProgrammeur> programmeurs; // les programmeurs certifies

    static {
        programmeurs = new HashMap<>();
    }

    private String login;

    public ServiceProgAuthentification(Socket client) {
        super(client);
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

        this.login = login;
    }

    public String getProgrammeur() {
        return login;
    }

    public static InformationsProgrammeur getProgrammeur(String login) {
        return programmeurs.get(login);
    }
}
