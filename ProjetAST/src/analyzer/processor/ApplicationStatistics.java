package analyzer.processor;

import java.util.*;
import org.eclipse.jdt.core.dom.*;

public class ApplicationStatistics {

    private int nombreClasses = 0;
    private int nombreMethodes = 0;
    private int nombreLignesApplication = 0;

    private Set<String> packagesUniques = new HashSet<>();
    private ArrayList<Integer> methodesParClasse = new ArrayList<>();
    private ArrayList<Integer> attributsParClasse = new ArrayList<>();
    private ArrayList<Integer> lignesParMethode = new ArrayList<>();
    private ArrayList<Integer> parametresParMethode = new ArrayList<>();

    private List<InfosClasse> classesInfos = new ArrayList<>();
    private List<InfosMethode> methodesInfos = new ArrayList<>();

    public static class InfosClasse {
        String nom;
        int nbMethodes;
        int nbAttributs;

        InfosClasse(String nom, int nbMethodes, int nbAttributs) {
            this.nom = nom;
            this.nbMethodes = nbMethodes;
            this.nbAttributs = nbAttributs;
        }

        @Override
        public String toString() {
            return nom + " (M=" + nbMethodes + ", A=" + nbAttributs + ")";
        }
    }

    public static class InfosMethode {
        String nom;
        String classe;
        int nbLignes;

        InfosMethode(String classe, String nom, int nbLignes) {
            this.classe = classe;
            this.nom = nom;
            this.nbLignes = nbLignes;
        }

        @Override
        public String toString() {
            return classe + "." + nom + " (" + nbLignes + " lignes)";
        }
    }


    public void ajouterClasse(TypeDeclaration c) {
        nombreClasses++;
        methodesParClasse.add(c.getMethods().length);
        attributsParClasse.add(c.getFields().length);

        classesInfos.add(new InfosClasse(
                c.getName().toString(),
                c.getMethods().length,
                c.getFields().length
        ));
    }

    // Analyse le corps d'une méthode pour compter ses lignes et ses paramètres
    public void ajouterMethode(MethodDeclaration m, CompilationUnit cu) {
        nombreMethodes++;

        int lignes = 0;
        if (m.getBody() != null) {
            String[] contenu = m.getBody().toString().split("\n");
            for (String ligne : contenu) {
                String trim = ligne.trim();
                if (!trim.isEmpty()
                        && !trim.equals("{")
                        && !trim.equals("}")
                        && !trim.startsWith("//")
                        && !trim.startsWith("/*")
                        && !trim.startsWith("*")) {
                    lignes++;
                }
            }
        }
        lignesParMethode.add(lignes);
        parametresParMethode.add(m.parameters().size());
        nombreLignesApplication += lignes;

        if (m.getParent() instanceof TypeDeclaration typeDecl) {
            methodesInfos.add(new InfosMethode(
                    typeDecl.getName().toString(),
                    m.getName().toString(),
                    lignes
            ));
        }
    }

    // Enregistre le nom d’un package s’il est trouvé
    public void ajouterPackage(String nomPackage) {
        if (nomPackage != null) {
            packagesUniques.add(nomPackage);
        }
    }

    public int getNombreClasses() { return nombreClasses; }
    public int getNombreMethodes() { return nombreMethodes; }
    public int getNombrePackages() { return packagesUniques.size(); }
    public int getNombreLignesApplication() { return nombreLignesApplication; }

    // Calculs statistiques 
    public double moyenneMethodesParClasse() {
        return methodesParClasse.stream().mapToInt(Integer::intValue).average().orElse(0);
    }
    public double moyenneAttributsParClasse() {
        return attributsParClasse.stream().mapToInt(Integer::intValue).average().orElse(0);
    }
    public double moyenneLignesParMethode() {
        return lignesParMethode.stream().mapToInt(Integer::intValue).average().orElse(0);
    }
    public int maxParametres() {
        return parametresParMethode.stream().mapToInt(Integer::intValue).max().orElse(0);
    }

    // Classements des classes selon le nombre de méthodes ou d'attributs
    public List<InfosClasse> top10PourcentClassesParMethodes() {
        int n = Math.max(1, classesInfos.size() / 10);
        return classesInfos.stream()
                .sorted(Comparator.comparingInt(c -> -c.nbMethodes))
                .limit(n)
                .toList();
    }
    public List<InfosClasse> top10PourcentClassesParAttributs() {
        int n = Math.max(1, classesInfos.size() / 10);
        return classesInfos.stream()
                .sorted(Comparator.comparingInt(c -> -c.nbAttributs))
                .limit(n)
                .toList();
    }
    
    // Recherche des classes présentes dans les deux classements précédents
    public List<String> classesDansDeuxCategories() {
        var topMeth = top10PourcentClassesParMethodes().stream().map(c -> c.nom).toList();
        var topAttr = top10PourcentClassesParAttributs().stream().map(c -> c.nom).toList();
        return topMeth.stream().filter(topAttr::contains).toList();
    }
    
    // Classes possédant plus de X méthodes (X paramétrable)
    public List<String> classesAvecPlusDeXMethodes(int x) {
        return classesInfos.stream()
                .filter(c -> c.nbMethodes > x)
                .map(c -> c.nom + " (" + c.nbMethodes + " méthodes)")
                .toList();
    }
    public List<InfosMethode> top10PourcentMethodesParLignes() {
        int n = Math.max(1, methodesInfos.size() / 10);
        return methodesInfos.stream()
                .sorted(Comparator.comparingInt(m -> -m.nbLignes))
                .limit(n)
                .toList();
    }
}
