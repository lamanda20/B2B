-- ===============================
-- SCRIPT DE MISE À JOUR DU SCHÉMA
-- ===============================
-- Exécutez ce script pour mettre à jour les tables existantes
-- sans les supprimer (préserve les données)

USE b2b_db;

-- Ajouter la colonne seller_line_prix si elle n'existe pas
ALTER TABLE lignes_commande
ADD COLUMN IF NOT EXISTS seller_line_prix DOUBLE AFTER prix_unitaire;

SELECT 'Schéma mis à jour avec succès!' AS Message;

