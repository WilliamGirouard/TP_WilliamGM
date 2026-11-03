package ca.cegep.biblio.model;

public enum Categorie {
    ROMAN(0.25, 10),
    SCIENCE(0.50, 20),
    JEUNESSE(0.10, 5);

    private final double tarif;
    private final int fraisMaximum;

    Categorie(double tarif, int fraisMaximum) {
        this.tarif = tarif;
        this.fraisMaximum = fraisMaximum;
    }

    public double calculerFraisRetard(int joursRetard) {
        double frais = 0;
        if (joursRetard > 0) {
             frais = joursRetard * tarif;
                if (frais > fraisMaximum) {
                    frais = fraisMaximum;
                }
                return frais;
        }
        return frais;
    }
}
