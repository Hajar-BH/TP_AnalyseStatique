package analyzer.visitors;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.*;

//identifier les appels de méthodes (m() et super.m()) dans le code
public class MethodInvocationVisitor extends ASTVisitor {
    private List<MethodInvocation> appels = new ArrayList<>();
    private List<SuperMethodInvocation> appelsSuper = new ArrayList<>();

    @Override
    public boolean visit(MethodInvocation node) {
        appels.add(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(SuperMethodInvocation node) {
        appelsSuper.add(node);
        return super.visit(node);
    }

    public List<MethodInvocation> getAppels() {
        return appels;
    }

    public List<SuperMethodInvocation> getAppelsSuper() {
        return appelsSuper;
    }
}
