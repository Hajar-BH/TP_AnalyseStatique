package analyzer.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

import analyzer.processor.ApplicationStatistics;

public class StatisticsWindow extends JFrame {

    private Object[][] data;
    private String[] colonnes = {"Question", "Résultat"};

    public StatisticsWindow(ApplicationStatistics stats) {
        setTitle("Statistiques de l'application");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Préparation des données à afficher dans le tableau
        data = new Object[][]{
                {"1. Nombre de classes", stats.getNombreClasses()},
                {"2. Nombre de lignes de code", stats.getNombreLignesApplication()},
                {"3. Nombre total de méthodes", stats.getNombreMethodes()},
                {"4. Nombre total de packages", stats.getNombrePackages()},
                {"5. Moyenne de méthodes par classe", stats.moyenneMethodesParClasse()},
                {"6. Moyenne d'attributs par classe", stats.moyenneAttributsParClasse()},
                {"7. Moyenne de lignes de code par méthode", stats.moyenneLignesParMethode()},
                {"8. Top 10% classes par méthodes",
                        stats.top10PourcentClassesParMethodes().stream()
                                .map(Object::toString)
                                .reduce((a,b) -> a + ", " + b).orElse("Aucune")},
                {"9. Top 10% classes par attributs",
                        stats.top10PourcentClassesParAttributs().stream()
                                .map(Object::toString)
                                .reduce((a,b) -> a + ", " + b).orElse("Aucune")},
                {"10. Classes dans les deux catégories",
                        String.join(", ", stats.classesDansDeuxCategories())},
                {"11. Classes avec plus de X méthodes (X=2)",
                        String.join(", ", stats.classesAvecPlusDeXMethodes(2))},
                {"12. Top 10% méthodes par lignes",
                        stats.top10PourcentMethodesParLignes().stream()
                                .map(Object::toString)
                                .reduce((a,b) -> a + ", " + b).orElse("Aucune")},
                {"13. Max paramètres d'une méthode", stats.maxParametres()}
        };

        // le tableau principal
        DefaultTableModel model = new DefaultTableModel(data, colonnes);
        JTable table = new JTable(model);
        table.setFont(new Font("Serif", Font.PLAIN, 14));
        table.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(table);

        JButton exportButton = new JButton("Exporter en CSV");
        exportButton.addActionListener(e -> exporterCSV());

        JPanel panelBouton = new JPanel();
        panelBouton.add(exportButton);

        // Ajout des composants dans la fenêtre principale
        add(scrollPane, BorderLayout.CENTER);
        add(panelBouton, BorderLayout.SOUTH);

        setLocationRelativeTo(null); 
    }

    // Méthode d’export des résultats dans un fichier CSV
    private void exporterCSV() {
        try (FileWriter writer = new FileWriter("stats.csv")) {
            writer.append(colonnes[0]).append(";").append(colonnes[1]).append("\n");

            for (Object[] row : data) {
                writer.append(row[0].toString()).append(";");
                writer.append(row[1].toString()).append("\n");
            }

            JOptionPane.showMessageDialog(this, "Export réussi : stats.csv");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'export : " + ex.getMessage());
        }
    }
}
