package courseauto.modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Course {
    private final String id;
    private final String nom;
    private final double distanceTotale;
    private final List<Participation> participants;
    private final List<Participation> classement;
    private boolean estDemarree;
    private boolean estTerminee;

    public Course(String id, String nom, double distanceTotale) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'identifiant de la course ne peut pas être vide.");
        }
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la course ne peut pas être vide.");
        }
        if (distanceTotale <= 0) {
            throw new IllegalArgumentException("La distance totale de la course doit être strictement supérieure à 0.");
        }

        this.id = id.trim();
        this.nom = nom.trim();
        this.distanceTotale = distanceTotale;
        this.participants = new ArrayList<>();
        this.classement = new ArrayList<>();
        this.estDemarree = false;
        this.estTerminee = false;
    }

    public void ajouterParticipant(Voiture voiture) {
        if (voiture == null) {
            throw new IllegalArgumentException("Impossible d'inscrire une voiture nulle.");
        }
        if (estDemarree || estTerminee) {
            throw new IllegalStateException("Impossible d'ajouter une voiture : la course a déjà démarré ou est terminée.");
        }
        if (contientVoiture(voiture.getId())) {
            throw new IllegalArgumentException("La voiture avec l'identifiant '" + voiture.getId() + "' est déjà inscrite à cette course.");
        }

        participants.add(new Participation(voiture));
    }

    public boolean contientVoiture(String idVoiture) {
        if (idVoiture == null) return false;
        for (Participation p : participants) {
            if (p.getVoiture().getId().equals(idVoiture)) {
                return true;
            }
        }
        return false;

        public void verifierConfiguration() {
            if (estDemarree || estTerminee) {
                throw new IllegalStateException("Cette course a déjà été lancée ou est terminée.");
        }
            if (participants.size() < 2) {
                throw new IllegalStateException("Impossible de démarrer la course : au moins 2 voitures doivent participer (actuellement : " + participants.size() + ").");
            }
        }
    }
    public void calculerClassementSequentiel() {
        classement.clear();
        classement.addAll(participants);

        classement.sort(Comparator
                .comparingInt(Participation::getNbTours)
                .thenComparingDouble(p -> p.getTempsTheorique(distanceTotale))
                .thenComparing(p -> p.getVoiture().getId())
        );
    }

    public String getDetailsConfiguration() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("=== Course [%s] : %s ===\n", id, nom));
        sb.append(String.format("Distance totale : %.1f km\n", distanceTotale));
        sb.append(String.format("Statut : %s\n", estTerminee ? "Terminée" : (estDemarree ? "En cours" : "En attente de départ")));
        sb.append("Participants (" + participants.size() + ") :\n");
        if (participants.isEmpty()) {
            sb.append("  Aucune voiture inscrite.\n");
        } else {
            for (Participation p : participants) {
                sb.append("  - ").append(p.getVoiture().toString()).append("\n");
            }
        }
        return sb.toString();
    }

     // --- Getters & Setters d'état ---

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public double getDistanceTotale() {
        return distanceTotale;
    }

    public List<Participation> getParticipants() {
        return Collections.unmodifiableList(participants);
    }

    public List<Participation> getClassement() {
        return Collections.unmodifiableList(classement);
    }

     public boolean isEstDemarree() {
        return estDemarree;
    }

    public void setEstDemarree(boolean estDemarree) {
        this.estDemarree = estDemarree;
    }

    public boolean isEstTerminee() {
        return estTerminee;
    }

    public void setEstTerminee(boolean estTerminee) {
        this.estTerminee = estTerminee;
    }


}