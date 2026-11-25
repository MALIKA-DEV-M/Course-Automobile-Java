package courseauto;

import courseauto.concurrent.ClassementPartage;
import courseauto.modele.Course;
import courseauto.modele.Participation;
import courseauto.modele.Voiture;
import courseauto.service.GestionCourse;
import courseauto.service.SimulateurSequentiel;

import java.util.List;

public class TestCourseAutomobile {

    public static void main(String[] args) {
        System.out.println("=== EXÉCUTION DES TESTS AUTOMATISÉS ===");

        try {
            test1_CreationVoiture();
            test2_ProgressionEtPlafonnement();
            test3_DetectionArriveeEtFinSimulation();
            test4_ValidationDonneesEtReglesMetier();
            test5_CoherenceClassementSequentiel();
            test6_SynchronisationClassementPartage();

            System.out.println("\n[SUCCÈS] Tous les tests automatisés sont passés (6/6) !");
            System.exit(0);
        } catch (AssertionError | Exception e) {
            System.err.println("\n[ÉCHEC DU TEST] " + e.getMessage());
            e.printStackTrace();
            System.exit(1); // Code de sortie non nul exigé pour faire échouer Jenkins
        }
    }

    // Test 1 : Création d'une voiture et encapsulations
    private static void test1_CreationVoiture() {
        Voiture v = new Voiture("V1", "SF-24", "Ferrari", 120.0);
        verifier("V1".equals(v.getId()), "L'ID de la voiture doit être V1");
        verifier("SF-24".equals(v.getNom()), "Le nom doit être SF-24");
        verifier("Ferrari".equals(v.getMarque()), "La marque doit être Ferrari");
        verifier(v.getVitesse() == 120.0, "La vitesse doit être 120.0");
        System.out.println("[OK] Test 1 : Création d'une voiture");
    }

    // Test 2 : Progression tour par tour et plafonnement à la distance totale
    private static void test2_ProgressionEtPlafonnement() {
        Voiture v = new Voiture("V1", "SF-24", "Ferrari", 120.0);
        Participation p = new Participation(v);
        double distanceCourse = 300.0;

        p.avancer(distanceCourse);
        verifier(p.getDistanceParcourue() == 120.0 && !p.isEstArrivee(), "Tour 1 : doit être à 120 km");

        p.avancer(distanceCourse);
        verifier(p.getDistanceParcourue() == 240.0 && !p.isEstArrivee(), "Tour 2 : doit être à 240 km");

        p.avancer(distanceCourse);
        verifier(p.getDistanceParcourue() == 300.0, "Tour 3 : la distance doit être plafonnée à 300.0 km");
        verifier(p.isEstArrivee(), "Tour 3 : la voiture doit être marquée arrivée");
        verifier(p.getNbTours() == 3, "Tour 3 : le compteur de tours doit valoir 3");

        // Vérifie qu'une voiture arrivée ne progresse plus
        p.avancer(distanceCourse);
        verifier(p.getNbTours() == 3 && p.getDistanceParcourue() == 300.0, "Une voiture arrivée ne doit plus avancer");
        System.out.println("[OK] Test 2 : Progression et plafonnement de la distance");
    }

    // Test 3 : Détection de l'arrivée de tous les participants en simulation séquentielle
    private static void test3_DetectionArriveeEtFinSimulation() {
        Course c = new Course("GP1", "Monza", 200.0);
        c.ajouterParticipant(new Voiture("V1", "Ferrari", "Ferrari", 100.0));
        c.ajouterParticipant(new Voiture("V2", "Renault", "Renault", 50.0));

        SimulateurSequentiel sim = new SimulateurSequentiel();
        sim.lancerSimulation(c);

        verifier(c.isEstTerminee(), "La course doit être marquée terminée");
        for (Participation p : c.getParticipants()) {
            verifier(p.isEstArrivee(), "Tous les participants doivent avoir franchi la ligne");
            verifier(p.getDistanceParcourue() == 200.0, "Toutes les distances finales doivent être à 200.0 km");
        }
        System.out.println("[OK] Test 3 : Détection de l'arrivée et arrêt de la simulation");
    }

