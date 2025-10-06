package analyzer.visitors;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.*;


//Visiteur chargé de récupérer les variables locales déclarées dans les méthodes
public class FieldDeclarationVisitor extends ASTVisitor {
    private List<VariableDeclarationFragment> variables = new ArrayList<>();

    @Override
    public boolean visit(VariableDeclarationFragment node) {
        variables.add(node);
        return super.visit(node);
    }

    public List<VariableDeclarationFragment> getVariables() {
        return variables;
    }
}
