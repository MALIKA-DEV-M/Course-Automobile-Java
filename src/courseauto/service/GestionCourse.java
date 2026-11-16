package courseauto.service;

import courseauto.modele.Course;
import courseauto.modele.Voiture;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class GestionCourse {
    private final Map<String, Voiture> voitures;
    private Course courseActuelle;
    private Course derniereCourseTerminee;

    public GestionCourse() {
        this.voitures = new LinkedHashMap<>();
        this.courseActuelle = null;
        this.derniereCourseTerminee = null;
    }
    public Course creerCourse(String id, String nom, double distanceTotale) {
        if (this.courseActuelle != null && this.courseActuelle.isEstTerminee()) {
            this.derniereCourseTerminee = this.courseActuelle;
        }
        this.courseActuelle = new Course(id, nom, distanceTotale);
        return this.courseActuelle;
    }
    public void inscrireVoitureACourse(String idVoiture) {
        if (courseActuelle == null) {
            throw new IllegalStateException("Aucune course n'est actuellement configurée. Créez d'abord une course.");
        }
        Voiture voiture = rechercherVoiture(idVoiture);
        if (voiture == null) {
            throw new IllegalArgumentException("Aucune voiture trouvée avec l'identifiant '" + idVoiture + "'.");
        }
        courseActuelle.ajouterParticipant(voiture);
    }

    public void archiverCourseTerminee() {
        if (this.courseActuelle != null && this.courseActuelle.isEstTerminee()) {
            this.derniereCourseTerminee = this.courseActuelle;
        }
    }

    public Course getCourseActuelle() {
        return courseActuelle;
    }
    public Course getDerniersResultatsDisponibles() {
        if (courseActuelle != null && courseActuelle.isEstTerminee()) {
            return courseActuelle;
        }
        return derniereCourseTerminee;
    }
}