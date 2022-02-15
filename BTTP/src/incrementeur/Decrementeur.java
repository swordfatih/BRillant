package incrementeur;

public class Decrementeur implements Runnable {
    private String name;
    public Decrementeur(String name) {
        this.name = name;
    }

    public static int getValeur() {
        return Compteur.get().getValeur();
    }

    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("valeur a decrementer (round " + i + ") : " + Compteur.get().getValeur() + " par " + name);

            synchronized (Compteur.get()) {
                Compteur.get().decrementer();
            }

            System.out.println("valeur calculee (round " + i + ") par " + name + " est : " + Compteur.get().getValeur());
        }
    }
}
