package courseauto.concurrent;

import courseauto.modele.Participation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassementPartage {
    private final List<Participation> classement = new ArrayList<>();

    public synchronized void enregistrerArrivee(Participation participation) {
        if (participation == null) {
            return;
        }
        // Empêche l'enregistrement multiple d'une même voiture
        for (Participation p : classement) {
            if (p.getVoiture().getId().equalsIgnoreCase(participation.getVoiture().getId())) {
                return;
            }
        }
        classement.add(participation);
        System.out.printf("==> [ARRIVÉE #%d] %s (%s) franchit la ligne d'arrivée !%n",
                classement.size(),
                participation.getVoiture().getNom(),
                participation.getVoiture().getId());
    }

    public synchronized List<Participation> getClassement() {
        return Collections.unmodifiableList(new ArrayList<>(classement));
    }

    public synchronized int getNombreArrivees() {
        return classement.size();
    }
}