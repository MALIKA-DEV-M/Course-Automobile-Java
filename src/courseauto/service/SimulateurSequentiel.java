package courseauto.service;

import courseauto.modele.Course;
import courseauto.modele.Participation;

import java.util.List;

/**
 * Moteur de simulation séquentielle d'une course automobile (Fonctionnalités F3 et F4).
 * Fait progresser les voitures tour par tour jusqu'à l'arrivée de tous les participants,
 * puis affiche le classement final départagé selon les règles du cahier des charges.
 */
public class SimulateurSequentiel {

    /**
     * Lance et exécute la simulation complète d'une course configurée.
     *
     * @param course La course à simuler (doit contenir au moins 2 voitures)
     * @throws IllegalArgumentException si la course est nulle
     * @throws IllegalStateException    si la course n'est pas valide pour démarrer
     */
    public void lancerSimulation(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Aucune course à simuler.");
        }

        // Vérification des préconditions (au moins 2 voitures inscrites, non démarrée)
        course.verifierConfiguration();
        course.setEstDemarree(true);

        double distanceTotale = course.getDistanceTotale();
        List<Participation> participants = course.getParticipants();

        System.out.println("\n=======================================================");
        System.out.printf("  DÉPART DE LA COURSE : %s (%.1f km)\n", course.getNom(), distanceTotale);
        System.out.println("=======================================================");

        int tour = 0;
        boolean toutesArrivees = false;

        // Boucle de simulation tour par tour (F3)
        while (!toutesArrivees) {
            tour++;
            System.out.printf("\n--- Tour %d ---\n", tour);
            toutesArrivees = true;

            for (Participation p : participants) {
                if (!p.isEstArrivee()) {
                    p.avancer(distanceTotale);
                }

                // Affichage de l'état de la voiture à l'issue du tour
                String statut = p.isEstArrivee() ? " [ARRIVÉE !]" : "";
                System.out.printf("  %-15s (%s) : %.1f / %.1f km%s\n",
                        p.getVoiture().getNom(),
                        p.getVoiture().getId(),
                        p.getDistanceParcourue(),
                        distanceTotale,
                        statut);

                if (!p.isEstArrivee()) {
                    toutesArrivees = false;
                }
            }
        }

        // Clôture de la course et calcul du classement final (F4)
        course.setEstTerminee(true);
        course.calculerClassementSequentiel();

        afficherClassement(course);
    }

    /**
     * Affiche le classement officiel d'une course terminée.
     *
     * @param course La course terminée
     */
    public void afficherClassement(Course course) {
        if (course == null || !course.isEstTerminee()) {
            System.out.println("Aucun classement disponible : la course n'est pas encore terminée.");
            return;
        }
        List<Participation> classement = course.getClassement();
        if (classement.isEmpty()) {
            System.out.println("Aucun participant dans le classement.");
            return;
        }

        // Le nombre total de tours de la simulation correspond au max des tours effectués
        int totalToursSimulation = 0;
        for (Participation p : classement) {
            if (p.getNbTours() > totalToursSimulation) {
                totalToursSimulation = p.getNbTours();
            }
        }

        Participation gagnant = classement.get(0);

        System.out.println("\n=======================================================");
        System.out.printf("  CLASSEMENT FINAL : %s (%.1f km)\n", course.getNom(), course.getDistanceTotale());
        System.out.printf("  GAGNANT : [%s] %s (%s)\n",
                gagnant.getVoiture().getId(),
                gagnant.getVoiture().getNom(),
                gagnant.getVoiture().getMarque());
        System.out.printf("  Nombre total de tours de la simulation : %d tours\n", totalToursSimulation);
        System.out.println("=======================================================");

        int rang = 1;
        for (Participation p : classement) {
            double tempsTheorique = p.getTempsTheorique(course.getDistanceTotale());
            System.out.printf("  %d. [%s] %-15s (%s) | Distance : %.1f km | Tours : %d | Temps théorique : %.2f tours\n",
                    rang++,
                    p.getVoiture().getId(),
                    p.getVoiture().getNom(),
                    p.getVoiture().getMarque(),
                    p.getDistanceParcourue(),
                    p.getNbTours(),
                    tempsTheorique);
        }
        System.out.println("=======================================================\n");
    }
}