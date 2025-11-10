# 🚀 Guide de Configuration MySQL Local

## 📋 Ce qui a été configuré

### Fichiers créés :
- ✅ `application-local.properties` - Configuration MySQL
- ✅ `database/init-db.sql` - Script de création de la base
- ✅ `database/sample-data.sql` - Données de test
- ✅ `setup-mysql.bat` - Installation automatique
- ✅ `start-app.bat` - Démarrage de l'application

### Configuration MySQL :
- **Base de données** : `b2b_db`
- **URL** : `jdbc:mysql://localhost:3306/b2b_db`
- **Username** : `root` (par défaut)
- **Password** : vide (à configurer selon votre installation)

---

## 🎯 Démarrage en 3 étapes

### Étape 1 : Installer MySQL (si pas déjà fait)

**Option A - MySQL Community Server :**
1. Téléchargez : https://dev.mysql.com/downloads/mysql/
2. Installez avec les paramètres par défaut
3. Notez le mot de passe root

**Option B - XAMPP (plus simple) :**
1. Téléchargez : https://www.apachefriends.org/
2. Installez XAMPP
3. Démarrez MySQL depuis le panneau de contrôle

### Étape 2 : Créer la base de données

**Méthode automatique (recommandée) :**
```cmd
setup-mysql.bat
```

**Méthode manuelle :**
```cmd
mysql -u root -p
```
Puis dans MySQL :
```sql
CREATE DATABASE b2b_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Étape 3 : Configurer le mot de passe

Ouvrez `src/main/resources/application-local.properties` et modifiez :

```properties
spring.datasource.username=root
spring.datasource.password=VOTRE_MOT_DE_PASSE_MYSQL
```

**Si vous utilisez XAMPP**, le mot de passe est souvent vide, laissez :
```properties
spring.datasource.password=
```

---

## ▶️ Démarrer l'application

**Méthode 1 - Script batch :**
```cmd
start-app.bat
```

**Méthode 2 - Maven direct :**
```cmd
mvnw.cmd spring-boot:run
```

**Méthode 3 - Depuis votre IDE :**
- Assurez-vous que `spring.profiles.active=local` dans `application.properties`
- Lancez `B2BApplication.java`

---

## 🔍 Vérification

### 1. Vérifier MySQL
```cmd
mysql -u root -p
```
```sql
SHOW DATABASES;
USE b2b_db;
SHOW TABLES;
```

### 2. Tables créées automatiquement
Au premier démarrage, Hibernate créera :
- `users`
- `livraisons`
- `commandes` (si le modèle existe)
- `paniers`
- `produits`
- etc.

### 3. Accéder à l'application
- Dashboard : http://localhost:8082/api/dashboard
- Index : http://localhost:8082/api/

### 4. Connexion admin
- Username : `admin`
- Password : `admin123`

---

## 📊 Ajouter des données de test

**Après le premier démarrage**, insérez des données de test :

```cmd
mysql -u root -p b2b_db < database\sample-data.sql
```

Cela créera :
- 5 utilisateurs de test
- 5 livraisons de test

---

## ⚙️ Configuration avancée

### Créer un utilisateur MySQL dédié

```sql
CREATE USER 'b2b_user'@'localhost' IDENTIFIED BY 'b2b_password';
GRANT ALL PRIVILEGES ON b2b_db.* TO 'b2b_user'@'localhost';
FLUSH PRIVILEGES;
```

Puis dans `application-local.properties` :
```properties
spring.datasource.username=b2b_user
spring.datasource.password=b2b_password
```

### Changer le port MySQL

Si le port 3306 est déjà utilisé, modifiez dans `application-local.properties` :
```properties
spring.datasource.url=jdbc:mysql://localhost:3307/b2b_db?...
```

---

## 🛠️ Commandes MySQL utiles

```sql
-- Voir toutes les bases
SHOW DATABASES;

-- Utiliser la base b2b_db
USE b2b_db;

-- Voir toutes les tables
SHOW TABLES;

-- Voir la structure d'une table
DESCRIBE users;
DESCRIBE livraisons;

-- Voir les données
SELECT * FROM users;
SELECT * FROM livraisons;

-- Compter les enregistrements
SELECT COUNT(*) FROM users;

-- Supprimer toutes les données (ATTENTION!)
TRUNCATE TABLE livraisons;
TRUNCATE TABLE users;

-- Supprimer la base complète (ATTENTION!)
DROP DATABASE b2b_db;
```

---

## ❗ Problèmes courants

### Problème 1 : "mysql n'est pas reconnu"
**Solution :** Ajoutez MySQL au PATH système ou utilisez le chemin complet :
```cmd
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p
```

### Problème 2 : "Access denied"
**Solution :** Vérifiez le mot de passe dans `application-local.properties`

### Problème 3 : "Can't connect to MySQL server"
**Solution :** Vérifiez que MySQL est démarré :
- Sous Windows : Services → MySQL80 → Démarrer
- Sous XAMPP : Panneau de contrôle → Start MySQL

### Problème 4 : "Table doesn't exist"
**Solution :** Démarrez d'abord l'application pour créer les tables, puis insérez les données de test

### Problème 5 : Port 8082 déjà utilisé
**Solution :** Changez le port dans `application-local.properties` :
```properties
server.port=8083
```

---

## 🔄 Basculer entre H2 et MySQL

### Utiliser H2 (test en mémoire) :
Dans `application.properties` :
```properties
spring.profiles.active=test
```

### Utiliser MySQL (local) :
Dans `application.properties` :
```properties
spring.profiles.active=local
```

---

## 📚 Outils recommandés

- **MySQL Workbench** : Interface graphique officielle
- **HeidiSQL** : Alternative légère et gratuite
- **DBeaver** : Outil universel multi-bases
- **phpMyAdmin** : Interface web (inclus avec XAMPP)

---

## 💾 Sauvegarde et restauration

### Sauvegarder :
```cmd
mysqldump -u root -p b2b_db > backup.sql
```

### Restaurer :
```cmd
mysql -u root -p b2b_db < backup.sql
```

---

## ✅ Checklist finale

- [ ] MySQL est installé et démarré
- [ ] Base de données `b2b_db` créée
- [ ] Mot de passe configuré dans `application-local.properties`
- [ ] Profil `local` activé dans `application.properties`
- [ ] Application démarrée sans erreur
- [ ] Tables créées automatiquement
- [ ] Données de test insérées (optionnel)
- [ ] Application accessible sur http://localhost:8082/api

---

## 🆘 Besoin d'aide ?

Si vous rencontrez des problèmes :
1. Vérifiez les logs dans la console
2. Vérifiez que MySQL est démarré
3. Testez la connexion MySQL manuellement
4. Vérifiez le mot de passe dans `application-local.properties`
-- ===============================
-- SCRIPT D'INITIALISATION BASE DE DONNÉES B2B
-- ===============================
-- Ce script crée la base de données pour l'application B2B

-- Créer la base de données
CREATE DATABASE IF NOT EXISTS b2b_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Utiliser la base de données
USE b2b_db;

-- Afficher la confirmation
SELECT 'Base de données b2b_db créée avec succès!' AS Message;

-- Les tables seront créées automatiquement par Hibernate au premier démarrage
-- grâce à la configuration spring.jpa.hibernate.ddl-auto=update

