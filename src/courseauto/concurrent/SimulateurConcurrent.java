package courseauto.concurrent;

import courseauto.modele.Course;
import courseauto.modele.Participation;

import java.util.ArrayList;
import java.util.List;

public class SimulateurConcurrent {

    
 public void lancer( Course course) {
        if (course == null) {
            System.out.println("Aucune course à lancer.");
            return;
        }

       
        double distanceTotale = course.getDistanceTotale();
        List<Participation> participations = course.getParticipants(); 
        List<Thread> threads = new ArrayList<>();

        for (Participation participation : participations) {
            Runnable tache1 = new VoitureRunnable(participation, distanceTotale);
            Thread thread = new Thread(tache1);
            threads.add(thread);
            System.out.println(  thread.getName() + " - " + participation.getVoiture().getId() + " - " + participation.getVoiture().getNom() + " a commencé la course !");
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
                System.out.println(">>> [" + thread.getName() +"] a terminé la course !");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Simulation interrompue.");
                return;
            }
        }
    }
}