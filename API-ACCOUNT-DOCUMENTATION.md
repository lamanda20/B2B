# 📚 API Documentation - Gestion de Compte

## 🔐 Endpoint: Changement de mot de passe

### **PUT** `/api/account/password`

Change le mot de passe de l'utilisateur actuellement connecté.

---

## 🔑 Authentification

**Type**: HTTP Basic Authentication

**Headers requis**:
```
Authorization: Basic base64(email:password)
```

---

## 📥 Request Body

**Content-Type**: `application/json`

```json
{
  "currentPassword": "string",
  "newPassword": "string"
}
```

### Champs

| Champ | Type | Obligatoire | Validation | Description |
|-------|------|-------------|------------|-------------|
| `currentPassword` | string | ✅ Oui | Non vide | Le mot de passe actuel de l'utilisateur |
| `newPassword` | string | ✅ Oui | Min: 8 caractères<br>Max: 100 caractères | Le nouveau mot de passe souhaité |

---

## 📤 Réponses

### ✅ **200 OK** - Succès

Mot de passe changé avec succès.

```json
{
  "success": true,
  "message": "Mot de passe modifié avec succès"
}
```

---

### ❌ **400 Bad Request** - Erreur de validation

Données invalides dans la requête.

**Cas 1: Champs manquants ou invalides**
```json
{
  "success": false,
  "message": "Erreur de validation",
  "errors": {
    "currentPassword": "ne doit pas être vide",
    "newPassword": "la taille doit être comprise entre 8 et 100"
  }
}
```

**Cas 2: Mot de passe actuel incorrect**
```json
{
  "success": false,
  "message": "Mot de passe actuel invalide"
}
```

**Cas 3: Nouveau mot de passe identique à l'ancien**
```json
{
  "success": false,
  "message": "Le nouveau mot de passe doit être différent"
}
```

---

### ❌ **401 Unauthorized** - Non authentifié

L'utilisateur n'est pas authentifié ou les identifiants sont incorrects.

```json
{
  "success": false,
  "message": "Authentification requise"
}
```

---

## 📝 Exemples d'utilisation

### Exemple avec **fetch** (JavaScript)

```javascript
// Fonction pour changer le mot de passe
async function changePassword(email, currentPassword, newPassword) {
  const credentials = btoa(`${email}:${currentPassword}`);
  
  try {
    const response = await fetch('http://localhost:8082/api/account/password', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Basic ${credentials}`
      },
      body: JSON.stringify({
        currentPassword: currentPassword,
        newPassword: newPassword
      })
    });

    const data = await response.json();

    if (response.ok) {
      console.log('✅ Succès:', data.message);
      return data;
    } else {
      console.error('❌ Erreur:', data.message);
      throw new Error(data.message);
    }
  } catch (error) {
    console.error('❌ Erreur réseau:', error);
    throw error;
  }
}

// Utilisation
changePassword('superadmin@b2b.local', 'ChangeMe!123', 'NewSecurePassword123!')
  .then(result => alert('Mot de passe changé avec succès!'))
  .catch(error => alert(`Erreur: ${error.message}`));
```

---

### Exemple avec **axios** (JavaScript/React)

```javascript
import axios from 'axios';

const changePassword = async (email, currentPassword, newPassword) => {
  try {
    const response = await axios.put(
      'http://localhost:8082/api/account/password',
      {
        currentPassword: currentPassword,
        newPassword: newPassword
      },
      {
        auth: {
          username: email,
          password: currentPassword
        },
        headers: {
          'Content-Type': 'application/json'
        }
      }
    );

    console.log('✅ Succès:', response.data.message);
    return response.data;
  } catch (error) {
    if (error.response) {
      // Erreur de la réponse serveur
      console.error('❌ Erreur:', error.response.data.message);
      throw new Error(error.response.data.message);
    } else {
      // Erreur réseau
      console.error('❌ Erreur réseau:', error.message);
      throw error;
    }
  }
};

// Utilisation dans un composant React
const PasswordChangeForm = () => {
  const [formData, setFormData] = useState({
    email: '',
    currentPassword: '',
    newPassword: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      const result = await changePassword(
        formData.email,
        formData.currentPassword,
        formData.newPassword
      );
      setSuccess(result.message);
      // Réinitialiser le formulaire ou rediriger
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      {/* Formulaire ici */}
    </form>
  );
};
```

---

### Exemple avec **cURL**

```bash
curl -X PUT http://localhost:8082/api/account/password \
  -H "Content-Type: application/json" \
  -u "superadmin@b2b.local:ChangeMe!123" \
  -d '{
    "currentPassword": "ChangeMe!123",
    "newPassword": "NewSecurePassword123!"
  }'
```

---

### Exemple avec **Postman**

1. **Method**: PUT
2. **URL**: `http://localhost:8082/api/account/password`
3. **Authorization**: 
   - Type: Basic Auth
   - Username: `superadmin@b2b.local`
   - Password: `ChangeMe!123`
4. **Headers**:
   - `Content-Type`: `application/json`
5. **Body** (raw JSON):
```json
{
  "currentPassword": "ChangeMe!123",
  "newPassword": "NewSecurePassword123!"
}
```

