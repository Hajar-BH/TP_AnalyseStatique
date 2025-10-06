package analyzer.graph;

import java.util.*;

public class CallGraph {
    private Map<String, Set<String>> graphe = new HashMap<>();

    // ici l'Ajout d'une relation d'appel entre deux méthodes
    public void ajouterAppel(String appelant, String appelee) {
        graphe.computeIfAbsent(appelant, k -> new HashSet<>()).add(appelee);
    }

    public Set<String> getAppels(String methode) {
        return graphe.getOrDefault(methode, Collections.emptySet());
    }

    // Renvoie l'ensemble des méthodes présentes dans le graphe
    public Set<String> getMethodes() {
        return graphe.keySet();
    }

    // renvoie la structure complète pour recuperer tout le graphe
    public Map<String, Set<String>> getTousAppels() {
        return graphe;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n===== Graphe d'appel =====\n");
        for (var entry : graphe.entrySet()) {
            sb.append(entry.getKey()).append(" → ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
