-- ===============================
-- SCRIPT COMPLET DE CRÉATION DES TABLES B2B
-- ===============================
-- Base de données: b2b_db
-- Date: 2024-11-10

-- Supprimer et recréer la base de données complètement
DROP DATABASE IF EXISTS b2b_db;

CREATE DATABASE b2b_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Utiliser la base de données
USE b2b_db;

-- ===============================
-- TABLE: users (doit être créée en premier)
-- ===============================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    telephone VARCHAR(20),
    adresse VARCHAR(255),
    ville VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===============================
-- TABLE: companies (pour la gestion des entreprises)
-- ===============================
CREATE TABLE companies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    address VARCHAR(255),
    city VARCHAR(100),
    phone VARCHAR(20),
    website VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===============================
-- TABLE: app_users (pour l'authentification)
-- ===============================
CREATE TABLE app_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    must_change_password BOOLEAN DEFAULT FALSE,
    company_id BIGINT,
    created_at DATE,
    CONSTRAINT fk_app_user_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===============================
-- TABLE: clients (doit être créée en second)
-- ===============================
CREATE TABLE clients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    telephone VARCHAR(20),
    adresse_par_defaut VARCHAR(255),
    ville_par_defaut VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===============================
-- TABLE: produits
-- ===============================
CREATE TABLE produits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(200) NOT NULL,
    description TEXT,
    prix DECIMAL(10, 2) NOT NULL,
    stock_disponible INT DEFAULT 0,
    categorie VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===============================
-- TABLE: livraisons (dépend de users)
-- ===============================
CREATE TABLE livraisons (
    id_livraison BIGINT AUTO_INCREMENT PRIMARY KEY,
    adresse VARCHAR(255) NOT NULL,
    ville VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    transporteur VARCHAR(100),
    frais_livraison DECIMAL(10, 2) DEFAULT 0.00,
    date_envoi DATE,
    date_estimee DATE,
    user_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_livraison_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===============================
-- TABLE: commandes (dépend de users et livraisons)
-- ===============================
CREATE TABLE commandes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ref_commande VARCHAR(50) UNIQUE NOT NULL,
    date_commande DATE NOT NULL,
    statut VARCHAR(50) DEFAULT 'EN_ATTENTE',
    user_id BIGINT,
    livraison_id BIGINT UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_commande_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_commande_livraison FOREIGN KEY (livraison_id) REFERENCES livraisons(id_livraison) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===============================
-- TABLE: paniers (dépend de clients)
-- ===============================
CREATE TABLE paniers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id BIGINT,
    date_creation DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_panier_client FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
    CONSTRAINT uk_panier_client UNIQUE (client_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===============================
-- TABLE: lignes_commande (dépend de commandes et produits)
-- ===============================
CREATE TABLE lignes_commande (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    commande_id BIGINT NOT NULL,
    produit_id BIGINT NOT NULL,
    quantite INT NOT NULL DEFAULT 1,
    prix_unitaire DECIMAL(10, 2) NOT NULL,
    seller_line_prix DOUBLE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ligne_commande_commande FOREIGN KEY (commande_id) REFERENCES commandes(id) ON DELETE CASCADE,
    CONSTRAINT fk_ligne_commande_produit FOREIGN KEY (produit_id) REFERENCES produits(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===============================
-- TABLE: lignes_panier (dépend de paniers et produits)
-- ===============================
CREATE TABLE lignes_panier (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    panier_id BIGINT NOT NULL,
    produit_id BIGINT NOT NULL,
    quantite INT NOT NULL DEFAULT 1,
    prix_unitaire DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ligne_panier_panier FOREIGN KEY (panier_id) REFERENCES paniers(id) ON DELETE CASCADE,
    CONSTRAINT fk_ligne_panier_produit FOREIGN KEY (produit_id) REFERENCES produits(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===============================
-- TABLE: payments (dépend de commandes)
-- ===============================
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    inv_id VARCHAR(100) NOT NULL UNIQUE,
    client_name VARCHAR(100) NOT NULL,
    product VARCHAR(200) NOT NULL,
    date DATE NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'EN_ATTENTE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===============================
-- INDEX POUR OPTIMISATION
-- ===============================
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_client_email ON clients(email);
CREATE INDEX idx_commande_user ON commandes(user_id);
CREATE INDEX idx_commande_statut ON commandes(statut);
CREATE INDEX idx_livraison_user ON livraisons(user_id);
CREATE INDEX idx_produit_categorie ON produits(categorie);
CREATE INDEX idx_payment_inv_id ON payments(inv_id);
CREATE INDEX idx_payment_status ON payments(status);
CREATE INDEX idx_payment_client_name ON payments(client_name);

-- ===============================
-- CONFIRMATION
-- ===============================
SELECT 'Toutes les tables ont été créées avec succès!' AS Message;
SELECT 'Nombre de tables créées:' AS Info, COUNT(*) AS Total
FROM information_schema.tables
WHERE table_schema = 'b2b_db';

-- Afficher les tables créées
SHOW TABLES;
