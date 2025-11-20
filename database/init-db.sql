-- ===============================
-- SCRIPT D'INITIALISATION SIMPLE
-- ===============================

-- Créer la base de données
CREATE DATABASE IF NOT EXISTS b2b_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Utiliser la base de données
USE b2b_db;

-- Message de confirmation
SELECT 'Base de données b2b_db créée avec succès!' AS Message;
SELECT 'Pour créer les tables, exécutez: mysql -u root -p < database/create-tables.sql' AS Info;

