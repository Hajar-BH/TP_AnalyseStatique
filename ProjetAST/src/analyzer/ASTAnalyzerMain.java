package analyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import analyzer.visitors.MethodInvocationVisitor;
import analyzer.visitors.TypeDeclarationVisitor;
import analyzer.visitors.MethodDeclarationVisitor;
import analyzer.visitors.FieldDeclarationVisitor;
import analyzer.processor.ApplicationStatistics;
import analyzer.graph.CallGraph;
import analyzer.ui.MainWindow;

public class ASTAnalyzerMain {

    // Chemin du projet par défaut à analyser
	public static final String defaultProjectPath =
            new File(System.getProperty("user.dir"))
                    .getParentFile()
                    .getAbsolutePath() + File.separator + "Projetmetier" + File.separator + "src";

	public static final String jrePath = System.getProperty("java.home") + File.separator + "lib";
    
	public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

       
        System.out.println("Voulez-vous utiliser le projet par défaut ? (O/N)");
        String reponse = scanner.nextLine().trim().toUpperCase();

        String projectPath;
        if (reponse.equals("O") || reponse.equals("")) {
            projectPath = defaultProjectPath; // chemin par défaut
        } else {
            System.out.println("Veuillez entrer le chemin complet du projet à analyser :");
            projectPath = scanner.nextLine().trim();
        }

        String sourcePath = projectPath;
        System.out.println("Chemin analysé : " + sourcePath);

        // Parcours des fichiers et préparation de l'analyse
        ArrayList<File> javaFiles = collectJavaFiles(new File(sourcePath));
        ApplicationStatistics stats = new ApplicationStatistics();
        CallGraph graphe = new CallGraph();

        for (File file : javaFiles) {
            String code = FileUtils.readFileToString(file, "UTF-8");
            CompilationUnit cu = buildAST(code.toCharArray(), sourcePath);

            if (cu.getPackage() != null) {
                stats.ajouterPackage(cu.getPackage().getName().toString());
            }

            System.out.println("\n=== Analyse du fichier : " + file.getName() + " ===");

            // Analyse des classes
            TypeDeclarationVisitor typeDeclarationVisitor = new TypeDeclarationVisitor();
            cu.accept(typeDeclarationVisitor);
            for (TypeDeclaration c : typeDeclarationVisitor.getTypes()) {
                System.out.println("Classe : " + c.getName());
                if (c.getSuperclassType() != null) {
                    System.out.println("  Hérite de : " + c.getSuperclassType());
                }

                stats.ajouterClasse(c);

                // Analyse des attributs
                FieldDeclaration[] fields = c.getFields();
                for (FieldDeclaration field : fields) {
                    for (Object f : field.fragments()) {
                        VariableDeclarationFragment var = (VariableDeclarationFragment) f;
                        String visibility = "";
                        for (Object mod : field.modifiers()) {
                            if (mod instanceof Modifier) {
                                visibility += mod.toString() + " ";
                            }
                        }
                        System.out.println("  Attribut : " + var.getName()
                                + " | Visibilité : " + visibility.trim()
                                + " | Initialisation : " + var.getInitializer());
                    }
                }
            }

            // Analyse des Méthodes
            MethodDeclarationVisitor methodDeclarationVisitor = new MethodDeclarationVisitor();
            cu.accept(methodDeclarationVisitor);
            for (MethodDeclaration m : methodDeclarationVisitor.getMethodes()) {
                System.out.println("Méthode : " + m.getName() +
                        " | Retour : " + m.getReturnType2());

                stats.ajouterMethode(m, cu);

                FieldDeclarationVisitor fieldDeclarationVisitor = new FieldDeclarationVisitor();
                m.accept(fieldDeclarationVisitor);
                for (VariableDeclarationFragment var : fieldDeclarationVisitor.getVariables()) {
                    System.out.println("  Variable : " + var.getName() +
                            " | Initialisation : " + var.getInitializer());
                }

                MethodInvocationVisitor visiteurAppel = new MethodInvocationVisitor();
                m.accept(visiteurAppel);
                for (MethodInvocation call : visiteurAppel.getAppels()) {
                    System.out.println("  Appel trouvé dans " + m.getName() +
                            " → " + call.getName());
                    graphe.ajouterAppel(m.getName().toString(), call.getName().toString());
                }
                for (SuperMethodInvocation superCall : visiteurAppel.getAppelsSuper()) {
                    System.out.println("  Appel super trouvé dans " + m.getName() +
                            " → " + superCall.getName());
                    graphe.ajouterAppel(m.getName().toString(), superCall.getName().toString());
                }
            }
        }

        // Affichage des Statis (console)
        System.out.println("\n=== Statistiques globales ===");
        System.out.println("1. Nombre de classes : " + stats.getNombreClasses());
        System.out.println("2. Nombre de lignes de code : " + stats.getNombreLignesApplication());
        System.out.println("3. Nombre total de méthodes : " + stats.getNombreMethodes());
        System.out.println("4. Nombre total de packages : " + stats.getNombrePackages());
        System.out.println("5. Moyenne de méthodes par classe : " + stats.moyenneMethodesParClasse());
        System.out.println("6. Moyenne d'attributs par classe : " + stats.moyenneAttributsParClasse());
        System.out.println("7. Moyenne de lignes de code par méthode : " + stats.moyenneLignesParMethode());
        System.out.println("8. Top 10% classes par méthodes : " + stats.top10PourcentClassesParMethodes());
        System.out.println("9. Top 10% classes par attributs : " + stats.top10PourcentClassesParAttributs());
        System.out.println("10. Classes dans les deux catégories : " + stats.classesDansDeuxCategories());
        System.out.println("11. Classes avec plus de X méthodes (X=2) : " + stats.classesAvecPlusDeXMethodes(2));
        System.out.println("12. Top 10% méthodes par lignes : " + stats.top10PourcentMethodesParLignes());
        System.out.println("13. Max paramètres d'une méthode : " + stats.maxParametres());

        System.out.println(graphe);

        // Ouverture de la GUI avec (onglets)
        SwingUtilities.invokeLater(() -> {
            MainWindow fenetre = new MainWindow(stats, graphe);
            fenetre.setVisible(true);
            fenetre.setAlwaysOnTop(true);
            fenetre.toFront();
            fenetre.requestFocus();
            fenetre.setAlwaysOnTop(false);
        });
    }

    // ici c'est la recherche récursive des fichiers Java dans le dossier
    	private static ArrayList<File> collectJavaFiles(File folder) {
        ArrayList<File> javaFiles = new ArrayList<>();
        if (folder == null || !folder.exists()) {
            System.err.println("Dossier introuvable : " + folder);
            return javaFiles;
        }
        File[] files = folder.listFiles();
        if (files == null) {
            return javaFiles;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                javaFiles.addAll(collectJavaFiles(f));
            } else if (f.getName().endsWith(".java")) {
                javaFiles.add(f);
            }
        }
        return javaFiles;
    }

    // buildAST = Construction de l’AST pour un fichier source
    private static CompilationUnit buildAST(char[] source, String sourcePath) {
        ASTParser parser = ASTParser.newParser(AST.JLS17);
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setBindingsRecovery(true);

        @SuppressWarnings("unchecked")
        Map<String, String> options = (Map<String, String>) JavaCore.getOptions();
        parser.setCompilerOptions(options);

        parser.setUnitName("");
        String[] sources = { sourcePath };
        String[] classpath = { jrePath };
        parser.setEnvironment(classpath, sources, new String[] { "UTF-8" }, true);
        parser.setSource(source);

        return (CompilationUnit) parser.createAST(null);
    }
}
