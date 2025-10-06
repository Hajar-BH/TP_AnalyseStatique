# README - Manuel d'installation et d'utilisation de l'outil d'analyse statique (AST / JDT) 

## Plan 
- Présentation du projet
- Prérequis
- Installation
- Organisation du projet
- Utilisation
- Personnalisation
- Liens

## 1. Présentation du projet

Ce projet a été développé dans le cadre du module HAI913I - Évolution et  Restructuration des Logiciels.
Il a pour objectif de mettre en œuvre une analyse statique du code source Java à l’aide de l’API Eclipse JDT (Java Development Tools).

L'outil permet de : 
- Construire et parcourir un AST (Abstract Syntax Tree) pour extraire les classes, méthodes, attributs et appels.
- Calculer automatiquement 13 indicateurs statistiques sur le code analysé.
- Afficher les résultats dans une interface graphique interactive avec possibilité d’exportation en CSV.
- Générer un graphe d’appel illustrant les dépendances entre les méthodes via JGraphX.

## 2. Prérequis

-   **Java JDK 17 ou supérieur** (testé avec Eclipse Adoptium JDK 17).
-   **Eclipse IDE** (ou IntelliJ IDEA compatible)
-   **Maven** pour la gestion des dépendances, ou bien ajouter
    manuellement les librairies suivantes :
    -   `commons-io.jar` (Apache Commons IO)
    -   `jgraphx-4.2.2.jar` (JGraphX)
-   **Dépendances** Les dépendances nécessaires sont automatiquement gérées via le fichier pom.xml.
Elles peuvent également être ajoutées manuellement si le projet n’est pas importé comme projet Maven.

## 3. Installation

1.  Importer le projet **ProjetAST** dans **Eclipse**.
2.  Créer un dossier `lib/` et y placer les fichiers :
    -   `commons-io.jar`
    -   `jgraphx-4.2.2.jar`
3.  Ajouter ces librairies au **Build Path** (clic droit → Build Path →
    Add to Build Path).
4.  Vérifier et adapter le chemin du projet dans la classe
    `ASTAnalyzerMain` :
    private static final String defaultProjectPath = "chemin/vers/ProjetMetier/src";
5.  Lancer la classe **ASTAnalyzerMain.java**.

## 4. Organisation du projet

### 4.1 - ProjetMetier

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

### 4.2 - ProjetAST

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

## 5. Utilisation

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

## 6. Personnalisation

-   Pour **changer la valeur de X** (question 11 : classes avec plus de
    X méthodes), modifier :
    classesAvecPlusDeXMethodes(int X) dans `ApplicationStatistics.java`.

-   Pour **analyser un autre projet**, modifier la constante
    `defaultProjectPath` dans `ASTAnalyzerMain`.

-   Pour **ajuster le style du graphe**, modifier le layout dans
    `CallGraphWindow.java` :
    mxCircleLayout layout = new mxCircleLayout(graph);
    layout.setRadius(250);
    
## 7. Liens 

**Code source complet sur GitHub** :  
  https://github.com/Hajar-BH/TP_AnalyseStatique

**Vidéo de démonstration (Google Drive)** :  
  https://drive.google.com/file/d/14LTwO_ZicyZ9BfizJCbKeElFvlLw6UIM/view?usp=sharing