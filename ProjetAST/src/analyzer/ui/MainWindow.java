package analyzer.ui;

import javax.swing.*;
import analyzer.processor.ApplicationStatistics;
import analyzer.graph.CallGraph;
import analyzer.graph.CallGraphWindow;

public class MainWindow extends JFrame {

    public MainWindow(ApplicationStatistics stats, CallGraph graphe) {
        setTitle("Analyse Statique - Résultats");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Création du conteneur à onglets principal
        JTabbedPane tabs = new JTabbedPane();

        // Onglet 1 : Statistiques
        StatisticsWindow statsPanel = new StatisticsWindow(stats);
        tabs.addTab("Statistiques", statsPanel.getContentPane());

        // Onglet 2 : Graphe d’appel
        CallGraphWindow graphPanel = new CallGraphWindow(graphe);
        tabs.addTab("Graphe d'appel", graphPanel.getContentPane());

        add(tabs);
    }
}
