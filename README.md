# Mini ERP - TP Titre Professionnel CDA

Réalisé par : [TON NOM ICI]

## Description Courte

Ce projet est un mini Système de Planification des Ressources d'Entreprise (ERP) développé en Java avec une interface graphique Swing et une base de données PostgreSQL. Il a été réalisé dans le cadre du Titre Professionnel Concepteur Développeur d'Applications.
L'application permet de gérer les clients, visualiser les produits, créer des commandes, consulter l'historique des commandes et gérer les stocks de manière basique.

## Fonctionnalités Implémentées

*   **Gestion des Clients :**
    *   Affichage de la liste des clients.
    *   Ajout de nouveaux clients via un formulaire (utilisant une procédure stockée SQL `new_customer`).
*   **Gestion des Produits :**
    *   Affichage de la liste des produits.
    *   Filtrage des produits par catégorie.
*   **Gestion des Commandes :**
    *   Création de nouvelles commandes (sélection du client, ajout de produits avec quantité).
    *   Calcul automatique des montants (HT, TVA, TTC).
    *   Sauvegarde des commandes en base de données avec gestion des transactions.
*   **Historique des Commandes :**
    *   Affichage de l'historique des commandes pour un client sélectionné, avec le détail des produits commandés.
*   **Gestion des Stocks :**
    *   Mise à jour (décrémentation) automatique du stock des produits lors de la validation d'une commande.
    *   Affichage des produits dont le stock est bas (en dessous d'un seuil défini).
*   **Interface Utilisateur :**
    *   Menu principal centralisant l'accès aux différentes fonctionnalités.
    *   Navigation facilitée avec des boutons "Retour au Menu".

## Technologies Utilisées

*   **Langage :** Java (JDK 11+ recommandé)
*   **Interface Graphique :** Java Swing
*   **Base de Données :** PostgreSQL (Version 12+ recommandée)
*   **Driver JDBC :** PostgreSQL JDBC Driver
*   **IDE (Développement) :** VS Code (ou autre IDE Java)

## Prérequis

*   JDK (Java Development Kit) version 11 ou supérieure.
*   Serveur PostgreSQL actif.
*   Base de données nommée `store` créée sur le serveur PostgreSQL.
*   Les tables `customers`, `products`, `orders`, `orderlines`, et `inventory` doivent être créées et peuplées (voir section "Structure de la Base de Données" ou scripts fournis si disponibles).
*   La procédure stockée `new_customer(name VARCHAR, email VARCHAR, phone VARCHAR)` doit exister dans la base `store`.

## Configuration JDBC

Les informations de connexion à la base de données sont configurées dans le fichier `src/DatabaseManager.java`.
Par défaut, les paramètres sont :

*   **URL :** `jdbc:postgresql://localhost:5432/store`
*   **Utilisateur :** `postgres`
*   **Mot de passe :** `root`

Si votre configuration PostgreSQL locale est différente, veuillez adapter ces valeurs directement dans le fichier `DatabaseManager.java` avant de compiler et lancer l'application.

## Structure de la Base de Données (Simplifiée)

*   **`customers`**: `customer_id` (PK), `customer_name`, `email`, `phone`
*   **`products`**: `product_id` (PK), `product_name`, `price`, `category`
*   **`orders`**: `order_id` (PK), `customer_id` (FK), `order_date`, `net_amount`, `tax`, `total_amount`
*   **`orderlines`**: `orderline_id` (PK) ou (`order_id`, `product_id`) (PK), `order_id` (FK), `product_id` (FK), `quantity`, `unit_price`
*   **`inventory`**: `product_id` (PK, FK), `quantity_in_stock`, `reorder_threshold`

*Il est recommandé de créer ces tables avec les contraintes de clés primaires et étrangères appropriées. La procédure stockée `new_customer` est également requise pour l'ajout de clients.*

## Consignes de Lancement

1.  **Cloner le dépôt GitHub :**
    ```bash
    git clone [URL_DE_VOTRE_DEPOT_GITHUB]
    cd [NOM_DU_DOSSIER_DU_PROJET]
    ```
2.  **Configuration de la base de données :**
    *   Assurez-vous que votre serveur PostgreSQL est lancé et que la base `store` avec les tables et la procédure `new_customer` sont configurées.
    *   Vérifiez et adaptez si besoin les identifiants de connexion dans `src/DatabaseManager.java`.
3.  **Compilation (si nécessaire) :**
    *   Si vous utilisez un IDE comme IntelliJ IDEA, Eclipse, ou VS Code avec les extensions Java, l'IDE gère généralement la compilation.
    *   Si vous compilez manuellement :
        ```bash
        # Naviguez dans le dossier racine du projet (où se trouve le dossier src)
        javac -d bin src/*.java 
        # (Adaptez si vos fichiers sont dans des packages)
        # Par exemple, si vos fichiers sont dans un package com.example:
        # mkdir bin
        # javac -d bin src/com/example/*.java
        ```
4.  **Exécution de l'application :**
    *   La classe principale contenant la méthode `main` à exécuter est `App.java` (qui lance `MainMenu.java`).
    *   Depuis un IDE : Faites un clic droit sur `App.java` et choisissez "Run" ou "Exécuter".
    *   Depuis la ligne de commande (après compilation dans un dossier `bin` par exemple) :
        ```bash
        # Assurez-vous que le driver JDBC PostgreSQL est dans votre classpath
        # (Ex: postgresql-42.2.5.jar ou une version plus récente)
        # java -cp "bin:chemin/vers/postgresql-jdbc.jar" App
        # Si vos fichiers sont dans des packages (ex: com.example):
        # java -cp "bin:chemin/vers/postgresql-jdbc.jar" com.example.App
        ```
        (La gestion du classpath est plus simple avec un IDE ou des outils de build comme Maven/Gradle).
5.  **Navigation :**
    *   Une fois l'application lancée, le "Menu Principal" s'affiche.
    *   Utilisez les boutons pour naviguer vers les différentes sections (Gestion Clients, Produits, Création Commande, Historique).
    *   Utilisez le bouton "Retour au Menu" présent dans chaque section pour revenir au menu principal.

## Captures d'écran

*(Insérez ici vos captures d'écran. Sur GitHub, vous pouvez les glisser-déposer dans l'éditeur du README.md ou utiliser la syntaxe Markdown `![Description de l'image](chemin/vers/image.png)` si elles sont dans votre dépôt).*

**Exemple :**

![Menu Principal](chemin/vers/screenshot_menu_principal.png)
*Menu Principal de l'application*

![Liste des Clients](chemin/vers/screenshot_liste_clients.png)
*Fenêtre de gestion des clients*

<!-- Ajoutez d'autres captures d'écran ici -->

## Limitations Connues / Améliorations Possibles

*   La validation des saisies utilisateur est basique. Des contrôles plus avancés (format email, format téléphone, etc.) pourraient être ajoutés.
*   La gestion des erreurs SQL pourrait être plus fine pour donner des messages plus spécifiques à l'utilisateur.
*   L'interface de gestion des stocks bas est actuellement un simple affichage ; une gestion plus interactive pourrait être envisagée.
*   Pas de gestion des utilisateurs/rôles.
*   Le design de l'interface est fonctionnel mais pourrait être amélioré esthétiquement.

---
