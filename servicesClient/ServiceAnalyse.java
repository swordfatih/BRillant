package servicesClient;

import bri.ServiceClient;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.Properties;

/**
 * Service inversant un mot donne
 */
public class ServiceAnalyse extends ServiceClient {
	private static final String USERNAME = "bretteworldboss@gmail.com";
	private static final String PASSWORD = "Brette123!";

	private static Class Session;
	private static Class InternetAddress;
	private static Class MimeMessage;
	private static Class Transport;

	public static void init(URLClassLoader loader) {
		try {
			Session = Class.forName("javax.mail.Session", true, loader);
			InternetAddress = Class.forName("javax.mail.internet.InternetAddress", true, loader);
			MimeMessage = Class.forName("javax.mail.internet.MimeMessage", true, loader);
			Transport = Class.forName("javax.mail.Transport", true, loader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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

			affichage.append("Bonjour,\n");
			affichage.append("Voici tous les elements <brette> de votre fichier\n");
			for(int i = 0; i < elements.getLength(); ++i) {
				if(i != 0)
					affichage.append("\n");

				Node e = elements.item(i);
				affichage.append(e.getTextContent());
			}

			out.println(affichage);

			out.println("Nous vous envoyons le resultat par mail.");
			out.println("Cela peut prendre jusqu'a cinq minutes.");

			sendMail(mail, "Resultats de l'analyse XML", affichage.toString());

			out.println("Votre mail a ete envoye avec succes.");
		} catch(StringIndexOutOfBoundsException e) {
			out.println("Le texte est vide");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	private static void sendMail(String mail, String sujet, String contenu) throws ReflectiveOperationException {
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.user", USERNAME);
		prop.put("mail.smtp.password", PASSWORD);
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); // TLS

		Object session = Session.getMethod("getDefaultInstance", Properties.class).invoke(null, prop);
		Object message = MimeMessage.getConstructor(session.getClass()).newInstance(session);
		MimeMessage.getMethod("setFrom", String.class).invoke(message, USERNAME);

		MimeMessage.getMethod("addRecipient", MimeMessage.getSuperclass().getDeclaredClasses()[0], InternetAddress.getSuperclass())
				.invoke(message, MimeMessage.getSuperclass().getDeclaredClasses()[0].getDeclaredField("TO").get(null),
						InternetAddress.getConstructor(String.class).newInstance(mail));

		MimeMessage.getMethod("setSubject", String.class).invoke(message, sujet);
		MimeMessage.getMethod("setText", String.class).invoke(message, contenu);

		Object transport = Session.getMethod("getTransport", String.class).invoke(session, "smtp");
		Transport.getMethod("connect", String.class, int.class, String.class, String.class).invoke(transport, "smtp.gmail.com", 587, USERNAME, PASSWORD);
		Transport.getMethod("sendMessage", MimeMessage.getSuperclass(), java.lang.reflect.Array.newInstance(InternetAddress.getSuperclass(), 0).getClass())
				.invoke(transport, MimeMessage.cast(message), java.lang.reflect.Array.newInstance(InternetAddress.getSuperclass(), 0).getClass().cast(MimeMessage.getSuperclass().getMethod("getAllRecipients").invoke(message)));

		Transport.getMethod("close").invoke(transport);
	}

	public static String toStringue() {
		return "Service d'analyse XML";
	}
}
