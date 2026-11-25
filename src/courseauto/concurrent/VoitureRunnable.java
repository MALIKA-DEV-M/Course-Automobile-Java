package courseauto.concurrent;

import courseauto.modele.Participation;

public class VoitureRunnable implements Runnable {

    private final Participation participation;
    private final double distanceTotale;
    private final ClassementPartage classementPartage;
    private final long delaiMs = 500;

    public VoitureRunnable(Participation participation, double distanceTotale, ClassementPartage classementPartage) {
        this.participation = participation;
        this.distanceTotale = distanceTotale;
        this.classementPartage = classementPartage;
    }

    @Override
    public void run() {
        while (!participation.isEstArrivee()) {
            participation.avancer(distanceTotale);

            System.out.printf("[%s] %.1f / %.1f km (Tour %d)%n",
                    participation.getVoiture().getNom(),
                    participation.getDistanceParcourue(),
                    distanceTotale,
                    participation.getNbTours());

            if (participation.isEstArrivee()) {
                break;
            }

            try {
                Thread.sleep(delaiMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[" + participation.getVoiture().getNom() + "] Simulation interrompue.");
                return;
            }
        }

        if (classementPartage != null) {
            classementPartage.enregistrerArrivee(participation);
        }
    }
}