# Contact Manager - Java Swing & MySQL

Application desktop de gestion de contacts développée en Java Swing avec connexion à une base de données MySQL via JDBC.

---

## Fonctionnalités

- Ajouter un contact
- Modifier un contact
- Supprimer un contact
- Actualiser et afficher les contacts
- Affichage dynamique dans un tableau (JTable)

---

## Technologies utilisées

- Java (JDK 8+)
- Java Swing
- MySQL
- JDBC (mysql-connector-j)
- Git & GitHub

---

## Structure du projet
```
contact-manager-java/
│
├── src/
│   └── ContactManager.java
│
├── lib/
│   └── mysql-connector-j-9.5.0.jar
│
├── database/
│   └── gestion_contacts.sql
│
├── .gitignore
└── README.md
```

---

## Configuration de la base de données

### 1. Créer la base de données

```sql
CREATE DATABASE IF NOT EXISTS gestion_contacts;
USE gestion_contacts;

CREATE TABLE contacts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    telephone VARCHAR(15),
    email VARCHAR(100)
);
```
## Configurer la connexion dans le code
private static final String URL = "jdbc:mysql://localhost:3306/gestion_contacts";

private static final String USER = "root";

private static final String PASSWORD = "your_password";

## Lancer l'application
1. Compiler le projet
2. Ajouter mysql-connector-j-x.x.xx.jar au classpath
3. Exécuter ContactManager.java
Exemple en ligne de commande :
javac -cp ".;lib/mysql-connector-j-9.5.0.jar" src/ContactManager.java

java -cp ".;lib/mysql-connector-j-9.5.0.jar;src" ContactManager
(Sur Linux ou macOS, remplacer ; par :)
## Objectif du projet
Ce projet a été réalisé pour :
- Pratiquer Java Swing
- Comprendre la connexion JDBC
- Manipuler une base de données MySQL
- Implémenter les opérations CRUD (Create, Read, Update, Delete)
## Améliorations possibles
- Validation avancée des emails
- Recherche dynamique de contacts
- Architecture MVC
- Migration vers Maven ou Gradle
- Ajout d’une authentification utilisateur
## Auteur 
Moncef Aaouine

Étudiant en Informatique
