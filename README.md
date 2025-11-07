# B2B
B2B JAVA PROJECT avec Spring Boot

## 📋 Prérequis

- Java 17 ou supérieur
- Maven 3.6+
- MySQL 8.0+ (ou utiliser H2 pour les tests)
- IDE (IntelliJ IDEA, Eclipse, ou VS Code)

## 🚀 Installation et Configuration

### 1. Cloner le projet
```bash
git clone <repository-url>
cd B2B
```

### 2. Configuration de la base de données

#### Option A: MySQL (Recommandé pour dev/prod)
1. Installer MySQL
2. Créer une base de données:
```sql
CREATE DATABASE b2b_db_dev;
```
3. Mettre à jour `src/main/resources/application-dev.properties` avec vos credentials MySQL:
```properties
spring.datasource.username=votre_username
spring.datasource.password=votre_password
```

#### Option B: H2 (Pour tests rapides)
Utiliser le profil `test` - aucune configuration nécessaire

### 3. Build le projet
```bash
mvnw.cmd clean install
```

### 4. Lancer l'application
```bash
# Mode développement
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev

# Mode test
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=test

# Mode production
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod
```

## 📁 Structure du Projet

```
B2B/
├── src/
│   ├── main/
│   │   ├── java/com/b2b/
│   │   │   ├── B2BApplication.java          # Point d'entrée
│   │   │   ├── config/                      # Configurations
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── WebConfig.java
│   │   │   ├── controller/                  # REST Controllers
│   │   │   ├── service/                     # Business Logic
│   │   │   ├── repository/                  # Data Access
│   │   │   ├── model/                       # Entities
│   │   │   ├── dto/                         # Data Transfer Objects
│   │   │   └── exception/                   # Exception Handling
│   │   └── resources/
│   │       ├── application.properties       # Configuration principale
│   │       ├── application-dev.properties   # Config développement
│   │       ├── application-test.properties  # Config test
│   │       └── application-prod.properties  # Config production
│   └── test/                                # Tests unitaires
├── pom.xml                                  # Dépendances Maven
└── README.md
```

## 🔧 Configuration des Profils

### Profil `dev` (Développement)
- Port: 8080
- Base de données: MySQL (b2b_db_dev)
- Logs: DEBUG
- DDL: update

### Profil `test` (Tests)
- Port: 8081
- Base de données: H2 (en mémoire)
- Console H2: http://localhost:8081/api/h2-console
- DDL: create-drop

### Profil `prod` (Production)
- Port: 8080
- Base de données: MySQL (configuration via variables d'environnement)
- Logs: WARN/INFO
- DDL: validate

## 🛠️ Technologies Utilisées

- **Java 21** (LTS)
- **Spring Boot 3.3.5**
- **Spring Web** - REST APIs
- **Spring Data JPA** - Accès aux données
- **Spring Security** - Sécurité
- **MySQL** - Base de données production
- **H2** - Base de données tests
- **Lombok** - Réduction du code boilerplate
- **Maven** - Gestion des dépendances

## 📡 Endpoints

### Health Check
```
GET /api/public/health
```
Retourne le statut de l'application (accessible sans authentification)

## 🔐 Sécurité

Par défaut, l'application utilise Spring Security avec:
- Username: `admin`
- Password: `admin123`

**⚠️ À changer en production!**

Les endpoints `/api/public/**` sont accessibles sans authentification.

## 👥 Pour l'Équipe

### Configuration Locale
Chaque développeur peut créer un fichier `application-local.properties` (ignoré par git) pour ses configurations personnelles:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ma_db_locale
spring.datasource.username=mon_user
spring.datasource.password=mon_password
```

Puis lancer avec: `mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local`

### Bonnes Pratiques
1. Toujours travailler sur une branche feature
2. Tester en local avant de push
3. Utiliser le profil `dev` pour le développement
4. Ne jamais commiter les mots de passe réels
5. Suivre la structure des packages existante

## 📝 Commandes Utiles

```bash
# Nettoyer et compiler
mvnw.cmd clean install

# Lancer les tests
mvnw.cmd test

# Lancer l'application
mvnw.cmd spring-boot:run

# Package l'application
mvnw.cmd package

# Lancer le JAR
java -jar target/b2b-application-1.0.0-SNAPSHOT.jar --spring.profiles.active=dev
```

## 🐛 Debugging

Pour déboguer l'application:
```bash
mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

Puis connecter votre debugger sur le port 5005.

## 📦 Déploiement

### Build Production
```bash
mvnw.cmd clean package -Pprod
```

Le JAR sera généré dans `target/b2b-application-1.0.0-SNAPSHOT.jar`

## 📞 Support

Pour toute question, contacter l'équipe de développement.
