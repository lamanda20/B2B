@echo off
chcp 65001 >nul
echo ===============================
echo Installation complète MySQL B2B
echo ===============================
echo.

REM Vérifier MySQL
where mysql >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERREUR] MySQL n'est pas installé
    pause
    exit /b 1
)

echo [OK] MySQL détecté
echo.

REM Demander les identifiants
set /p MYSQL_USER="Utilisateur MySQL (défaut: root): "
if "%MYSQL_USER%"=="" set MYSQL_USER=root

echo.
echo Entrez le mot de passe MySQL:
set /p MYSQL_PASS="Mot de passe: "

echo.
echo ===============================
echo Étape 1/3: Création de la base
echo ===============================
mysql -u %MYSQL_USER% -p%MYSQL_PASS% < database\init-db.sql
if %ERRORLEVEL% NEQ 0 (
    echo [ERREUR] Échec création base
    pause
    exit /b 1
)
echo [OK] Base de données créée

echo.
echo ===============================
echo Étape 2/3: Création des tables
echo ===============================
mysql -u %MYSQL_USER% -p%MYSQL_PASS% < database\create-tables.sql
if %ERRORLEVEL% NEQ 0 (
    echo [ERREUR] Échec création tables
    pause
    exit /b 1
)
echo [OK] Tables créées

echo.
set /p INSERT_DATA="Voulez-vous insérer les données de test? (o/n): "
if /i "%INSERT_DATA%"=="o" (
    echo.
    echo ===============================
    echo Étape 3/3: Insertion des données
    echo ===============================
    mysql -u %MYSQL_USER% -p%MYSQL_PASS% < database\insert-data.sql
    if %ERRORLEVEL% NEQ 0 (
        echo [ERREUR] Échec insertion données
        pause
        exit /b 1
    )
    echo [OK] Données insérées
)

echo.
echo ===============================
echo Installation terminée!
echo ===============================
echo.
echo Base: b2b_db
echo Tables: 8 tables créées
echo.
echo Vérifiez avec:
echo mysql -u %MYSQL_USER% -p%MYSQL_PASS% -e "USE b2b_db; SHOW TABLES;"
echo.
echo Lancez l'application avec: start-app.bat
echo.
pause
-- ===============================
-- SCRIPT COMPLET DE CRÉATION DES TABLES B2B
-- ===============================
-- Base de données: b2b_db
-- Date: 2024-11-10

-- Créer la base de données
CREATE DATABASE IF NOT EXISTS b2b_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Utiliser la base de données
USE b2b_db;

-- ===============================
-- SUPPRESSION DES TABLES (si elles existent)
-- ===============================
DROP TABLE IF EXISTS lignes_panier;
DROP TABLE IF EXISTS lignes_commande;
DROP TABLE IF EXISTS commandes;
DROP TABLE IF EXISTS livraisons;
DROP TABLE IF EXISTS paniers;
DROP TABLE IF EXISTS clients;
DROP TABLE IF EXISTS produits;
DROP TABLE IF EXISTS users;

-- ===============================
-- TABLE: users
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
-- TABLE: clients
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
-- TABLE: livraisons
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
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===============================
-- TABLE: commandes
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
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (livraison_id) REFERENCES livraisons(id_livraison) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===============================
-- TABLE: paniers
-- ===============================
CREATE TABLE paniers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id BIGINT UNIQUE,
    date_creation DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===============================
-- TABLE: lignes_commande
-- ===============================
CREATE TABLE lignes_commande (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    commande_id BIGINT NOT NULL,
    produit_id BIGINT NOT NULL,
    quantite INT NOT NULL DEFAULT 1,
    prix_unitaire DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (commande_id) REFERENCES commandes(id) ON DELETE CASCADE,
    FOREIGN KEY (produit_id) REFERENCES produits(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===============================
-- TABLE: lignes_panier
-- ===============================
CREATE TABLE lignes_panier (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    panier_id BIGINT NOT NULL,
    produit_id BIGINT NOT NULL,
    quantite INT NOT NULL DEFAULT 1,
    prix_unitaire DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (panier_id) REFERENCES paniers(id) ON DELETE CASCADE,
    FOREIGN KEY (produit_id) REFERENCES produits(id) ON DELETE CASCADE
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

-- ===============================
-- CONFIRMATION
-- ===============================
SELECT 'Toutes les tables ont été créées avec succès!' AS Message;

-- Afficher les tables créées
SHOW TABLES;