    // Test 4 : Validation des données et règles métier
    private static void test4_ValidationDonneesEtReglesMetier() {
        // Vitesse <= 0 refusée
        verifierException(() -> new Voiture("V1", "Test", "Test", 0), "Une vitesse nulle doit être refusée");
        verifierException(() -> new Voiture("V1", "Test", "Test", -10), "Une vitesse négative doit être refusée");

        // Distance <= 0 refusée
        verifierException(() -> new Course("C1", "Test", 0), "Une distance nulle doit être refusée");

        // Doublon d'identifiant refusé
        GestionCourse gestion = new GestionCourse();
        gestion.ajouterVoiture(new Voiture("V1", "Ferrari", "Ferrari", 100));
        verifierException(() -> gestion.ajouterVoiture(new Voiture("v1", "Autre", "Autre", 110)),
                "Un doublon d'identifiant (même insensible à la casse) doit être refusé");

        // Lancement avec moins de 2 voitures refusé
        Course c = gestion.creerCourse("C1", "Monaco", 100);
        gestion.inscrireVoitureACourse("V1");
        SimulateurSequentiel sim = new SimulateurSequentiel();
        verifierException(() -> sim.lancerSimulation(c), "Une course avec 1 seule voiture ne doit pas démarrer");

        System.out.println("[OK] Test 4 : Validation des données et règles métier");
    }

    // Test 5 : Cohérence du classement (tours -> temps théorique -> ID)
    private static void test5_CoherenceClassementSequentiel() {
        Course c = new Course("GP-TEST", "Test Départage", 300.0);
        // Toutes arrivent en 3 tours :
        // V1 : 300 / 120 = 2.50 tours
        // V2 : 300 / 100 = 3.00 tours
        // V0 : 300 / 100 = 3.00 tours (doit passer devant V2 grâce à l'ID "V0" < "V2")
        c.ajouterParticipant(new Voiture("V2", "RedBull", "RedBull", 100.0));
        c.ajouterParticipant(new Voiture("V1", "Ferrari", "Ferrari", 120.0));
        c.ajouterParticipant(new Voiture("V0", "Alpine", "Alpine", 100.0));

        SimulateurSequentiel sim = new SimulateurSequentiel();
        sim.lancerSimulation(c);

        List<Participation> classement = c.getClassement();
        verifier(classement.size() == 3, "Le classement doit contenir 3 voitures");
        verifier("V1".equals(classement.get(0).getVoiture().getId()), "1er doit être V1 (temps théorique 2.50)");
        verifier("V0".equals(classement.get(1).getVoiture().getId()), "2e doit être V0 (départage ID vs V2)");
        verifier("V2".equals(classement.get(2).getVoiture().getId()), "3e doit être V2");

        System.out.println("[OK] Test 5 : Cohérence du classement et départage des ex æquo");
    }

    // Test 6 : Protection contre les doublons dans ClassementPartage (Partie 2)
    private static void test6_SynchronisationClassementPartage() {
        ClassementPartage cp = new ClassementPartage();
        Participation p1 = new Participation(new Voiture("V1", "Ferrari", "Ferrari", 100));
        cp.enregistrerArrivee(p1);
        cp.enregistrerArrivee(p1); // Tentative d'enregistrement multiple

        verifier(cp.getNombreArrivees() == 1, "Une voiture ne doit être enregistrée qu'une seule fois dans le classement partagé");
        System.out.println("[OK] Test 6 : Unicité dans le classement partagé synchronisé");
    }

    // --- Méthodes utilitaires d'assertion ---

    private static void verifier(boolean condition, String messageErreur) {
        if (!condition) {
            throw new AssertionError(messageErreur);
        }
    }

    private static void verifierException(Runnable action, String messageErreur) {
        try {
            action.run();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return; // Exception attendue bien levée
        }
        throw new AssertionError(messageErreur);
    }
}