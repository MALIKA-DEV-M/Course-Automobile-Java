package courseauto.concurrent;

import courseauto.modele.Participation;

public class VoitureRunnable implements Runnable {

    private final Participation participation;
    private final double distanceTotale;
    private final long delaiMs = 5000; 

    public VoitureRunnable(Participation participation, double distanceTotale) {
        this.participation = participation;
        this.distanceTotale = distanceTotale;
        
    }

    @Override
    public void run() {
        while (!participation.isEstArrivee()) { 
            participation.avancer(distanceTotale);

            System.out.printf("[%s] Tour %d : %.1f / %.1f km%n",
                    participation.getVoiture().getId(),
                    participation.getNbTours(),
                    participation.getDistanceParcourue(),
                    distanceTotale);

            if (participation.isEstArrivee()) {
                break;
            }

            try {
                Thread.sleep(delaiMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[" + participation.getVoiture().getId() + "] Simulation interrompue.");
                return;
            }
        }

        System.out.println(">>> [" + participation.getVoiture().getId() + "] "
                + participation.getVoiture().getNom() + " a terminé la course !");
    }
}