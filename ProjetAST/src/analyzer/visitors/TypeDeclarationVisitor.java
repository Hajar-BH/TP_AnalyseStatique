package analyzer.visitors;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.*;

//identifier les classes et récupérer leurs super-classes
public class TypeDeclarationVisitor extends ASTVisitor {
    private List<TypeDeclaration> types = new ArrayList<>();

    @Override
    public boolean visit(TypeDeclaration node) {
        types.add(node);
        return super.visit(node);
    }

    public List<TypeDeclaration> getTypes() {
        return types;
    }
}
