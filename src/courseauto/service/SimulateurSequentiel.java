import java.util.Scanner;

public class SimulateurSequentiel {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GestionCourse gestionCourse = new GestionCourse();
        int choix = -1;

        do {
            System.out.println("==================================================");
            System.out.println("          GESTION DE COURSES AUTOMOBILES          ");
            System.out.println("==================================================");
            System.out.println("1. Ajouter une voiture");
            System.out.println("2. Afficher les voitures");
            System.out.println("3. Rechercher une voiture");
            System.out.println("4. Modifier une voiture");
            System.out.println("5. Supprimer une voiture");
            System.out.println("6. Créer et configurer une course");
            System.out.println("7. Démarrer une course");
            System.out.println("8. Consulter les résultats");
            System.out.println("0. Quitter");
            System.out.println("==================================================");
            System.out.print("Votre choix : ");

            if (scanner.hasNextInt()) {
                choix = scanner.nextInt();
                scanner.nextLine(); // Consommer la fin de ligne

                switch (choix) {
                    case 1:
                        System.out.print("Entrez l'immatriculation de la voiture : ");
                        String immat = scanner.nextLine();
                        System.out.print("Entrez le modèle de la voiture : ");
                        String modele = scanner.nextLine();
                        System.out.println("-> Option 1 sélectionnée.");
                        gestionCourse.ajouterVoiture(immat, modele);
                        System.out.println("-> Succès : La voiture a bien été ajoutée !");
                        break;
                    case 2:
                        System.out.println("\n--- LISTE DES VOITURES ---");
                        gestionCourse.afficherVoitures();
                       break;
                    case 3:
                        System.out.print("Entrez l'immatriculation de la voiture à rechercher : ");
                        String immatRecherche = scanner.nextLine();
                        gestionCourse.rechercherVoiture(immatRecherche);
                         break;
                    case 4:
                        System.out.print("Entrez l'immatriculation de la voiture à modifier : ");
                        String immatModif = scanner.nextLine();
                        System.out.print("Entrez le nouveau modèle : ");
                        String nouveauModele = scanner.nextLine();
                        gestionCourse.modifierVoiture(immatModif, nouveauModele);
                        System.out.println("-> Modification effectuée.");
                        break;
                       
                    case 5:
                         System.out.print("Entrez l'immatriculation de la voiture à supprimer : ");
                        String immatSuppr = scanner.nextLine();
                        gestionCourse.supprimerVoiture(immatSuppr);
                        System.out.println("-> Voiture supprimée.");
                        break;
                        
                    case 6:
                        System.out.print("Entrez le nom ou l'identifiant de la course à configurer : ");
                        String nomCourse = scanner.nextLine();
                        gestionCourse.configurerCourse(nomCourse);
                        System.out.println("-> Course configurée avec succès.");
                        break;
                       
                    case 7:
                        gestionCourse.demarrerCourse();
                        System.out.println("-> La course a démarré !");
                        break;
                    case 8:
                         System.out.println("\n--- RÉSULTATS DE LA COURSE ---");
                            gestionCourse.consulterResultats();
                            break;
                    case 0:
                        System.out.println("Fermeture de l'application...");
                        break;
                    default:
                        System.out.println("Erreur : option invalide (choisissez entre 0 et 8).");
                }
            } else {
                System.out.println("Erreur : veuillez entrer un nombre entier valide.");
                scanner.next(); 
            }
            
            System.out.println(); 
        } while (choix != 0);

        scanner.close();
    }
}