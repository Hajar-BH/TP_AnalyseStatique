# README - Manuel d'installation et d'utilisation de l'outil d'analyse statique (AST / JDT) 

## Plan 
- Prérequis
- Installation
- Organisation du projet
- Utilisation
- Personnalisation
- Liens
- 
## 1. Prérequis

-   **Java JDK 17 ou supérieur** (testé avec Eclipse Adoptium JDK 17).
-   **Eclipse IDE** (ou IntelliJ IDEA compatible)
-   **Maven** pour la gestion des dépendances, ou bien ajouter
    manuellement les librairies suivantes :
    -   `commons-io.jar` (Apache Commons IO)
    -   `jgraphx-4.2.2.jar` (JGraphX)
    -   `org.eclipse.jdt.core-3.32.0.jar` (API JDT pour l’analyse du code Java)
    Si Maven n’est pas utilisé, ces librairies doivent être placées dans le dossier lib/ du projet et ajoutées manuellement au Build Path.
-   **Dépendances** Les dépendances nécessaires sont automatiquement gérées via le fichier pom.xml.

## 2. Installation

1.  Décompresser le fichier TP1_AnalyseStatique.zip
2.  Dans Eclipse, allez dans :
        File → Import → Existing Maven Project.
3.  Cliquez sur Browse, puis sélectionnez le dossier TP1_AnalyseStatique qui contient :
        TP1AST/
        ProjetMetier/
4.  Eclipse importera automatiquement les dépendances via Maven.
5.  Ouvrez ensuite le dossier TP1AST 
6.  Entrez dans src, puis dans le package analyser: 
        TP1AST/src/analyzer/ASTAnalyzerMain.java
7.  Executez le Main ASTAnalyzerMain via Run As → Java Application.

## 3. Organisation du projet

### 3.1 - ProjetMetier

Projet d'exemple servant à tester l’analyse (modifiable par
l'utilisateur).
Il contient plusieurs classes simples pour tester l'outil :
- **Car.java** → Attributs (`model`, `speed`), méthodes (`accelerate`,
`brake`, `getModel`).
- **ElectricCar.java** → Hérite de `Car`, ajoute `batteryLevel` et
`chargeBattery`.\
- **Garage.java** → Gère une liste de voitures (`addCar`, `showCars`).
- **Driver.java**, **Truck.java** → Illustrent l'héritage et les
relations d'appel.

### 3.2 - ProjetAST

Projet principal de l’analyseur :
- `analyzer/ASTAnalyzerMain.java` → Point d'entrée du programme.
- `analyzer/parser/Parser.java` → Construction de l'AST à partir du code
source.
- `analyzer/processor/ApplicationStatistics.java` → Calcul des
statistiques et agrégation des données.
- `analyzer/ui/MainWindow.java`, `StatisticsWindow.java` → Interface
graphique.
- `analyzer/graph/CallGraph.java`, `CallGraphWindow.java` → Construction
et affichage du graphe d'appel.
- `analyzer/visitors/` → Visiteurs de l'AST (Type, Méthodes, Attributs,
Appels).

## 4. Utilisation

Au lancement, le programme affiche :

    Voulez-vous utiliser le projet par défaut ? (O/N)
-   **O** → analyse du projet par défaut `ProjetMetier`.
-   **N** → saisie manuelle d'un autre chemin de projet Java à analyser.

    ## Trois résultats sont produits :

1.  **Sortie console** : liste des classes, méthodes, attributs, et appels.
2.  **Statistiques globales (console + interface graphique)** : 13 indicateurs.
3.  **Interface graphique (Swing)** :
    -   Onglet **Statistiques** → tableau clair et export CSV.
    -   Onglet **Graphe d'appel** → représentation visuelle circulaire
        des appels entre méthodes.

## 5. Personnalisation

-   Pour **changer la valeur de X** (question 11 : classes avec plus de
    X méthodes), modifier :
    classesAvecPlusDeXMethodes(int X) dans `ApplicationStatistics.java`.

-   Pour **analyser un autre projet**, modifier la constante
    `defaultProjectPath` dans `ASTAnalyzerMain`.

-   Pour **ajuster le style du graphe**, modifier le layout dans
    `CallGraphWindow.java` :
    mxCircleLayout layout = new mxCircleLayout(graph);
    layout.setRadius(250);
    
## 6. Liens 

**Code source complet sur GitHub** :  
  https://github.com/Hajar-BH/TP_AnalyseStatique

**Vidéo de démonstration (Google Drive)** :  
  https://drive.google.com/file/d/14LTwO_ZicyZ9BfizJCbKeElFvlLw6UIM/view?usp=sharing
