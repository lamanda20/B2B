-- ===============================
-- DONNÉES DE TEST POUR B2B APPLICATION
-- ===============================
-- Exécutez ce script APRÈS avoir créé les tables

USE b2b_db;

-- ===============================
-- 1. UTILISATEURS
-- ===============================
INSERT INTO users (nom, email, telephone, adresse, ville) VALUES
('Mohammed Alami', 'mohammed.alami@example.com', '0612345678', '123 Rue Hassan II', 'Casablanca'),
('Fatima Bennani', 'fatima.bennani@example.com', '0623456789', '456 Avenue Mohammed V', 'Rabat'),
('Youssef Tazi', 'youssef.tazi@example.com', '0634567890', '789 Boulevard Zerktouni', 'Marrakech'),
('Amina Idrissi', 'amina.idrissi@example.com', '0645678901', '321 Rue de Fès', 'Fès'),
('Hassan Mouhib', 'hassan.mouhib@example.com', '0656789012', '654 Avenue Ibn Sina', 'Tanger');

-- ===============================
-- 2. CLIENTS
-- ===============================
INSERT INTO clients (nom, email, telephone, adresse_par_defaut, ville_par_defaut) VALUES
('Karim Benali', 'karim.benali@example.com', '0667890123', '111 Rue Anfa', 'Casablanca'),
('Salma Chaoui', 'salma.chaoui@example.com', '0678901234', '222 Avenue Agdal', 'Rabat'),
('Omar Fassi', 'omar.fassi@example.com', '0689012345', '333 Bd Allal Ben Abdellah', 'Fès');

-- ===============================
-- 3. PRODUITS
-- ===============================
INSERT INTO produits (nom, description, prix, stock_disponible, categorie) VALUES
('Ordinateur Portable HP', 'Ordinateur portable 15 pouces, 8GB RAM, 256GB SSD', 7500.00, 50, 'INFORMATIQUE'),
('Souris Sans Fil Logitech', 'Souris ergonomique sans fil 2.4GHz', 250.00, 200, 'ACCESSOIRES'),
('Clavier Mécanique RGB', 'Clavier mécanique gaming avec rétroéclairage RGB', 450.00, 100, 'ACCESSOIRES'),
('Écran 24 pouces Samsung', 'Écran Full HD IPS 24 pouces', 1500.00, 75, 'INFORMATIQUE'),
('Casque Audio Sony WH-1000XM4', 'Casque audio avec réduction de bruit active', 2850.00, 60, 'AUDIO'),
('Webcam Logitech C920', 'Webcam Full HD 1080p avec micro intégré', 650.00, 120, 'ACCESSOIRES'),
('Disque Dur Externe 1TB', 'Disque dur externe USB 3.0 portable', 550.00, 150, 'STOCKAGE'),
('Imprimante HP LaserJet', 'Imprimante laser noir et blanc A4', 1200.00, 40, 'PERIPHERIQUES'),
('Tablette Samsung Galaxy', 'Tablette 10 pouces, 64GB, WiFi', 2200.00, 80, 'INFORMATIQUE'),
('Chargeur USB-C 65W', 'Chargeur rapide USB-C 65W compatible multi-appareils', 180.00, 250, 'ACCESSOIRES');

-- ===============================
-- 4. LIVRAISONS
-- ===============================
INSERT INTO livraisons (adresse, ville, telephone, transporteur, frais_livraison, date_envoi, date_estimee, user_id) VALUES
('123 Rue Hassan II', 'Casablanca', '0612345678', 'Maroc Poste', 20.00, '2024-11-01', '2024-11-05', 1),
('456 Avenue Mohammed V', 'Rabat', '0623456789', 'Amana Express', 35.00, '2024-11-03', '2024-11-07', 2),
('789 Boulevard Zerktouni', 'Marrakech', '0634567890', 'CTM', 35.00, '2024-11-05', '2024-11-09', 3),
('321 Rue de Fès', 'Fès', '0645678901', 'Maroc Poste', 50.00, '2024-11-06', '2024-11-10', 4),
('654 Avenue Ibn Sina', 'Tanger', '0656789012', 'Amana Express', 35.00, '2024-11-08', '2024-11-12', 5);

-- ===============================
-- 5. COMMANDES
-- ===============================
INSERT INTO commandes (ref_commande, date_commande, statut, user_id, livraison_id) VALUES
('CMD-2024-001', '2024-11-01', 'LIVREE', 1, 1),
('CMD-2024-002', '2024-11-03', 'EN_COURS', 2, 2),
('CMD-2024-003', '2024-11-05', 'EN_PREPARATION', 3, 3),
('CMD-2024-004', '2024-11-06', 'EXPEDIEE', 4, 4),
('CMD-2024-005', '2024-11-08', 'EN_ATTENTE', 5, 5);

