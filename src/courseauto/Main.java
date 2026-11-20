package courseauto;

import courseauto.concurrent.SimulateurConcurrent;
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

    public static void executerModeInteractif() {
        int choix = -1;

        do {
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
            System.out.println("  11. Lancer la simulation concurrente");
            System.out.println("  0. Quitter");
            System.out.println("================================================");
            System.out.print("Votre choix : ");

            if (scanner.hasNextInt()) {
                choix = scanner.nextInt();
                scanner.nextLine(); // Consomme le retour à la ligne

                try {
                    switch (choix) {
                        case 1:
                            System.out.println("\n--- Ajout d'une voiture ---");
                            System.out.print("Identifiant (ex: V1) : ");
                            String idAjout = scanner.nextLine();
                            System.out.print("Nom du modèle (ex: SF-24) : ");
                            String nomAjout = scanner.nextLine();
                            System.out.print("Marque (ex: Ferrari) : ");
                            String marqueAjout = scanner.nextLine();
                            System.out.print("Vitesse en km/tour (> 0) : ");
                            double vitesseAjout = Double.parseDouble(scanner.nextLine().replace(',', '.'));

                            Voiture vAjout = new Voiture(idAjout, nomAjout, marqueAjout, vitesseAjout);
                            gestion.ajouterVoiture(vAjout);
                            System.out.println("[OK] Voiture ajoutée : " + vAjout);
                            break;

                        case 2:
                            System.out.println("\n--- Liste des voitures enregistrées ---");
                            List<Voiture> liste = gestion.listerVoitures();
                            if (liste.isEmpty()) {
                                System.out.println("Aucune voiture enregistrée.");
                            } else {
                                for (Voiture v : liste) {
                                    String verrou = gestion.estVoitureVerrouillee(v.getId()) ? " [INSCRITE - VERROUILLÉE]" : "";
                                    System.out.println("  - " + v + verrou);
                                }
                            }
                            break;

                        case 3:
                            System.out.println("\n--- Modification d'une voiture ---");
                            System.out.print("Identifiant de la voiture à modifier : ");
                            String idModif = scanner.nextLine();
                            System.out.print("Nouveau nom : ");
                            String nomModif = scanner.nextLine();
                            System.out.print("Nouvelle marque : ");
                            String marqueModif = scanner.nextLine();
                            System.out.print("Nouvelle vitesse (> 0) : ");
                            double vitesseModif = Double.parseDouble(scanner.nextLine().replace(',', '.'));

                            gestion.modifierVoiture(idModif, nomModif, marqueModif, vitesseModif);
                            System.out.println("[OK] Voiture '" + idModif + "' mise à jour avec succès.");
                            break;

                        case 4:
                            System.out.println("\n--- Suppression d'une voiture ---");
                            System.out.print("Identifiant de la voiture à supprimer : ");
                            String idSuppr = scanner.nextLine();

                            gestion.supprimerVoiture(idSuppr);
                            System.out.println("[OK] Voiture '" + idSuppr + "' supprimée.");
                            break;

                        case 5:
                            System.out.println("\n--- Recherche d'une voiture ---");
                            System.out.print("Identifiant de la voiture recherchée : ");
                            String idRecherche = scanner.nextLine();

                            Voiture vRecherche = gestion.rechercherVoiture(idRecherche);
                            if (vRecherche == null) {
                                System.out.println("[INFO] Aucune voiture trouvée avec l'identifiant '" + idRecherche + "'.");
                            } else {
                                String verrou = gestion.estVoitureVerrouillee(vRecherche.getId()) ? " [INSCRITE - VERROUILLÉE]" : "";
                                System.out.println("[OK] Voiture trouvée : " + vRecherche + verrou);
                            }
                            break;

                        case 6:
                            System.out.println("\n--- Création d'une nouvelle course ---");
                            System.out.print("Identifiant de la course (ex: GP1) : ");
                            String idCourse = scanner.nextLine();
                            System.out.print("Nom de la course (ex: Grand Prix de Monaco) : ");
                            String nomCourse = scanner.nextLine();
                            System.out.print("Distance totale en km (> 0) : ");
                            double distance = Double.parseDouble(scanner.nextLine().replace(',', '.'));

                            Course cCreee = gestion.creerCourse(idCourse, nomCourse, distance);
                            System.out.println("[OK] Course configurée : " + cCreee.getNom() + " (" + cCreee.getDistanceTotale() + " km)");
                            break;

                        case 7:
                            System.out.println("\n--- Inscription d'une voiture à la course ---");
                            System.out.print("Identifiant de la voiture à inscrire : ");
                            String idVoiture = scanner.nextLine();

                            gestion.inscrireVoitureACourse(idVoiture);
                            System.out.println("[OK] Voiture '" + idVoiture + "' inscrite à la course.");
                            break;

                        case 8:
                            Course cConsultee = gestion.getCourseActuelle();
                            if (cConsultee == null) {
                                System.out.println("\nAucune course n'est actuellement configurée.");
                            } else {
                                System.out.println("\n" + cConsultee.getDetailsConfiguration());
                            }
                            break;

                        case 9:
                            simulateur.lancerSimulation(gestion.getCourseActuelle());
                            gestion.archiverCourseTerminee();
                            break;

                        case 10:
                            Course derniere = gestion.getDerniersResultatsDisponibles();
                            if (derniere == null) {
                                System.out.println("\nAucune course n'a encore été terminée.");
                            } else {
                                simulateur.afficherClassement(derniere);
                            }
                            break;
                        case 11:
                            executerModeConcurrent();
                            break;

                        case 0:
                            System.out.println("Fermeture de l'application. À bientôt !");
                            break;

                        default:
                            System.out.println("[ERREUR] Choix invalide. Veuillez saisir un numéro entre 0 et 10.");
                            break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\n[ERREUR DE SAISIE] Veuillez entrer un nombre valide (ex: 120.5).");
                } catch (IllegalArgumentException | IllegalStateException e) {
                    System.out.println("\n[REFUS MÉTIER] " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("\n[ERREUR INATTENDUE] " + e.getMessage());
                }

            } else {
                System.out.println("[ERREUR] Veuillez saisir un numéro entre 0 et 11.");
                scanner.next(); // Vide la mauvaise saisie non entière
            }

        } while (choix != 0);
    }
    private static void executerModeConcurrent(){ 

        gestion.creerCourse("C1", "Course concurrente", 100);
       
        Voiture v5 = new Voiture("V5", "Ferrari", "Ferrari", 250);
        Voiture v2 = new Voiture("V2", "Porsche", "Porsche", 220);
        Voiture v3 = new Voiture("V3", "BMW", "BMW", 200);

        
        gestion.ajouterVoiture(v5);
        gestion.ajouterVoiture(v2);
        gestion.ajouterVoiture(v3);

        
        gestion.inscrireVoitureACourse("V5");
        gestion.inscrireVoitureACourse("V2");
        gestion.inscrireVoitureACourse("V3");

    



       SimulateurConcurrent simulateurconcu = new SimulateurConcurrent();

       simulateurconcu.lancer(gestion.getCourseActuelle());
    }   
  
}