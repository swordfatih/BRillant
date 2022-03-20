package servicesServeur;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

/*
    Classe représentant un programmeur
 */
public class Programmeur {
    private URLClassLoader loader;
    private ArrayList<URL> urls;

    private final String mdp;

    public Programmeur(String mdp, String ftp) throws MalformedURLException {
        this.urls = new ArrayList<>();
        this.urls.add(new URL(ftp));
        this.mdp = mdp;

        this.setFTP(ftp);
    }

    public boolean validate(String mdp) {
        return this.mdp.equals(mdp);
    }

    public void setFTP(String ftp) throws MalformedURLException {
        this.urls.set(0, new URL(ftp));
        this.loader = URLClassLoader.newInstance(urls.toArray(new URL[0]));
    }

    public String getFTP() {
        return this.urls.get(0).toString();
    }

    public void addURL(String url) throws MalformedURLException {
        this.urls.add(new URL(url));
        this.loader = URLClassLoader.newInstance(urls.toArray(new URL[0]));
    }

    public URLClassLoader getClassLoader() {
        return loader;
    }
}