-- ===============================
-- 6. LIGNES DE COMMANDE
-- ===============================
INSERT INTO lignes_commande (commande_id, produit_id, quantite, prix_unitaire) VALUES
-- Commande 1
(1, 1, 1, 7500.00),
(1, 2, 2, 250.00),
-- Commande 2
(2, 4, 1, 1500.00),
(2, 3, 1, 450.00),
(2, 6, 1, 650.00),
-- Commande 3
(3, 1, 1, 7500.00),
-- Commande 4
(4, 5, 1, 2850.00),
(4, 7, 1, 550.00),
-- Commande 5
(5, 9, 1, 2200.00),
(5, 10, 3, 180.00);

-- ===============================
-- 7. PANIERS
-- ===============================
INSERT INTO paniers (client_id, date_creation) VALUES
(1, '2024-11-09'),
(2, '2024-11-10'),
(3, '2024-11-10');

-- ===============================
-- 8. LIGNES DE PANIER
-- ===============================
INSERT INTO lignes_panier (panier_id, produit_id, quantite, prix_unitaire) VALUES
-- Panier du client 1
(1, 8, 1, 1200.00),
(1, 2, 1, 250.00),
-- Panier du client 2
(2, 1, 1, 7500.00),
(2, 4, 1, 1500.00),
-- Panier du client 3
(3, 5, 1, 2850.00);

-- ===============================
-- 9. PAYMENTS (PAIEMENTS)
-- ===============================
INSERT INTO payments (inv_id, client_name, product, date, amount, status) VALUES
('INV-2024-001', 'Mohammed Alami', 'Ordinateur Portable HP + Souris Sans Fil', '2024-11-01', 8000.00, 'PAYE'),
('INV-2024-002', 'Fatima Bennani', 'Écran Samsung + Clavier + Webcam', '2024-11-03', 2600.00, 'PAYE'),
('INV-2024-003', 'Youssef Tazi', 'Ordinateur Portable HP', '2024-11-05', 7500.00, 'EN_ATTENTE'),
('INV-2024-004', 'Amina Idrissi', 'Casque Sony + Disque Dur', '2024-11-06', 3400.00, 'PAYE'),
('INV-2024-005', 'Hassan Mouhib', 'Tablette Samsung + Chargeurs', '2024-11-08', 2740.00, 'EN_ATTENTE'),
('INV-2024-006', 'Karim Benali', 'Imprimante HP LaserJet', '2024-11-09', 1200.00, 'PAYE'),
('INV-2024-007', 'Salma Chaoui', 'Ordinateur + Écran Samsung', '2024-11-10', 9000.00, 'ECHOUE'),
('INV-2024-008', 'Omar Fassi', 'Casque Audio Sony', '2024-11-10', 2850.00, 'PAYE');

-- ===============================
-- CONFIRMATION DES INSERTIONS
-- ===============================
SELECT 'Données insérées avec succès!' AS Message;

-- Afficher un résumé des données
SELECT 'Users' AS Table_Name, COUNT(*) AS Count FROM users
UNION ALL
SELECT 'Clients', COUNT(*) FROM clients
UNION ALL
SELECT 'Produits', COUNT(*) FROM produits
UNION ALL
SELECT 'Livraisons', COUNT(*) FROM livraisons
UNION ALL
SELECT 'Commandes', COUNT(*) FROM commandes
UNION ALL
SELECT 'Lignes Commande', COUNT(*) FROM lignes_commande
UNION ALL
SELECT 'Paniers', COUNT(*) FROM paniers
UNION ALL
SELECT 'Lignes Panier', COUNT(*) FROM lignes_panier
UNION ALL
SELECT 'Payments', COUNT(*) FROM payments;

-- ===============================
-- EXEMPLES DE REQUÊTES
-- ===============================

-- Voir toutes les commandes avec utilisateur
SELECT
    c.ref_commande,
    c.date_commande,
    c.statut,
    u.nom AS client_nom,
    u.email AS client_email
FROM commandes c
JOIN users u ON c.user_id = u.id
ORDER BY c.date_commande DESC;

-- Voir les détails d'une commande avec ses lignes
SELECT
    c.ref_commande,
    p.nom AS produit,
    lc.quantite,
    lc.prix_unitaire,
    (lc.quantite * lc.prix_unitaire) AS sous_total
FROM commandes c
JOIN lignes_commande lc ON c.id = lc.commande_id
JOIN produits p ON lc.produit_id = p.id
WHERE c.id = 1;

-- Voir les livraisons en cours
SELECT
    l.id_livraison,
    l.adresse,
    l.ville,
    l.transporteur,
    l.date_envoi,
    l.date_estimee,
    u.nom AS client_nom,
    c.ref_commande
FROM livraisons l
JOIN users u ON l.user_id = u.id
LEFT JOIN commandes c ON c.livraison_id = l.id_livraison
WHERE l.date_estimee >= CURDATE()
ORDER BY l.date_estimee;

