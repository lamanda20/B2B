-- ===============================
-- INSERT DATA (adapté aux tables : companies, categories, produits, commandes, livraisons,
-- lignes_commande, lignes_panier, paniers, avis, payments)
-- Usage: mysql -u root -p b2b_db < database/insert-data.sql
-- Exécutez APRÈS avoir créé la base et les tables
-- ===============================

USE b2b_db;

-- Désactiver temporairement les vérifications de clés étrangères pour permettre l'insertion ordonnée
SET FOREIGN_KEY_CHECKS=0;

START TRANSACTION;

-- -------------------------------
-- 1) COMPANIES
-- -------------------------------
-- Table companies columns: id, address, city, created_at, email, enabled, full_name, must_change_password, name, password, phone, role, website
INSERT INTO companies (name, address, city, created_at, email, enabled, full_name, must_change_password, password, phone, role, website) VALUES
('B2B System', 'Siège Social - 1 Rue Principale', 'Casablanca', NOW(), 'contact@b2b.local', TRUE, 'B2B Admin', FALSE, '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qBKjOYhL4bQa.yt7tq1lJhOzPiWy', '0522000000', 1, 'https://www.b2b.local'),
('Tech Supply', '12 Rue Tech', 'Rabat', NOW(), 'contact@tech-supply.local', TRUE, 'Tech Manager', FALSE, '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qBKjOYhL4bQa.yt7tq1lJhOzPiWy', '0522111111', 1, 'https://www.tech-supply.local');

-- -------------------------------
-- 2) CATEGORIES
-- -------------------------------
-- Table `categories` attend : id_cat (auto), name, description
INSERT INTO categories (id_cat, name, description) VALUES
(8, 'INFORMATIQUE', 'Ordinateurs, écrans et accessoires informatiques'),
(9, 'ACCESSOIRES', 'Souris, claviers, webcams, chargeurs'),
(10, 'AUDIO', 'Casques et enceintes'),
(11, 'STOCKAGE', 'Disques durs externes et SSD');

-- -------------------------------
-- 3) PRODUITS
-- Colonnes attendues (par l'entité Produit) : name, description, price, stock, company_id, categorie_id
-- -------------------------------
INSERT INTO produits (name, description, price, stock, company_id, categorie_id) VALUES
('Ordinateur Portable HP', 'Portable 15" - 8GB RAM - 256GB SSD', 7500.00, 50, 1, 1),
('Souris Sans Fil Logitech', 'Souris ergonomique sans fil 2.4GHz', 250.00, 200, 2, 2),
('Clavier Mécanique RGB', 'Clavier mécanique gaming RGB', 450.00, 100, 2, 2),
('Écran 24" Samsung', 'Écran Full HD IPS 24 pouces', 1500.00, 75, 1, 1),
('Casque Audio Sony WH-1000XM4', 'Casque sans fil ANC', 2850.00, 60, 1, 3),
('Webcam Logitech C920', 'Webcam Full HD 1080p', 650.00, 120, 2, 2),
('Disque Dur Externe 1TB', 'HDD portable USB 3.0', 550.00, 150, 2, 4),
('Imprimante HP LaserJet', 'Imprimante laser noir et blanc A4', 1200.00, 40, 1, 2),
('Tablette Samsung Galaxy', 'Tablette 10" 64GB', 2200.00, 80, 1, 1),
('Chargeur USB-C 65W', 'Chargeur rapide USB-C 65W', 180.00, 250, 2, 2);

-- -------------------------------
-- 4) LIVRAISONS
-- Colonnes (conformes à l'entité Livraison) : adresse, ville, code_postal (optionnel), telephone, transporteur, frais_livraison, date_envoi, date_estimee
-- NOTE: certaines bases utilisent snake_case ou camelCase; ici on utilise snake_case compatible avec la plupart des schémas SQL.
-- -------------------------------
INSERT INTO livraisons (adresse, ville, telephone, transporteur, frais_livraison, date_envoi, date_estimee) VALUES
('123 Rue Hassan II', 'Casablanca', '0612345678', 'Maroc Poste', 20.00, '2025-11-01', '2025-11-05'),
('456 Avenue Mohammed V', 'Rabat', '0623456789', 'Amana Express', 35.00, '2025-11-03', '2025-11-07'),
('789 Boulevard Zerktouni', 'Marrakech', '0634567890', 'CTM', 35.00, '2025-11-05', '2025-11-09');

-- -------------------------------
-- 5) COMMANDES
-- Colonnes utilisées : ref_commande, date_commande, statut, company_id, livraison_id
-- (NB: la colonne `company_id` est utilisée ici parce que l'entité Commande référence Company)
-- -------------------------------
INSERT INTO commandes (ref_commande, date_commande, statut, company_id, livraison_id) VALUES
('CMD-2025-001', '2025-11-01', 'LIVREE', 1, 1),
('CMD-2025-002', '2025-11-03', 'EN_COURS', 2, 2),
('CMD-2025-003', '2025-11-05', 'EN_PREPARATION', 1, 3);

-- -------------------------------
-- 6) LIGNES DE COMMANDE
-- Colonnes : commande_id, produit_id, quantite, prix_unitaire, seller_line_prix (optionnel)
-- -------------------------------
INSERT INTO lignes_commande (commande_id, produit_id, quantite, prix_unitaire, seller_line_prix) VALUES
(1, 1, 1, 7500.00, NULL),
(1, 2, 2, 250.00, NULL),
(2, 4, 1, 1500.00, NULL),
(2, 3, 1, 450.00, NULL),
(2, 6, 1, 650.00, NULL),
(3, 1, 1, 7500.00, NULL);

