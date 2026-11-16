package courseauto.modele;

import java.util.Objects; // pour redéfinir la méthode equals tranquillement

public class Voiture {
    private final String id;
    private String nom;
    private String marque;
    private double vitesse;

    public Voiture(String id, String nom, String marque, double vitesse){
        if (id == null || id.trim().isEmpty()) { // trim() est une fonction de la classe String qui permet de supprimer les espaces. 
                                                // Je l'ai utilisée pour s'assurer qu'on ait pas de chaînes vides composées d'espaces (ce qui fausserait les résultats).
            throw new IllegalArgumentException("L'identifiant de la voiture ne peut pas être vide.");
        }
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la voiture ne peut pas être vide.");
        }
        if (marque == null || marque.trim().isEmpty()) {
            throw new IllegalArgumentException("La marque de la voiture ne peut pas être vide.");
        }
        if (vitesse <= 0) {
            throw new IllegalArgumentException("La vitesse doit être strictement supérieure à 0.");
        }

        this.id = id.trim();
        this.nom = nom.trim();
        this.marque = marque.trim();
        this.vitesse = vitesse;
    }
    // -- Getters et Setters, toujours utiles.
    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la voiture ne peut pas être vide.");
        }
        this.nom = nom.trim();
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        if (marque == null || marque.trim().isEmpty()) {
            throw new IllegalArgumentException("La marque de la voiture ne peut pas être vide.");
        }
        this.marque = marque.trim();
    }

    public double getVitesse() {
        return vitesse;
    }

    public void setVitesse(double vitesse) {
        if (vitesse <= 0) {
            throw new IllegalArgumentException("La vitesse doit être strictement supérieure à 0.");
        }
        this.vitesse = vitesse;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voiture voiture = (Voiture) o;
        return Objects.equals(id, voiture.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - %.1f km/tour", id, nom, marque, vitesse);
    }

}