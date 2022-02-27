package servicesClient;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.stream.Collectors;

import bri.ServiceClient;

/**
 * Service inversant un mot donne
 */
public class ServiceInversion extends ServiceClient {
	public ServiceInversion(Socket client) {
		super(client);
	}

	public void activite(BufferedReader in, PrintWriter out) throws IOException {
		out.println("Tapez le texte a inverser");
		out.println(new StringBuffer(Arrays.stream(in.readLine()
				.split("\\s+")).map(t -> t.substring(0, 1).toUpperCase() + t.substring(1))
				.collect(Collectors.joining(" "))).reverse());
	}

	public static String toStringue() {
		return "Inversion de texte 2.0";
	}
}
