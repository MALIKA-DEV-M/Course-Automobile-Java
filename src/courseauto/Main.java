package courseauto;

import courseauto.modele.Course;
import courseauto.modele.Voiture;
import courseauto.service.GestionCourse;
import courseauto.service.SimulateurSequentiel;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        GestionCourse gestionCourse = new GestionCourse();
        SimulateurSequentiel simulateur = new SimulateurSequentiel();

        boolean modeAuto = false;
        for (String arg : args) {
            if ("--auto".equalsIgnoreCase(arg.trim())) {
                modeAuto = true;
                break;
            }
        }

        if (modeAuto) {
            executerModeAutomatique(gestionCourse, simulateur);
        } else {
            executerModeInteractif(gestionCourse, simulateur);
        }
    }
    private static void executerModeAutomatique(GestionCourse gestionCourse, SimulateurSequentiel simulateur) {
        System.out.println("=== MODE AUTOMATIQUE ACTIVÉ ===");
        try {
            gestionCourse.ajouterVoiture(new Voiture("V1", "SF-24", "Ferrari", 35.0));
            gestionCourse.ajouterVoiture(new Voiture("V2", "RB20", "Red Bull", 36.0));
            gestionCourse.ajouterVoiture(new Voiture("V3", "W15", "Mercedes", 30.0));
            gestionCourse.ajouterVoiture(new Voiture("V4", "MCL38", "McLaren", 35.0)); 

            Course course = gestionCourse.creerCourse("C-AUTO", "Grand Prix Automatique", 100.0);
            gestionCourse.inscrireVoitureACourse("V1");
            gestionCourse.inscrireVoitureACourse("V2");
            gestionCourse.inscrireVoitureACourse("V3");
            gestionCourse.inscrireVoitureACourse("V4");

            System.out.println(course.getDetailsConfiguration());

            simulateur.lancerSimulation(course);
            gestionCourse.archiverCourseTerminee();

            simulateur.afficherResultats(gestionCourse.getDerniersResultatsDisponibles());

        } catch (Exception e) {
            System.err.println("Erreur lors de l'exécution du mode automatique : " + e.getMessage());
            System.exit(1);
        }
    }

    private static void executerModeInteractif(GestionCourse gestionCourse, SimulateurSequentiel simulateur) {
        Scanner scanner = new Scanner(System.in);
        boolean quitter = false;

        while (!quitter) {
            afficherMenu();
            int choix = lireEntier(scanner, "Votre choix : ");

            try {
                switch (choix) {
                    case 1:
                        gererAjoutVoiture(scanner, gestionCourse);
                        break;
                    case 2:
                        gererAffichageVoitures(gestionCourse);
                        break;
                    case 3:
                        gererRechercheVoiture(scanner, gestionCourse);
                        break;
                    case 4:
                        gererModificationVoiture(scanner, gestionCourse);
                        break;
                    case 5:
                        gererSuppressionVoiture(scanner, gestionCourse);
                        break;
                    case 6:
                        gererConfigurationCourse(scanner, gestionCourse);
                        break;
                    case 7:
                        gererDemarrageCourse(gestionCourse, simulateur);
                        break;
                    case 8:
                        simulateur.afficherResultats(gestionCourse.getDerniersResultatsDisponibles());
                        break;
                    case 0:
                        quitter = true;
                        System.out.println("Fermeture de l'application. À bientôt !");
                        break;
                    default:
                        System.out.println("Erreur : Choix invalide. Veuillez saisir un chiffre entre 0 et 8.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Erreur : " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Une erreur inattendue est survenue : " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void afficherMenu() {
        System.out.println("\n========================================");
        System.out.println("    GESTION DE COURSES AUTOMOBILES");
        System.out.println("========================================");
        System.out.println("1. Ajouter une voiture");
        System.out.println("2. Afficher les voitures");
        System.out.println("3. Rechercher une voiture");
        System.out.println("4. Modifier une voiture");
        System.out.println("5. Supprimer une voiture");
        System.out.println("----------------------------------------");
        System.out.println("6. Créer et configurer une course");
        System.out.println("7. Démarrer une course");
        System.out.println("8. Consulter les résultats");
        System.out.println("0. Quitter");
        System.out.println("========================================");
    }

    private static void gererAjoutVoiture(Scanner scanner, GestionCourse gestionCourse) {
        System.out.println("\n--- Ajouter une voiture ---");
        System.out.print("Identifiant : ");
        String id = scanner.nextLine();
        System.out.print("Nom : ");
        String nom = scanner.nextLine();
        System.out.print("Marque : ");
        String marque = scanner.nextLine();
        double vitesse = lireDouble(scanner, "Vitesse (km/tour) : ");

        Voiture v = new Voiture(id, nom, marque, vitesse);
        gestionCourse.ajouterVoiture(v);
        System.out.println("Succès : Voiture ajoutée -> " + v);
    }

    private static void gererAffichageVoitures(GestionCourse gestionCourse) {
        System.out.println("\n--- Liste des voitures enregistrées ---");
        List<Voiture> voitures = gestionCourse.listerVoitures();
        if (voitures.isEmpty()) {
            System.out.println("Aucune voiture enregistrée pour le moment.");
        } else {
            for (Voiture v : voitures) {
                System.out.println("  - " + v);
            }
        }
    }

    private static void gererRechercheVoiture(Scanner scanner, GestionCourse gestionCourse) {
        System.out.println("\n--- Rechercher une voiture ---");
        System.out.print("Identifiant de la voiture : ");
        String id = scanner.nextLine().trim();
        Voiture v = gestionCourse.rechercherVoiture(id);
        if (v == null) {
            System.out.println("Aucune voiture trouvée avec l'identifiant '" + id + "'.");
        } else {
            System.out.println("Voiture trouvée : " + v);
        }
    }

    private static void gererModificationVoiture(Scanner scanner, GestionCourse gestionCourse) {
        System.out.println("\n--- Modifier une voiture ---");
        System.out.print("Identifiant de la voiture à modifier : ");
        String id = scanner.nextLine().trim();
        System.out.print("Nouveau nom : ");
        String nom = scanner.nextLine();
        System.out.print("Nouvelle marque : ");
        String marque = scanner.nextLine();
        double vitesse = lireDouble(scanner, "Nouvelle vitesse (km/tour) : ");

        gestionCourse.modifierVoiture(id, nom, marque, vitesse);
        System.out.println("Succès : La voiture '" + id + "' a été mise à jour.");
    }

    private static void gererSuppressionVoiture(Scanner scanner, GestionCourse gestionCourse) {
        System.out.println("\n--- Supprimer une voiture ---");
        System.out.print("Identifiant de la voiture à supprimer : ");
        String id = scanner.nextLine().trim();
        gestionCourse.supprimerVoiture(id);
        System.out.println("Succès : La voiture '" + id + "' a été supprimée.");
    }

    private static void gererConfigurationCourse(Scanner scanner, GestionCourse gestionCourse) {
        System.out.println("\n--- Créer et configurer une course ---");
        System.out.print("Identifiant de la course : ");
        String id = scanner.nextLine();
        System.out.print("Nom de la course : ");
        String nom = scanner.nextLine();
        double distance = lireDouble(scanner, "Distance totale (km) : ");

        Course course = gestionCourse.creerCourse(id, nom, distance);
        System.out.println("Course créée. Sélectionnez maintenant les voitures participantes (au moins 2).");
        gererAffichageVoitures(gestionCourse);

        while (true) {
            System.out.print("Entrez l'ID d'une voiture à inscrire (ou tapez 'FIN' pour terminer) : ");
            String saisie = scanner.nextLine().trim();
            if ("FIN".equalsIgnoreCase(saisie)) {
                break;
            }
            try {
                gestionCourse.inscrireVoitureACourse(saisie);
                System.out.println("  -> Voiture '" + saisie + "' inscrite !");
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("  -> Impossible d'inscrire cette voiture : " + e.getMessage());
            }
        }

        System.out.println("\n" + course.getDetailsConfiguration());
        if (course.getParticipants().size() < 2) {
            System.out.println("Attention : Moins de 2 voitures sont inscrites. Vous ne pourrez pas démarrer cette course tant que ce minimum n'est pas atteint.");
        }
    }

    private static void gererDemarrageCourse(GestionCourse gestionCourse, SimulateurSequentiel simulateur) {
        Course course = gestionCourse.getCourseActuelle();
        if (course == null) {
            throw new IllegalStateException("Aucune course n'est configurée. Utilisez d'abord l'option 6.");
        }
        simulateur.lancerSimulation(course);
        gestionCourse.archiverCourseTerminee();
    }


    private static int lireEntier(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String ligne = scanner.nextLine().trim();
            try {
                return Integer.parseInt(ligne);
            } catch (NumberFormatException e) {
                System.out.println("Erreur : Veuillez saisir un nombre entier valide.");
            }
        }
    }

    private static double lireDouble(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String ligne = scanner.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(ligne);
            } catch (NumberFormatException e) {
                System.out.println("Erreur : Veuillez saisir un nombre valide (ex: 25.5).");
            }
        }
    }
}
