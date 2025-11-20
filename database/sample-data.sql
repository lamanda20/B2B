-- ===============================
-- DONNÉES DE TEST POUR B2B APPLICATION
-- ===============================
-- Exécutez ce script APRÈS le premier démarrage de l'application

USE b2b_db;

-- ===============================
-- Utilisateurs de test
-- ===============================
INSERT INTO users (nom, email, telephone, adresse, ville) VALUES
('Mohammed Alami', 'mohammed.alami@example.com', '0612345678', '123 Rue Hassan II', 'Casablanca'),
('Fatima Bennani', 'fatima.bennani@example.com', '0623456789', '456 Avenue Mohammed V', 'Rabat'),
('Youssef Tazi', 'youssef.tazi@example.com', '0634567890', '789 Boulevard Zerktouni', 'Marrakech'),
('Amina Idrissi', 'amina.idrissi@example.com', '0645678901', '321 Rue de Fès', 'Fès'),
('Hassan Mouhib', 'hassan.mouhib@example.com', '0656789012', '654 Avenue Ibn Sina', 'Tanger');

-- ===============================
-- Livraisons de test
-- ===============================
INSERT INTO livraisons (adresse, ville, telephone, transporteur, frais_livraison, date_envoi, date_estimee, user_id) VALUES
('123 Rue Hassan II', 'Casablanca', '0612345678', 'Maroc Poste', 20.00, '2024-11-01', '2024-11-05', 1),
('456 Avenue Mohammed V', 'Rabat', '0623456789', 'Amana Express', 35.00, '2024-11-03', '2024-11-07', 2),
('789 Boulevard Zerktouni', 'Marrakech', '0634567890', 'CTM', 35.00, '2024-11-05', '2024-11-09', 3),
('321 Rue de Fès', 'Fès', '0645678901', 'Maroc Poste', 50.00, '2024-11-06', '2024-11-10', 4),
('654 Avenue Ibn Sina', 'Tanger', '0656789012', 'Amana Express', 35.00, '2024-11-08', '2024-11-12', 5);

-- ===============================
-- Vérification des données insérées
-- ===============================
SELECT 'Utilisateurs insérés:' as Info, COUNT(*) as Total FROM users;
SELECT 'Livraisons insérées:' as Info, COUNT(*) as Total FROM livraisons;

-- Afficher quelques données
SELECT * FROM users;
SELECT * FROM livraisons;
