package courseauto.concurrent;

import courseauto.modele.Course;
import courseauto.modele.Participation;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulateurPoolThreads {

    private static final int TAILLE_POOL = 3;

    public void lancer(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Aucune course à lancer.");
        }

        course.verifierConfiguration();
        course.setEstDemarree(true);

        double distanceTotale = course.getDistanceTotale();
        List<Participation> participations = course.getParticipants();
        ClassementPartage classementPartage = new ClassementPartage();

        System.out.println("\n=======================================================");
        System.out.printf("  COURSE CONCURRENTE (Pool fixe de %d threads) : %s (%.1f km)%n",
                TAILLE_POOL, course.getNom(), distanceTotale);
        System.out.println("=======================================================");

        ExecutorService executor = Executors.newFixedThreadPool(TAILLE_POOL);

        for (Participation participation : participations) {
            Runnable tache = new VoitureRunnable(participation, distanceTotale, classementPartage);
            executor.submit(tache);
        }

        // Arrêt ordonné : n'accepte plus de nouvelles tâches et attend la fin des tâches soumises
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            System.out.println("Simulation avec pool interrompue.");
            return;
        }

        course.setEstTerminee(true);
        afficherClassement(course, classementPartage.getClassement());
    }

    private void afficherClassement(Course course, List<Participation> classement) {
        System.out.println("\n=======================================================");
        System.out.printf("  CLASSEMENT (POOL DE THREADS) : %s%n", course.getNom());
        System.out.println("=======================================================");
        int rang = 1;
        for (Participation p : classement) {
            System.out.printf("  %d. [%s] %-15s (%s) | Distance : %.1f km | Tours : %d%n",
                    rang++,
                    p.getVoiture().getId(),
                    p.getVoiture().getNom(),
                    p.getVoiture().getMarque(),
                    p.getDistanceParcourue(),
                    p.getNbTours());
        }
        System.out.println("=======================================================\n");
    }
}