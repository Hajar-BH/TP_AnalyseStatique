package analyzer.visitors;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.*;

// Pour extraire les méthodes
public class MethodDeclarationVisitor extends ASTVisitor {
    private List<MethodDeclaration> methodes = new ArrayList<>();

    @Override
    public boolean visit(MethodDeclaration node) {
        methodes.add(node);
        return super.visit(node);
    }

    public List<MethodDeclaration> getMethodes() {
        return methodes;
    }
}

