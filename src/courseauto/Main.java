package courseauto;

import courseauto.modele.Course;
import courseauto.modele.Voiture;
import courseauto.service.GestionCourse;
import courseauto.service.SimulateurSequentiel;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final GestionCourse gestion = new GestionCourse();
    private static final SimulateurSequentiel simulateur = new SimulateurSequentiel();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        if (args.length > 0 && "--auto".equalsIgnoreCase(args[0])) {
            executerModeAutomatique();
            return;
        }
        executerModeInteractif();
    }

    private static void executerModeAutomatique() {
        System.out.println("=== MODE AUTOMATIQUE : SIMULATION DE COURSE ===");
        try {
            gestion.ajouterVoiture(new Voiture("V1", "SF-24", "Ferrari", 120.0));
            gestion.ajouterVoiture(new Voiture("V2", "RB20", "Red Bull", 100.0));
            gestion.ajouterVoiture(new Voiture("V3", "W15", "Mercedes", 110.0));

            Course course = gestion.creerCourse("GP-AUTO", "Grand Prix de Monza", 300.0);
            gestion.inscrireVoitureACourse("V1");
            gestion.inscrireVoitureACourse("V2");
            gestion.inscrireVoitureACourse("V3");

            System.out.println(course.getDetailsConfiguration());
            simulateur.lancerSimulation(course);
            gestion.archiverCourseTerminee();
            System.out.println("Exécution automatique terminée avec succès.");
        } catch (Exception e) {
            System.err.println("Erreur critique en mode automatique : " + e.getMessage());
            System.exit(1);
        }
    }

    private static void executerModeInteractif() {
        boolean quitter = false;

        while (!quitter) {
            afficherMenuPrincipal();
            int choix = lireEntier("Votre choix : ");

            try {
                switch (choix) {
                    case 1:
                        menuAjouterVoiture();
                        break;
                    case 2:
                        menuListerVoitures();
                        break;
                    case 3:
                        menuModifierVoiture();
                        break;
                    case 4:
                        menuSupprimerVoiture();
                        break;
                    case 5:
                        menuRechercherVoiture();
                        break;
                    case 6:
                        menuCreerCourse();
                        break;
                    case 7:
                        menuInscrireVoiture();
                        break;
                    case 8:
                        menuConsulterCourse();
                        break;
                    case 9:
                        menuLancerSimulation();
                        break;
                    case 10:
                        menuConsulterResultats();
                        break;
                    case 0:
                        quitter = true;
                        System.out.println("Fermeture de l'application. À bientôt !");
                        break;
                    default:
                        System.out.println("[ERREUR] Choix invalide. Veuillez saisir un numéro entre 0 et 10.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("\n[REFUS MÉTIER] " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\n[ERREUR INATTENDUE] " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void afficherMenuPrincipal() {
        System.out.println("\n================ MENU PRINCIPAL ================");
        System.out.println("  --- Gestion des Voitures (F1) ---");
        System.out.println("  1. Ajouter une voiture");
        System.out.println("  2. Lister les voitures");
        System.out.println("  3. Modifier une voiture");
        System.out.println("  4. Supprimer une voiture");
        System.out.println("  5. Rechercher une voiture");
        System.out.println("  --- Gestion de la Course (F2) ---");
        System.out.println("  6. Créer une nouvelle course");
        System.out.println("  7. Inscrire une voiture à la course");
        System.out.println("  8. Consulter la course configurée");
        System.out.println("  --- Simulation & Résultats (F3 & F4) ---");
        System.out.println("  9. Lancer la simulation séquentielle");
        System.out.println("  10. Consulter les derniers résultats");
        System.out.println("  0. Quitter");
        System.out.println("================================================");
    }

    private static void menuAjouterVoiture() {
        System.out.println("\n--- Ajout d'une voiture ---");
        String id = lireTexte("Identifiant (ex: V1) : ");
        String nom = lireTexte("Nom du modèle (ex: SF-24) : ");
        String marque = lireTexte("Marque (ex: Ferrari) : ");
        double vitesse = lireDouble("Vitesse en km/tour (> 0) : ");

        Voiture v = new Voiture(id, nom, marque, vitesse);
        gestion.ajouterVoiture(v);
        System.out.println("[OK] Voiture ajoutée : " + v);
    }

    private static void menuListerVoitures() {
        System.out.println("\n--- Liste des voitures enregistrées ---");
        List<Voiture> liste = gestion.listerVoitures();
        if (liste.isEmpty()) {
            System.out.println("Aucune voiture enregistrée.");
            return;
        }
        for (Voiture v : liste) {
            String verrou = gestion.estVoitureVerrouillee(v.getId()) ? " [INSCRITE - VERROUILLÉE]" : "";
            System.out.println("  - " + v + verrou);
        }
    }

    private static void menuModifierVoiture() {
        System.out.println("\n--- Modification d'une voiture ---");
        String id = lireTexte("Identifiant de la voiture à modifier : ");
        String nom = lireTexte("Nouveau nom : ");
        String marque = lireTexte("Nouvelle marque : ");
        double vitesse = lireDouble("Nouvelle vitesse (> 0) : ");

        gestion.modifierVoiture(id, nom, marque, vitesse);
        System.out.println("[OK] Voiture '" + id + "' mise à jour avec succès.");
    }

    private static void menuSupprimerVoiture() {
        System.out.println("\n--- Suppression d'une voiture ---");
        String id = lireTexte("Identifiant de la voiture à supprimer : ");
        gestion.supprimerVoiture(id);
        System.out.println("[OK] Voiture '" + id + "' supprimée.");
    }

    private static void menuRechercherVoiture() {
        System.out.println("\n--- Recherche d'une voiture ---");
        String id = lireTexte("Identifiant de la voiture recherchée : ");
        Voiture v = gestion.rechercherVoiture(id);
        if (v == null) {
            System.out.println("[INFO] Aucune voiture trouvée avec l'identifiant '" + id + "'.");
            return;
        }
        String verrou = gestion.estVoitureVerrouillee(v.getId()) ? " [INSCRITE - VERROUILLÉE]" : "";
        System.out.println("[OK] Voiture trouvée : " + v + verrou);
    }

    private static void menuCreerCourse() {
        System.out.println("\n--- Création d'une nouvelle course ---");
        String id = lireTexte("Identifiant de la course (ex: GP1) : ");
        String nom = lireTexte("Nom de la course (ex: Grand Prix de Monaco) : ");
        double distance = lireDouble("Distance totale en km (> 0) : ");

        Course c = gestion.creerCourse(id, nom, distance);
        System.out.println("[OK] Course configurée : " + c.getNom() + " (" + c.getDistanceTotale() + " km)");
    }

    private static void menuInscrireVoiture() {
        System.out.println("\n--- Inscription d'une voiture à la course ---");
        String idVoiture = lireTexte("Identifiant de la voiture à inscrire : ");
        gestion.inscrireVoitureACourse(idVoiture);
        System.out.println("[OK] Voiture '" + idVoiture + "' inscrite à la course.");
    }

    private static void menuConsulterCourse() {
        Course c = gestion.getCourseActuelle();
        if (c == null) {
            System.out.println("\nAucune course n'est actuellement configurée.");
            return;
        }
        System.out.println("\n" + c.getDetailsConfiguration());
    }

    private static void menuLancerSimulation() {
        Course c = gestion.getCourseActuelle();
        simulateur.lancerSimulation(c);
        gestion.archiverCourseTerminee();
    }

    private static void menuConsulterResultats() {
        Course derniere = gestion.getDerniersResultatsDisponibles();
        if (derniere == null) {
            System.out.println("\nAucune course n'a encore été terminée.");
            return;
        }
        simulateur.afficherClassement(derniere);
    }

    private static String lireTexte(String invite) {
        System.out.print(invite);
        return scanner.nextLine().trim();
    }

    private static int lireEntier(String invite) {
        while (true) {
            System.out.print(invite);
            String saisie = scanner.nextLine().trim();
            try {
                return Integer.parseInt(saisie);
            } catch (NumberFormatException e) {
                System.out.println("[ERREUR SAISIE] Veuillez entrer un nombre entier valide.");
            }
        }
    }

    private static double lireDouble(String invite) {
        while (true) {
            System.out.print(invite);
            String saisie = scanner.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(saisie);
            } catch (NumberFormatException e) {
                System.out.println("[ERREUR SAISIE] Veuillez entrer un nombre valide (ex: 120.5).");
            }
        }
    }
}