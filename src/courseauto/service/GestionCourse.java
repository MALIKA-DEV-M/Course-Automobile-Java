package courseauto.service;

import courseauto.modele.Course;
import courseauto.modele.Voiture;
import java.util.*;

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
        if (voitures.containsKey(voiture.getId())) {
            throw new IllegalArgumentException("Une voiture avec l'identifiant '" + voiture.getId() + "' existe déjà.");
        }
        voitures.put(voiture.getId(), voiture);
    }


    public Voiture rechercherVoiture(String id) {
    if (id == null || id.trim().isEmpty()) {
        return null;
    }
    return voitures.get(id.trim().toLowerCase());
   }
   private boolean estVoitureDansCourseNonTerminee(String idVoiture) {
        return courseActuelle != null
                && !courseActuelle.isEstTerminee()
                && courseActuelle.contientVoiture(idVoiture);
    }

    
   public void modifierVoiture(String id, String nouveauNom, String nouvelleMarque, double nouvelleVitesse) {
        Voiture voiture = rechercherVoiture(id);
        if (voiture == null) {
            throw new IllegalArgumentException("Aucune voiture trouvée avec l'identifiant '" + id + "'.");
        }
        if (estVoitureDansCourseNonTerminee(id)) {
            throw new IllegalStateException("La voiture avec l'identifiant '" + id + "' est actuellement inscrite à une course non terminée et ne peut pas être modifiée.");
        }
       if (nouveauNom == null || nouveauNom.isEmpty()) {
            throw new IllegalArgumentException("Le nom de la voiture ne peut pas être vide.");
        }
        if (nouvelleMarque == null || nouvelleMarque.isEmpty()) {
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
        if ( voiture == null){
            throw new IllegalArgumentException("Aucune voiture trouvée avec l'identifiant '" + id + "'.");
        }
    }
    public void listerVoituresConsole() {
        if (voitures.isEmpty()) {
            System.out.println("Aucune voiture n'est actuellement enregistrée.");
        } else {
            System.out.println("Liste des voitures enregistrées :");
            for (Map.Entry<String, Voiture> entry : voitures.entrySet()) {
                System.out.println("ID: " + entry.getKey() + ", Voiture: " + entry.getValue());
            }
        }
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
    public void lancerCourse() {
        if(courseActuelle == null) {
            throw new IllegalStateException("Aucune course n'est actuellement configurée. Créez d'abord une course.");
        }
        if (courseActuelle.getParticipants().isEmpty()) {
            throw new IllegalStateException("Aucune voiture n'est inscrite à la course. Inscrivez d'abord des voitures.");
        }
        courseActuelle.setEstDemarree(true);
        archiverCourseTerminee();
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