package courseauto.concurrent;

import courseauto.modele.Course;
import courseauto.modele.Participation;
import java.util.ArrayList;
import java.util.List;

public class SimulateurConcurrent {

    public void lancer(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Aucune course à lancer.");
        }

        course.verifierConfiguration();
        course.setEstDemarree(true);

        double distanceTotale = course.getDistanceTotale();
        List<Participation> participations = course.getParticipants();
        ClassementPartage classementPartage = new ClassementPartage();
        List<Thread> threads = new ArrayList<>();

        System.out.println("\n=======================================================");
        System.out.printf("  COURSE CONCURRENTE (1 Thread / Voiture) : %s (%.1f km)%n", course.getNom(), distanceTotale);
        System.out.println("=======================================================");

        for (Participation participation : participations) {
            Runnable tache = new VoitureRunnable(participation, distanceTotale, classementPartage);
            Thread thread = new Thread(tache, "Thread-" + participation.getVoiture().getNom());
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Simulation concurrente interrompue.");
                return;
            }
        }

        course.setEstTerminee(true);
        afficherClassement(course, classementPartage.getClassement());
    }

    private void afficherClassement(Course course, List<Participation> classement) {
        System.out.println("\n=======================================================");
        System.out.printf("  CLASSEMENT CONCURRENT : %s%n", course.getNom());
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