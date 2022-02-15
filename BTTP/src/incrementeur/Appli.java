package incrementeur;

import java.util.LinkedList;

public class Appli {
	public static void main(String args[]) throws InterruptedException {
	    System.out.println("VALEUR " + Incrementeur.getValeur());

		LinkedList<Thread> threads = new LinkedList<>();

	    threads.add(new Thread(new Incrementeur("Thread1")));
		//threads.add(new Thread(new Decrementeur("Thread2")));
		threads.add(new Thread(new Incrementeur("Thread3")));
		//threads.add(new Thread(new Decrementeur("Thread4")));

		for(Thread thread: threads)
			thread.start();

		for(Thread thread: threads)
			thread.join();
	    
	    System.out.println("VALEUR FINALE " + Incrementeur.getValeur());
	  }
}
