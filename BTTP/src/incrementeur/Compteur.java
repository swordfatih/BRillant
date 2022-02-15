package incrementeur;

public class Compteur {
    private static Compteur cpt;
    private Integer valeur;

    private Compteur(int valeur) {
        this.valeur = valeur;
    }

    public static Compteur get() {
        if(cpt == null)
            cpt = new Compteur(1);

        return cpt;
    }

    public Integer getValeur() {
        return valeur;
    }

    public void incrementer() {
        valeur++;
    }

    public void decrementer() {
        valeur--;
    }
}
