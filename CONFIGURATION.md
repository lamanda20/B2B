# Configuration Guide / Guide de Configuration

## Profiles Disponibles

### 1. Profil `dev` (Développement)
Utilisé pour le développement local avec MySQL.

**Configuration:**
- Modifier `application-dev.properties`
- Changer username/password MySQL selon votre installation

### 2. Profil `test` (Tests)
Utilisé pour les tests avec base de données H2 en mémoire.

**Aucune configuration nécessaire** - Fonctionne out of the box.

### 3. Profil `prod` (Production)
Utilisé pour la production avec variables d'environnement.

**Variables d'environnement requises:**
```
DB_USERNAME=votre_username_prod
DB_PASSWORD=votre_password_prod
```

### 4. Profil `local` (Personnel)
Créez votre propre fichier `application-local.properties` pour vos configs personnelles.

## Comment Changer de Profil

### Ligne de commande:
```bash
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

### Dans application.properties:
```properties
spring.profiles.active=dev
```

### Variable d'environnement:
```
SPRING_PROFILES_ACTIVE=dev
```

### Dans IntelliJ IDEA:
1. Run > Edit Configurations
2. Active profiles: `dev`

## Propriétés Communes

Toutes les propriétés de `application.properties` sont héritées par les profils spécifiques.
Les profils peuvent surcharger ces propriétés.

## Sécurité

⚠️ **NE JAMAIS COMMITER:**
- Mots de passe réels
- Clés API
- Tokens
- Fichier `application-local.properties`

✅ **Utiliser à la place:**
- Variables d'environnement en production
- Gestionnaire de secrets (Azure KeyVault, AWS Secrets Manager, etc.)