-- -------------------------------
-- 7) PANIERS
-- Colonnes : company_id (référence à Company), date_creation
-- -------------------------------
INSERT INTO paniers (company_id, date_creation) VALUES
(1, '2025-11-09'),
(2, '2025-11-10');

-- -------------------------------
-- 8) LIGNES DE PANIER
-- Colonnes : panier_id, produit_id, quantite
-- -------------------------------
INSERT INTO lignes_panier (panier_id, produit_id, quantite) VALUES
(1, 8, 1),
(1, 2, 1),
(2, 1, 1),
(2, 4, 1);

-- -------------------------------
-- 9) AVIS
-- Colonnes : feedback (TEXT), note (INT), date_creation, company_id (optionnel), produit_id, etat
-- -------------------------------
INSERT INTO avis (feedback, note, date_creation, company_id, produit_id, etat) VALUES
('Très bon produit, livraison rapide.', 5, '2025-11-02', 1, 1, 'APPROUVE'),
('Satisfaisant, mais le câble est arrivé abîmé.', 3, '2025-11-04', 2, 6, 'EN_ATTENTE');

-- -------------------------------
-- 10) PAYMENTS
-- Colonnes (d'après Payment.java) : moyen, produit (description), company_id, date, amount, status, commande_id
-- -------------------------------
INSERT INTO payments (moyen, produit, company_id, date, amount, status, commande_id) VALUES
('Carte', 'Ordinateur Portable HP + Souris', 1, '2025-11-01', 8000.00, 'PAYE', 1),
('Virement', 'Écran + Clavier', 2, '2025-11-03', 1850.00, 'PAYE', 2);

COMMIT;

-- Réactiver les vérifications de clés étrangères
SET FOREIGN_KEY_CHECKS=1;

-- Résumés (facultatif) :
SELECT 'SUMMARY' AS info;
SELECT 'companies' AS table_name, COUNT(*) AS cnt FROM companies
UNION ALL SELECT 'categories', COUNT(*) FROM categories
UNION ALL SELECT 'produits', COUNT(*) FROM produits
UNION ALL SELECT 'livraisons', COUNT(*) FROM livraisons
UNION ALL SELECT 'commandes', COUNT(*) FROM commandes
UNION ALL SELECT 'lignes_commande', COUNT(*) FROM lignes_commande
UNION ALL SELECT 'paniers', COUNT(*) FROM paniers
UNION ALL SELECT 'lignes_panier', COUNT(*) FROM lignes_panier
UNION ALL SELECT 'avis', COUNT(*) FROM avis
UNION ALL SELECT 'payments', COUNT(*) FROM payments;

-- Fin du script
