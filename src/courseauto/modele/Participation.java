package courseauto.modele;

public class Participation {
    private final Voiture voiture;
    private double distanceParcourue;
    private int nbTours;
    private boolean estArrivee;

    public Participation(Voiture voiture) {
        if (voiture == null) {
            throw new IllegalArgumentException("La voiture associée à la participation ne peut pas être nulle.");
        }
        this.voiture = voiture;
        this.distanceParcourue = 0.0;
        this.nbTours = 0;
        this.estArrivee = false;
    }

    public void avancer(double distanceTotale) {
        if (estArrivee) {
            return; // Une voiture arrivée ne progresse plus
        }

        nbTours++;
        distanceParcourue += voiture.getVitesse();

        if (distanceParcourue >= distanceTotale) {
            distanceParcourue = distanceTotale; // Plafonnement à la distance totale
            estArrivee = true;
        }
    }

    public double getTempsTheorique(double distanceTotale) {
        return distanceTotale / voiture.getVitesse();
    }

    public Voiture getVoiture() {
        return voiture;
    }

    public double getDistanceParcourue() {
        return distanceParcourue;
    }

    public int getNbTours() {
        return nbTours;
    }

    public boolean isEstArrivee() {
        return estArrivee;
    }

    @Override
    public String toString() {
        return String.format("%s : %.1f km (%d tours)%s",
                voiture.getNom(),
                distanceParcourue,
                nbTours,
                estArrivee ? " [ARRIVÉE]" : "");
    }
}