-- =============================================
-- FULL RESET / INIT SCRIPT (MySQL 8+ ONLY)
-- Purpose: Clean rebuild of schema without dynamic PREPARE usage.
-- WARNING: This script DROPS existing tables. Use ONLY on dev/local.
-- Date: 2025-11-12
-- =============================================

SET FOREIGN_KEY_CHECKS = 0;

-- Drop legacy / current tables if they exist (order chosen to avoid FK errors)
DROP TABLE IF EXISTS lignes_commande;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS livraisons;
DROP TABLE IF EXISTS paniers;
DROP TABLE IF EXISTS commandes;
DROP TABLE IF EXISTS produits;
DROP TABLE IF EXISTS app_users;
DROP TABLE IF EXISTS companies;
DROP TABLE IF EXISTS clients; -- legacy

SET FOREIGN_KEY_CHECKS = 1;

-- =============================================
-- CORE TABLES
-- =============================================
CREATE TABLE companies (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE,
  address VARCHAR(255),
  city VARCHAR(100),
  phone VARCHAR(20),
  website VARCHAR(255),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_company_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE app_users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL,
  company_id BIGINT,
  enabled BOOLEAN DEFAULT TRUE,
  must_change_password BOOLEAN DEFAULT FALSE,
  created_at DATE,
  INDEX idx_user_email (email),
  INDEX idx_user_role (role),
  CONSTRAINT fk_appuser_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE produits (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  price DECIMAL(10,2) NOT NULL,
  stock INT DEFAULT 0,
  company_id BIGINT,
  categorie_id BIGINT,
  INDEX idx_produit_name (name),
  INDEX idx_produit_company (company_id),
  CONSTRAINT fk_produit_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE SET NULL,
  CONSTRAINT fk_produit_categorie FOREIGN KEY (categorie_id) REFERENCES produits(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE commandes (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  reference VARCHAR(100),
  date_commande DATETIME DEFAULT CURRENT_TIMESTAMP,
  statut VARCHAR(50),
  client_id BIGINT,
  CONSTRAINT fk_commandes_client FOREIGN KEY (client_id) REFERENCES app_users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE paniers (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  app_user_id BIGINT,
  date_creation DATE DEFAULT (CURRENT_DATE),
  CONSTRAINT fk_paniers_appuser FOREIGN KEY (app_user_id) REFERENCES app_users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE livraisons (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  commande_id BIGINT,
  adresse VARCHAR(255),
  ville VARCHAR(100),
  code_postal VARCHAR(10),
  pays VARCHAR(100),
  statut VARCHAR(50),
  date_livraison_estimee DATE,
  CONSTRAINT fk_livraisons_commande FOREIGN KEY (commande_id) REFERENCES commandes(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE lignes_commande (
  id_ligne_commande BIGINT AUTO_INCREMENT PRIMARY KEY,
  commande_id BIGINT,
  produit_id BIGINT,
  quantite INT NOT NULL,
  prixUnitaire DOUBLE NOT NULL,
  seller_line_prix DOUBLE,
  CONSTRAINT fk_lignecommande_commande FOREIGN KEY (commande_id) REFERENCES commandes(id) ON DELETE CASCADE,
  CONSTRAINT fk_lignecommande_produit FOREIGN KEY (produit_id) REFERENCES produits(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE payments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  mode VARCHAR(50),
  statut VARCHAR(50),
  app_user_id BIGINT,
  product VARCHAR(255),
  date DATE,
  amount DOUBLE,
  status VARCHAR(50),
  commande_id BIGINT,
  CONSTRAINT fk_payment_appuser FOREIGN KEY (app_user_id) REFERENCES app_users(id) ON DELETE SET NULL,
  CONSTRAINT fk_payment_commande FOREIGN KEY (commande_id) REFERENCES commandes(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- SEED DATA
-- =============================================
INSERT INTO companies (name, address, city, phone, website) VALUES
 ('Test Company', '123 Rue Test', 'Casablanca', '0612345678', 'www.testcompany.ma');

-- Password is bcrypt for 'password'
SET @pwd := '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy';
INSERT INTO app_users (email, password, full_name, role, enabled, must_change_password, created_at, company_id) VALUES
 ('admin@b2b.com', @pwd, 'Super Administrateur', 'SUPER_ADMIN', TRUE, FALSE, CURDATE(), NULL),
 ('company@b2b.com', @pwd, 'Company Admin', 'COMPANY_ADMIN', TRUE, FALSE, CURDATE(), 1),
 ('buyer@b2b.com', @pwd, 'Test Buyer', 'BUYER', TRUE, FALSE, CURDATE(), 1),
 ('seller@b2b.com', @pwd, 'Test Seller', 'SELLER', TRUE, FALSE, CURDATE(), 1);

INSERT INTO produits (name, description, price, stock, company_id) VALUES
 ('Produit Test 1', 'Description du produit 1', 99.99, 100, 1),
 ('Produit Test 2', 'Description du produit 2', 149.99, 50, 1),
 ('Produit Test 3', 'Description du produit 3', 199.99, 25, 1);

-- Sample order and lines
INSERT INTO commandes (reference, statut, client_id) VALUES ('CMD-TEST-001', 'CREATED', 3);
INSERT INTO lignes_commande (commande_id, produit_id, quantite, prixUnitaire) VALUES
 (1, 1, 2, 99.99),
 (1, 2, 1, 149.99);

INSERT INTO payments (mode, statut, app_user_id, product, date, amount, status, commande_id) VALUES
 ('CARD', 'PENDING', 3, 'CMD-TEST-001', CURDATE(), 349.97, 'INIT', 1);

-- =============================================
-- VERIFICATION
-- =============================================
SELECT 'SCHEMA RESET COMPLETE' AS status_message;
SHOW TABLES;
SELECT id, email, role FROM app_users;
SELECT id, name, price FROM produits;

