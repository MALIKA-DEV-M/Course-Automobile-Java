package courseauto.service;

import courseauto.modele.Course;
import courseauto.modele.Participation;

import java.util.List;

public class SimulateurSequentiel {

    public int lancerSimulation(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Aucune course à simuler.");
        }

        course.verifierConfiguration();

        course.setEstDemarree(true);
        double distanceTotale = course.getDistanceTotale();
        List<Participation> participants = course.getParticipants();

        System.out.println("\n========== DÉBUT DE LA COURSE : " + course.getNom() + " ==========");
        System.out.printf("Distance totale : %.1f km | Participants : %d\n", distanceTotale, participants.size());

        int tour = 0;
        boolean toutesArrivees = false;

        while (!toutesArrivees) {
            tour++;
            System.out.println("\n--- Tour " + tour + " ---");
            toutesArrivees = true;

            for (Participation p : participants) {
                p.avancer(distanceTotale);
                System.out.printf("  [%s] %s : %.1f / %.1f km%s\n",
                        p.getVoiture().getId(),
                        p.getVoiture().getNom(),
                        p.getDistanceParcourue(),
                        distanceTotale,
                        p.isEstArrivee() ? " -> ARRIVÉE ! (Tour " + p.getNbTours() + ")" : "");

                if (!p.isEstArrivee()) {
                    toutesArrivees = false;
                }
            }
        }

        course.calculerClassementSequentiel();
        course.setEstTerminee(true);

        System.out.println("\n========== FIN DE LA SIMULATION (" + tour + " tours) ==========\n");
        return tour;
    }

    public void afficherResultats(Course course) {
        if (course == null || !course.isEstTerminee()) {
            System.out.println("Aucun résultat disponible : aucune course n'a encore été terminée.");
            return;
        }

        List<Participation> classement = course.getClassement();
        if (classement.isEmpty()) {
            System.out.println("Le classement de cette course est vide.");
            return;
        }

        int toursTotaux = 0;
        for (Participation p : classement) {
            if (p.getNbTours() > toursTotaux) {
                toursTotaux = p.getNbTours();
            }
        }

        Participation gagnant = classement.get(0);

        System.out.println("====================================================");
        System.out.println("   RÉSULTATS : " + course.getNom() + " (" + course.getDistanceTotale() + " km)");
        System.out.println("====================================================");
        System.out.println("GAGNANT : " + gagnant.getVoiture().getNom() + " [" + gagnant.getVoiture().getId() + "]");
        System.out.println("Nombre total de tours de la simulation : " + toursTotaux);
        System.out.println("----------------------------------------------------");

        int rang = 1;
        for (Participation p : classement) {
            double tempsTheorique = p.getTempsTheorique(course.getDistanceTotale());
            System.out.printf("%d. [%s] %s (%s) - Distance : %.1f km | Tours : %d | Temps théorique : %.2f tours\n",
                    rang,
                    p.getVoiture().getId(),
                    p.getVoiture().getNom(),
                    p.getVoiture().getMarque(),
                    p.getDistanceParcourue(),
                    p.getNbTours(),
                    tempsTheorique);
            rang++;
        }
        System.out.println("====================================================\n");
    }
}
