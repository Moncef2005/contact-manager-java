CREATE DATABASE IF NOT EXISTS gestion_contacts;
USE gestion_contacts;

CREATE TABLE contacts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    telephone VARCHAR(15),
    email VARCHAR(100)
);

INSERT INTO contacts (nom, prenom, telephone, email) VALUES
('Benidar', 'Jamal', '0612345678', 'jamal.benidar@gmail.com'),
('Bouzbouz', 'Anas', '0698765432', 'Anas.Bouzbouz@gmail.com');