---

## 🎯 Exemple de formulaire HTML/JavaScript complet

```html
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <title>Changement de mot de passe</title>
  <style>
    body { font-family: Arial, sans-serif; max-width: 500px; margin: 50px auto; }
    .form-group { margin-bottom: 15px; }
    label { display: block; margin-bottom: 5px; font-weight: bold; }
    input { width: 100%; padding: 8px; box-sizing: border-box; }
    button { background: #007bff; color: white; padding: 10px 20px; border: none; cursor: pointer; }
    button:hover { background: #0056b3; }
    .error { color: red; margin-top: 10px; }
    .success { color: green; margin-top: 10px; }
  </style>
</head>
<body>
  <h2>🔐 Changement de mot de passe</h2>
  
  <form id="passwordForm">
    <div class="form-group">
      <label for="email">Email</label>
      <input type="email" id="email" required>
    </div>
    
    <div class="form-group">
      <label for="currentPassword">Mot de passe actuel</label>
      <input type="password" id="currentPassword" required>
    </div>
    
    <div class="form-group">
      <label for="newPassword">Nouveau mot de passe (min. 8 caractères)</label>
      <input type="password" id="newPassword" required minlength="8">
    </div>
    
    <div class="form-group">
      <label for="confirmPassword">Confirmer le nouveau mot de passe</label>
      <input type="password" id="confirmPassword" required minlength="8">
    </div>
    
    <button type="submit">Changer le mot de passe</button>
    
    <div id="message"></div>
  </form>

  <script>
    document.getElementById('passwordForm').addEventListener('submit', async (e) => {
      e.preventDefault();
      
      const email = document.getElementById('email').value;
      const currentPassword = document.getElementById('currentPassword').value;
      const newPassword = document.getElementById('newPassword').value;
      const confirmPassword = document.getElementById('confirmPassword').value;
      const messageDiv = document.getElementById('message');
      
      // Validation côté client
      if (newPassword !== confirmPassword) {
        messageDiv.className = 'error';
        messageDiv.textContent = '❌ Les mots de passe ne correspondent pas';
        return;
      }
      
      if (newPassword.length < 8) {
        messageDiv.className = 'error';
        messageDiv.textContent = '❌ Le mot de passe doit contenir au moins 8 caractères';
        return;
      }
      
      // Appel API
      try {
        const credentials = btoa(`${email}:${currentPassword}`);
        const response = await fetch('http://localhost:8082/api/account/password', {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Basic ${credentials}`
          },
          body: JSON.stringify({
            currentPassword: currentPassword,
            newPassword: newPassword
          })
        });
        
        const data = await response.json();
        
        if (response.ok) {
          messageDiv.className = 'success';
          messageDiv.textContent = `✅ ${data.message}`;
          // Réinitialiser le formulaire
          document.getElementById('passwordForm').reset();
        } else {
          messageDiv.className = 'error';
          messageDiv.textContent = `❌ ${data.message}`;
        }
      } catch (error) {
        messageDiv.className = 'error';
        messageDiv.textContent = `❌ Erreur: ${error.message}`;
      }
    });
  </script>
</body>
</html>
```

---

## 🔒 Règles de sécurité

1. ✅ Le mot de passe actuel doit être correct
2. ✅ Le nouveau mot de passe doit avoir au moins 8 caractères
3. ✅ Le nouveau mot de passe doit être différent de l'ancien
4. ✅ L'authentification Basic Auth est requise
5. ✅ Après changement, le flag `mustChangePassword` est automatiquement mis à `false`

---

## 🛠️ Notes techniques

- **Base URL**: `http://localhost:8082/api`
- **Contexte**: `/api`
- **Port**: `8082`
- **Encodage**: UTF-8
- **Format**: JSON

---

## 📊 Codes d'erreur HTTP

| Code | Signification | Description |
|------|---------------|-------------|
| 200 | OK | Opération réussie |
| 400 | Bad Request | Données invalides |
| 401 | Unauthorized | Authentification requise ou invalide |
| 404 | Not Found | Utilisateur non trouvé |
| 500 | Internal Server Error | Erreur serveur |

---

## 🎓 Cas d'usage typiques

### Scénario 1: Premier changement de mot de passe (SuperAdmin)
```
Email: superadmin@b2b.local
Mot de passe actuel: ChangeMe!123
Nouveau mot de passe: MonNouveauMotDePasse2024!
```

### Scénario 2: Changement de mot de passe régulier
```
Email: user@company.com
Mot de passe actuel: OldPassword123
Nouveau mot de passe: NewSecurePass456!
```

---

## 🐛 Débogage

Si vous rencontrez des problèmes:

1. Vérifiez que le serveur est démarré sur le port `8082`
2. Vérifiez l'URL complète: `http://localhost:8082/api/account/password`
3. Vérifiez le header `Authorization` avec les bons identifiants
4. Vérifiez le format JSON du body
5. Consultez les logs du serveur pour plus de détails

---

## 📞 Support

Pour toute question, consultez:
- Les logs de l'application Spring Boot
- La console du navigateur (Network tab)
- Les messages d'erreur retournés par l'API

