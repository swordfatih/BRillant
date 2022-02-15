package incrementeur;

public final class Incrementeur implements Runnable {
  private String name;

  private static int cpt = 1;
  private static Object moniteur = new Object();

  public Incrementeur(String name) {
    this.name = name;
  }

  public static int getValeur() {
    return cpt;
  }

  public void run() {
    for (int i = 1; i <= 10; i++) {
      synchronized (moniteur) {
        int c = cpt;

        System.out.println("valeur à incrementer (round " + i + ") : " + Compteur.get().getValeur() + " par " + name);

        c = c + 1;

        System.out.println("valeur calculee (round " + i + ") par " + name + " est : " + Compteur.get().getValeur());

        cpt = c;
      }
    }
  }
}