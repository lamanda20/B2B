# 📋 Scripts SQL pour Base de Données B2B

## 📁 Fichiers SQL créés

### 1. `init-db.sql` - Initialisation de la base
Crée uniquement la base de données `b2b_db`

### 2. `create-tables.sql` - Création des tables
Crée toutes les tables avec leurs relations :
- ✅ `users` - Utilisateurs
- ✅ `clients` - Clients
- ✅ `produits` - Produits
- ✅ `livraisons` - Livraisons
- ✅ `commandes` - Commandes
- ✅ `lignes_commande` - Détails des commandes
- ✅ `paniers` - Paniers
- ✅ `lignes_panier` - Détails des paniers

### 3. `insert-data.sql` - Données de test
Insère des données de test complètes :
- 5 utilisateurs
- 3 clients
- 10 produits
- 5 livraisons
- 5 commandes
- 11 lignes de commande
- 3 paniers
- 5 lignes de panier

---

## 🚀 Commandes à exécuter dans l'ordre

### Option 1 : Tout en une fois (Automatique)

```cmd
mysql -u root -p < database\create-tables.sql
mysql -u root -p < database\insert-data.sql
```

### Option 2 : Étape par étape

#### Étape 1 : Créer la base de données
```cmd
mysql -u root -p
```

Puis dans MySQL :
```sql
CREATE DATABASE IF NOT EXISTS b2b_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE b2b_db;
```

#### Étape 2 : Créer les tables
```cmd
mysql -u root -p b2b_db < database\create-tables.sql
```

#### Étape 3 : Insérer les données de test (optionnel)
```cmd
mysql -u root -p b2b_db < database\insert-data.sql
```

---

## 📊 Structure des tables créées

### Table: `users`
```sql
id, nom, email, telephone, adresse, ville
```

### Table: `clients`
```sql
id, nom, email, telephone, adresse_par_defaut, ville_par_defaut
```

### Table: `produits`
```sql
id, nom, description, prix, stock_disponible, categorie
```

### Table: `livraisons`
```sql
id_livraison, adresse, ville, telephone, transporteur, 
frais_livraison, date_envoi, date_estimee, user_id
```

### Table: `commandes`
```sql
id, ref_commande, date_commande, statut, user_id, livraison_id
```

### Table: `lignes_commande`
```sql
id, commande_id, produit_id, quantite, prix_unitaire
```

### Table: `paniers`
```sql
id, client_id, date_creation
```

### Table: `lignes_panier`
```sql
id, panier_id, produit_id, quantite, prix_unitaire
```

---

## ✅ Vérification

### Vérifier que les tables sont créées
```sql
USE b2b_db;
SHOW TABLES;
```

### Voir la structure d'une table
```sql
DESCRIBE users;
DESCRIBE commandes;
DESCRIBE livraisons;
```

### Compter les enregistrements
```sql
SELECT COUNT(*) FROM users;
SELECT COUNT(*) FROM produits;
SELECT COUNT(*) FROM commandes;
```

### Voir toutes les commandes
```sql
SELECT 
    c.ref_commande,
    c.date_commande,
    c.statut,
    u.nom AS client,
    l.ville AS destination
FROM commandes c
JOIN users u ON c.user_id = u.id
LEFT JOIN livraisons l ON c.livraison_id = l.id_livraison;
```

---

## 🔄 Alternative : Laisser Hibernate créer les tables

Si vous préférez que Spring Boot/Hibernate crée automatiquement les tables :

1. **Ne lancez PAS** `create-tables.sql`
2. **Créez juste la base** :
   ```sql
   CREATE DATABASE b2b_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
3. **Lancez l'application** avec `spring.jpa.hibernate.ddl-auto=update`
4. **Ensuite**, insérez les données de test :
   ```cmd
   mysql -u root -p b2b_db < database\insert-data.sql
   ```

---

## 🗑️ Nettoyer et recommencer

### Supprimer toutes les données (garder les tables)
```sql
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE lignes_panier;
TRUNCATE TABLE lignes_commande;
TRUNCATE TABLE commandes;
TRUNCATE TABLE livraisons;
TRUNCATE TABLE paniers;
TRUNCATE TABLE clients;
TRUNCATE TABLE produits;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;
```

### Supprimer complètement la base
```sql
DROP DATABASE IF EXISTS b2b_db;
```

Puis recréer avec `create-tables.sql`

---

## 📝 Notes importantes

1. **Ordre d'exécution** : Respectez l'ordre (init → create-tables → insert-data)
2. **Clés étrangères** : Les relations sont configurées avec `ON DELETE CASCADE`
3. **Charset** : UTF-8 (utf8mb4) pour supporter les caractères arabes
4. **Index** : Index créés sur les colonnes fréquemment recherchées
5. **Auto-increment** : Tous les IDs sont en auto-incrémentation

---

## 🆘 En cas de problème

### Erreur "Access denied"
Vérifiez votre mot de passe MySQL

### Erreur "Database exists"
Normal si vous relancez le script, les tables seront recréées

### Erreur de clé étrangère
Exécutez d'abord `create-tables.sql` avant `insert-data.sql`

