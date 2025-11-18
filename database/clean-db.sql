-- ===============================
-- SCRIPT DE NETTOYAGE COMPLET
-- ===============================
-- Exécutez ce script d'abord pour nettoyer complètement la base

USE b2b_db;

-- Désactiver les vérifications de clés étrangères
SET FOREIGN_KEY_CHECKS = 0;

-- Supprimer toutes les tables
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS lignes_panier;
DROP TABLE IF EXISTS lignes_commande;
DROP TABLE IF EXISTS paniers;
DROP TABLE IF EXISTS commandes;
DROP TABLE IF EXISTS livraisons;
DROP TABLE IF EXISTS clients;
DROP TABLE IF EXISTS produits;
DROP TABLE IF EXISTS app_users;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS companies;

-- Réactiver les vérifications
SET FOREIGN_KEY_CHECKS = 1;

SELECT 'Base de données nettoyée! Maintenant exécutez create-tables.sql' AS Message;
