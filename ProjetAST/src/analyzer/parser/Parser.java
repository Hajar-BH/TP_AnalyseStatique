package analyzer.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class Parser {

    public static final String projectPath = "C:\\Users\\Rezk\\workspace\\ProjetMetier";
    public static final String projectSourcePath = projectPath + "\\src";
    public static final String jrePath = "C:\\Program Files\\Java\\jre1.8.0_51\\lib\\rt.jar";
 
    // Parcourt le projet pour récupérer tous les fichiers Java
    public static ArrayList<File> listJavaFiles(File folder) {
        ArrayList<File> javaFiles = new ArrayList<>();
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                javaFiles.addAll(listJavaFiles(file));
            } else if (file.getName().endsWith(".java")) {
                javaFiles.add(file);
            }
        }
        return javaFiles;
    }

    public static String readFile(File file) throws IOException {
        return FileUtils.readFileToString(file, "UTF-8");
    }

    // ici on construit l’AST à partir du code source
    public static CompilationUnit parse(char[] source) {
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setBindingsRecovery(true);

        @SuppressWarnings("unchecked")
        Map<String, String> options = (Map<String, String>) JavaCore.getOptions();
        parser.setCompilerOptions(options);

        parser.setUnitName("");
        String[] sources = { projectSourcePath };
        String[] classpath = { jrePath };
        parser.setEnvironment(classpath, sources, new String[] { "UTF-8" }, true);
        parser.setSource(source);

        return (CompilationUnit) parser.createAST(null);
    }
}
