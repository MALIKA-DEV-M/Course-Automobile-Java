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

    public void ajouterVoiture(Voiture voiture) {
        if (voiture == null) {
            throw new IllegalArgumentException("La voiture ne peut pas être nulle.");
        }
        String cle = voiture.getId().toUpperCase();
        if (voitures.containsKey(cle)) {
            throw new IllegalArgumentException("Une voiture avec l'identifiant '" + voiture.getId() + "' existe déjà.");
        }
        voitures.put(cle, voiture);
    }

    public Voiture rechercherVoiture(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        return voitures.get(id.trim().toUpperCase());
    }

    public List<Voiture> listerVoitures() {
        return new ArrayList<>(voitures.values());
    }

    public boolean estVoitureVerrouillee(String idVoiture) {
        return courseActuelle != null
                && !courseActuelle.isEstTerminee()
                && courseActuelle.contientVoiture(idVoiture);
    }

    public void modifierVoiture(String id, String nouveauNom, String nouvelleMarque, double nouvelleVitesse) {
        Voiture voiture = rechercherVoiture(id);
        if (voiture == null) {
            throw new IllegalArgumentException("Aucune voiture trouvée avec l'identifiant '" + id + "'.");
        }
        if (estVoitureVerrouillee(voiture.getId())) {
            throw new IllegalStateException("La voiture '" + voiture.getId() + "' est inscrite à une course non terminée et ne peut pas être modifiée.");
        }
        if (nouveauNom == null || nouveauNom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la voiture ne peut pas être vide.");
        }
        if (nouvelleMarque == null || nouvelleMarque.trim().isEmpty()) {
            throw new IllegalArgumentException("La marque de la voiture ne peut pas être vide.");
        }
        if (nouvelleVitesse <= 0) {
            throw new IllegalArgumentException("La vitesse doit être strictement supérieure à 0.");
        }

        voiture.setNom(nouveauNom);
        voiture.setMarque(nouvelleMarque);
        voiture.setVitesse(nouvelleVitesse);
    }

    public void supprimerVoiture(String id) {
        Voiture voiture = rechercherVoiture(id);
        if (voiture == null) {
            throw new IllegalArgumentException("Aucune voiture trouvée avec l'identifiant '" + id + "'.");
        }
        if (estVoitureVerrouillee(voiture.getId())) {
            throw new IllegalStateException("La voiture '" + voiture.getId() + "' est inscrite à une course non terminée et ne peut pas être supprimée.");
        }
        voitures.remove(voiture.getId().toUpperCase());
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