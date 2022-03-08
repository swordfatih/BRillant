package servicesClient;

import bri.ServiceClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

/**
 * Service inversant un mot donne
 */
public class ServiceAnalyse extends ServiceClient {
	public ServiceAnalyse(Socket client) {
		super(client);
	}

	public void activite(BufferedReader in, PrintWriter out) throws IOException {
		try {
			out.println("Entrez le chemin du fichier XML à analyser");
			String url = in.readLine();

			out.println("Ecrivez votre adresse mail");
			String mail = in.readLine();

			URL urlObj = new URL(url);
			URLConnection con = urlObj.openConnection();
			InputStream file = con.getInputStream();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document document = db.parse(file);

			NodeList elements = document.getElementsByTagName("brette");

			StringBuilder affichage = new StringBuilder();

			affichage.append("Voici tous les elements <brette> de votre fichier\n");
			for(int i = 0; i < elements.getLength(); ++i) {
				Node e = elements.item(i);
				affichage.append(e.getTextContent());
				affichage.append("\n");
			}

			out.println(affichage);
		} catch(StringIndexOutOfBoundsException e) {
			out.println("Le texte est vide");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public static String toStringue() {
		return "Service d'analyse XML";
	}
}
