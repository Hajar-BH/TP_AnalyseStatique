package analyzer.graph;


import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.util.mxRectangle;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CallGraphWindow extends JFrame {

    public CallGraphWindow(CallGraph graphe) {
        setTitle("Graphe d'appel - TP Analyse Statique");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

       // Table pour garder la correspondance entre les méthodes et leurs nœuds
        Map<String, Object> noeuds = new HashMap<>();

        graph.getModel().beginUpdate();
        try {
            // Ajout d’un nœud pour chaque méthode détectée
            for (String methode : graphe.getTousAppels().keySet()) {
                noeuds.put(methode, graph.insertVertex(
                        parent, null, methode, 0, 0, 60, 40,
                        "shape=ellipse;fillColor=#AED6F1;fontSize=12"
                ));
            }

            // liens entre les méthodes (les arêtes)
            for (Map.Entry<String, Set<String>> entry : graphe.getTousAppels().entrySet()) {
                String source = entry.getKey();
                for (String cible : entry.getValue()) {
                    // Vérifier si la méthode appelée n’a pas encore de nœud, (sinon on l'ajoute)
                    if (!noeuds.containsKey(cible)) {
                        noeuds.put(cible, graph.insertVertex(
                                parent, null, cible, 0, 0, 60, 40,
                                "shape=ellipse;fillColor=#AED6F1;fontSize=12"
                        ));
                    }
                    graph.insertEdge(parent, null, "", noeuds.get(source), noeuds.get(cible));
                }
            }
        } finally {
            graph.getModel().endUpdate();
        }

        // un layout automatique (cercle)
        mxCircleLayout layout = new mxCircleLayout(graph);
        layout.setRadius(250); 
        layout.execute(graph.getDefaultParent());

        mxGraphComponent graphComponent = new mxGraphComponent(graph);

        // pour le Zoom à la molette et pan activé
        graphComponent.setPanning(true);
        graphComponent.getGraphControl().addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0)
                graphComponent.zoomIn();
            else
                graphComponent.zoomOut();
            e.consume();
        });

        SwingUtilities.invokeLater(() -> {
            graphComponent.getGraph().getView().validate();
            mxRectangle bounds = graphComponent.getGraph().getView().getGraphBounds();
            Dimension size = graphComponent.getViewport().getExtentSize();
            if (bounds != null && bounds.getWidth() > 0 && bounds.getHeight() > 0) {
                double sx = size.getWidth() / bounds.getWidth();
                double sy = size.getHeight() / bounds.getHeight();
                double scale = Math.min(sx, sy) * 0.9;
                graphComponent.zoomTo(scale, true);
            }
        });

        // La barre d’outils (Zoom +, Zoom -, Ajuster)
        JToolBar toolBar = new JToolBar();
        JButton zoomInBtn = new JButton("🔍 +");
        JButton zoomOutBtn = new JButton("🔍 -");
        JButton fitBtn = new JButton("🖥 Ajuster");

        zoomInBtn.addActionListener(e -> graphComponent.zoomIn());
        zoomOutBtn.addActionListener(e -> graphComponent.zoomOut());
        fitBtn.addActionListener(e -> {
            graphComponent.getGraph().getView().validate();
            mxRectangle bounds = graphComponent.getGraph().getView().getGraphBounds();
            Dimension size = graphComponent.getViewport().getExtentSize();
            if (bounds != null && bounds.getWidth() > 0 && bounds.getHeight() > 0) {
                double sx = size.getWidth() / bounds.getWidth();
                double sy = size.getHeight() / bounds.getHeight();
                double scale = Math.min(sx, sy) * 0.9;
                graphComponent.zoomTo(scale, true);
            }
        });

        toolBar.add(zoomInBtn);
        toolBar.add(zoomOutBtn);
        toolBar.add(fitBtn);

        // la mise en page
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(graphComponent, BorderLayout.CENTER);
    }
}

