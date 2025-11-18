 # 📚 API Documentation - Gestion des Livraisons

## 🚚 Base URL
```
http://localhost:8082/api/deliveries
```

---

## 📋 Table des matières
1. [Liste toutes les livraisons](#1-liste-toutes-les-livraisons)
2. [Détails d'une livraison](#2-détails-dune-livraison)
3. [Suivre une livraison par tracking](#3-suivre-une-livraison-par-tracking)
4. [Filtrer par statut](#4-filtrer-par-statut)
5. [Calculer les frais de livraison](#5-calculer-les-frais-de-livraison)
6. [Créer une livraison](#6-créer-une-livraison)
7. [Mettre à jour le statut](#7-mettre-à-jour-le-statut)

---

## 1. Liste toutes les livraisons

### **GET** `/api/deliveries`

Récupère la liste complète de toutes les livraisons avec leurs informations associées.

#### 📥 Request
Aucun paramètre requis.

#### 📤 Response (200 OK)
```json
[
  {
    "id": 1,
    "adresse": "123 Rue Mohammed V",
    "ville": "Casablanca",
    "codePostal": "20000",
    "telephone": "0612345678",
    "transporteur": "DHL",
    "fraisLivraison": 20.0,
    "dateEstimee": "2025-11-15",
    "dateEnvoi": "2025-11-12",
    "trackingNumber": "TRK-1",
    "commandeId": 100,
    "refCommande": "CMD-2025-001",
    "statut": "EXPEDIEE",
    "userId": 5,
    "userName": "Ahmed Alami"
  },
  {
    "id": 2,
    "adresse": "45 Avenue Hassan II",
    "ville": "Rabat",
    "codePostal": "10000",
    "telephone": "0698765432",
    "transporteur": "Amana",
    "fraisLivraison": 20.0,
    "dateEstimee": "2025-11-14",
    "dateEnvoi": "2025-11-11",
    "trackingNumber": "TRK-2",
    "commandeId": 101,
    "refCommande": "CMD-2025-002",
    "statut": "LIVREE",
    "userId": 6,
    "userName": "Fatima Benani"
  }
]
```

#### 💻 Exemple JavaScript
```javascript
async function getAllDeliveries() {
  try {
    const response = await fetch('http://localhost:8082/api/deliveries', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });
    
    const deliveries = await response.json();
    console.log('Livraisons:', deliveries);
    return deliveries;
  } catch (error) {
    console.error('Erreur:', error);
  }
}
```

---

## 2. Détails d'une livraison

### **GET** `/api/deliveries/{id}`

Récupère les détails complets d'une livraison spécifique par son ID.

#### 📥 Request Parameters
| Paramètre | Type | Obligatoire | Description |
|-----------|------|-------------|-------------|
| `id` | Long | ✅ Oui | ID de la livraison |

#### 📤 Response (200 OK)
```json
{
  "id": 1,
  "adresse": "123 Rue Mohammed V",
  "ville": "Casablanca",
  "codePostal": "20000",
  "telephone": "0612345678",
  "transporteur": "DHL",
  "fraisLivraison": 20.0,
  "dateEstimee": "2025-11-15",
  "dateEnvoi": "2025-11-12",
  "trackingNumber": "TRK-1",
  "commandeId": 100,
  "refCommande": "CMD-2025-001",
  "statut": "EXPEDIEE",
  "userId": 5,
  "userName": "Ahmed Alami"
}
```

#### ❌ Response (404 Not Found)
```json
{
  "error": "Livraison non trouvée"
}
```

#### 💻 Exemple JavaScript
```javascript
async function getDeliveryById(deliveryId) {
  try {
    const response = await fetch(`http://localhost:8082/api/deliveries/${deliveryId}`);
    
    if (!response.ok) {
      throw new Error('Livraison non trouvée');
    }
    
    const delivery = await response.json();
    console.log('Détails livraison:', delivery);
    return delivery;
  } catch (error) {
    console.error('Erreur:', error);
  }
}

// Utilisation
getDeliveryById(1);
```

---

## 3. Suivre une livraison par tracking

### **GET** `/api/deliveries/track/{trackingNumber}`

Permet de suivre une livraison en utilisant son numéro de suivi (tracking number).

#### 📥 Request Parameters
| Paramètre | Type | Obligatoire | Description |
|-----------|------|-------------|-------------|
| `trackingNumber` | String | ✅ Oui | Numéro de suivi (format: TRK-XXX) |

#### 📤 Response (200 OK)
```json
{
  "id": 1,
  "adresse": "123 Rue Mohammed V",
  "ville": "Casablanca",
  "codePostal": "20000",
  "telephone": "0612345678",
  "transporteur": "DHL",
  "fraisLivraison": 20.0,
  "dateEstimee": "2025-11-15",
  "dateEnvoi": "2025-11-12",
  "trackingNumber": "TRK-1",
  "commandeId": 100,
  "refCommande": "CMD-2025-001",
  "statut": "EXPEDIEE",
  "userId": 5,
  "userName": "Ahmed Alami"
}
```

#### 💻 Exemple JavaScript
```javascript
async function trackDelivery(trackingNumber) {
  try {
    const response = await fetch(
      `http://localhost:8082/api/deliveries/track/${trackingNumber}`
    );
    
    if (!response.ok) {
      throw new Error('Numéro de suivi invalide');
    }
    
    const delivery = await response.json();
    console.log('Suivi livraison:', delivery);
    return delivery;
  } catch (error) {
    console.error('Erreur:', error);
  }
}

// Utilisation
trackDelivery('TRK-1');
```

#### 🎯 Exemple de formulaire de suivi
```html
<div class="tracking-form">
  <h3>🔍 Suivre ma livraison</h3>
  <input type="text" id="trackingInput" placeholder="Ex: TRK-123" />
  <button onclick="searchTracking()">Rechercher</button>
  <div id="trackingResult"></div>
</div>

<script>
async function searchTracking() {
  const trackingNumber = document.getElementById('trackingInput').value;
  const resultDiv = document.getElementById('trackingResult');
  
  try {
    const response = await fetch(
      `http://localhost:8082/api/deliveries/track/${trackingNumber}`
    );
    
    if (!response.ok) {
      resultDiv.innerHTML = '<p style="color:red">❌ Numéro de suivi introuvable</p>';
      return;
    }
    
    const delivery = await response.json();
    resultDiv.innerHTML = `
      <div class="tracking-info">
        <h4>✅ Livraison trouvée</h4>
        <p><strong>Statut:</strong> ${delivery.statut}</p>
        <p><strong>Transporteur:</strong> ${delivery.transporteur}</p>
        <p><strong>Adresse:</strong> ${delivery.adresse}, ${delivery.ville}</p>
        <p><strong>Date estimée:</strong> ${delivery.dateEstimee}</p>
      </div>
    `;
  } catch (error) {
    resultDiv.innerHTML = '<p style="color:red">❌ Erreur de recherche</p>';
  }
}
</script>
```

---

## 4. Filtrer par statut

### **GET** `/api/deliveries/status/{statut}`

Récupère toutes les livraisons ayant un statut spécifique.

#### 📥 Request Parameters
| Paramètre | Type | Obligatoire | Description |
|-----------|------|-------------|-------------|
| `statut` | String | ✅ Oui | Statut de la commande |

#### 📋 Statuts disponibles
- `EN_ATTENTE` - En attente
- `EN_PREPARATION` - En préparation
- `EXPEDIEE` - Expédiée
- `LIVREE` - Livrée
- `ANNULEE` - Annulée

#### 📤 Response (200 OK)
```json
[
  {
    "id": 1,
    "adresse": "123 Rue Mohammed V",
    "ville": "Casablanca",
    "transporteur": "DHL",
    "fraisLivraison": 20.0,
    "trackingNumber": "TRK-1",
    "commandeId": 100,
    "refCommande": "CMD-2025-001",
    "statut": "EXPEDIEE"
  },
  {
    "id": 3,
    "adresse": "78 Boulevard Zerktouni",
    "ville": "Marrakech",
    "transporteur": "CTM",
    "fraisLivraison": 35.0,
    "trackingNumber": "TRK-3",
    "commandeId": 103,
    "refCommande": "CMD-2025-003",
    "statut": "EXPEDIEE"
  }
]
```

#### ❌ Response (400 Bad Request)
Statut invalide fourni.

#### 💻 Exemple JavaScript
```javascript
async function getDeliveriesByStatus(status) {
  try {
    const response = await fetch(
      `http://localhost:8082/api/deliveries/status/${status}`
    );
    
    if (!response.ok) {
      throw new Error('Statut invalide');
    }
    
    const deliveries = await response.json();
    console.log(`Livraisons ${status}:`, deliveries);
    return deliveries;
  } catch (error) {
    console.error('Erreur:', error);
  }
}

// Utilisation
getDeliveriesByStatus('EXPEDIEE');
getDeliveriesByStatus('LIVREE');
```

#### 🎯 Exemple de filtres
```html
<div class="status-filters">
  <button onclick="filterByStatus('EN_ATTENTE')">En attente</button>
  <button onclick="filterByStatus('EN_PREPARATION')">En préparation</button>
  <button onclick="filterByStatus('EXPEDIEE')">Expédiée</button>
  <button onclick="filterByStatus('LIVREE')">Livrée</button>
</div>
<div id="deliveriesList"></div>

<script>
async function filterByStatus(status) {
  const response = await fetch(
    `http://localhost:8082/api/deliveries/status/${status}`
  );
  const deliveries = await response.json();
  
  const listDiv = document.getElementById('deliveriesList');
  listDiv.innerHTML = deliveries.map(d => `
    <div class="delivery-card">
      <h4>📦 ${d.trackingNumber}</h4>
      <p>Commande: ${d.refCommande}</p>
      <p>Ville: ${d.ville}</p>
      <p>Statut: <span class="status-${d.statut}">${d.statut}</span></p>
    </div>
  `).join('');
}
</script>
```

---

## 5. Calculer les frais de livraison

### **GET** `/api/deliveries/calculate-shipping?city={ville}`

Calcule automatiquement les frais de livraison en fonction de la ville de destination.

#### 📥 Query Parameters
| Paramètre | Type | Obligatoire | Description |
|-----------|------|-------------|-------------|
| `city` | String | ✅ Oui | Nom de la ville |

#### 💰 Tarifs par ville
| Ville(s) | Frais (DH) |
|----------|------------|
| Casablanca, Rabat | 20.00 |
| Marrakech, Fès, Tanger, Agadir | 35.00 |
| Oujda, Tétouan, Meknès | 45.00 |
| Autres villes | 50.00 |

#### 📤 Response (200 OK)
```json
{
  "shippingCost": 20.0
}
```

#### 💻 Exemple JavaScript
```javascript
async function calculateShippingCost(city) {
  try {
    const response = await fetch(
      `http://localhost:8082/api/deliveries/calculate-shipping?city=${encodeURIComponent(city)}`
    );
    
    const data = await response.json();
    console.log(`Frais de livraison pour ${city}:`, data.shippingCost, 'DH');
    return data.shippingCost;
  } catch (error) {
    console.error('Erreur:', error);
  }
}

// Utilisation
calculateShippingCost('Casablanca'); // 20.0 DH
calculateShippingCost('Marrakech');  // 35.0 DH
calculateShippingCost('Oujda');      // 45.0 DH
calculateShippingCost('Essaouira');  // 50.0 DH
```

#### 🎯 Exemple de calculateur de frais
```html
<div class="shipping-calculator">
  <h3>💰 Calculateur de frais de livraison</h3>
  <select id="citySelect" onchange="updateShippingCost()">
    <option value="">-- Sélectionnez une ville --</option>
    <option value="Casablanca">Casablanca</option>
    <option value="Rabat">Rabat</option>
    <option value="Marrakech">Marrakech</option>
    <option value="Fès">Fès</option>
    <option value="Tanger">Tanger</option>
    <option value="Agadir">Agadir</option>
    <option value="Oujda">Oujda</option>
    <option value="Tétouan">Tétouan</option>
    <option value="Meknès">Meknès</option>
  </select>
  <div id="shippingResult"></div>
</div>

<script>
async function updateShippingCost() {
  const city = document.getElementById('citySelect').value;
  const resultDiv = document.getElementById('shippingResult');
  
  if (!city) {
    resultDiv.innerHTML = '';
    return;
  }
  
  const response = await fetch(
    `http://localhost:8082/api/deliveries/calculate-shipping?city=${city}`
  );
  const data = await response.json();
  
  resultDiv.innerHTML = `
    <div class="shipping-cost">
      <h4>Frais de livraison</h4>
      <p class="price">${data.shippingCost} DH</p>
    </div>
  `;
}
</script>
```

---

## 6. Créer une livraison

### **POST** `/api/deliveries`

Crée une nouvelle livraison. Les frais sont calculés automatiquement si non fournis.

#### 📥 Request Body
```json
{
  "adresse": "123 Rue Mohammed V",
  "ville": "Casablanca",
  "codePostal": "20000",
  "telephone": "0612345678",
  "transporteur": "DHL",
  "fraisLivraison": 20.0,
  "dateEnvoi": "2025-11-12",
  "dateEstimee": "2025-11-15"
}
```

**Note**: Si `fraisLivraison` est omis ou égal à 0, il sera calculé automatiquement selon la ville.

#### 📤 Response (200 OK)
```json
{
  "id": 1,
  "trackingNumber": "TRK-1",
  "message": "Livraison créée avec succès"
}
```

#### 💻 Exemple JavaScript
```javascript
async function createDelivery(deliveryData) {
  try {
    const response = await fetch('http://localhost:8082/api/deliveries', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(deliveryData)
    });
    
    const result = await response.json();
    console.log('Livraison créée:', result);
    alert(`✅ Livraison créée avec succès!\nNuméro de suivi: ${result.trackingNumber}`);
    return result;
  } catch (error) {
    console.error('Erreur:', error);
    alert('❌ Erreur lors de la création de la livraison');
  }
}

// Utilisation
const newDelivery = {
  adresse: "123 Rue Mohammed V",
  ville: "Casablanca",
  codePostal: "20000",
  telephone: "0612345678",
  transporteur: "DHL",
  dateEnvoi: "2025-11-12",
  dateEstimee: "2025-11-15"
  // fraisLivraison sera calculé automatiquement
};

createDelivery(newDelivery);
```

#### 🎯 Exemple de formulaire complet
```html
<form id="deliveryForm" onsubmit="submitDelivery(event)">
  <h3>📦 Créer une nouvelle livraison</h3>
  
  <div class="form-group">
    <label>Adresse *</label>
    <input type="text" name="adresse" required>
  </div>
  
  <div class="form-group">
    <label>Ville *</label>
    <select name="ville" required onchange="autoCalculateShipping()">
      <option value="">-- Sélectionnez --</option>
      <option value="Casablanca">Casablanca</option>
      <option value="Rabat">Rabat</option>
      <option value="Marrakech">Marrakech</option>
      <option value="Fès">Fès</option>
      <option value="Tanger">Tanger</option>
    </select>
  </div>
  
  <div class="form-group">
    <label>Code Postal</label>
    <input type="text" name="codePostal">
  </div>
  
  <div class="form-group">
    <label>Téléphone *</label>
    <input type="tel" name="telephone" required>
  </div>
  
  <div class="form-group">
    <label>Transporteur</label>
    <select name="transporteur">
      <option value="DHL">DHL</option>
      <option value="Amana">Amana</option>
      <option value="CTM">CTM</option>
    </select>
  </div>
  
  <div class="form-group">
    <label>Frais de livraison (DH)</label>
    <input type="number" name="fraisLivraison" id="fraisLivraison" readonly>
  </div>
  
  <button type="submit">Créer la livraison</button>
</form>

<script>
async function autoCalculateShipping() {
  const ville = document.querySelector('[name="ville"]').value;
  if (!ville) return;
  
  const response = await fetch(
    `http://localhost:8082/api/deliveries/calculate-shipping?city=${ville}`
  );
  const data = await response.json();
  document.getElementById('fraisLivraison').value = data.shippingCost;
}

async function submitDelivery(event) {
  event.preventDefault();
  
  const formData = new FormData(event.target);
  const deliveryData = {
    adresse: formData.get('adresse'),
    ville: formData.get('ville'),
    codePostal: formData.get('codePostal'),
    telephone: formData.get('telephone'),
    transporteur: formData.get('transporteur'),
    fraisLivraison: parseFloat(formData.get('fraisLivraison')) || 0,
    dateEnvoi: new Date().toISOString().split('T')[0]
  };
  
  const response = await fetch('http://localhost:8082/api/deliveries', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(deliveryData)
  });
  
  const result = await response.json();
  alert(`✅ ${result.message}\nNuméro de suivi: ${result.trackingNumber}`);
  event.target.reset();
}
</script>
```

---

## 7. Mettre à jour le statut

### **POST** `/api/deliveries/{id}/status`

Met à jour le statut d'une livraison (et de sa commande associée).

#### 📥 Request Parameters
| Paramètre | Type | Obligatoire | Description |
|-----------|------|-------------|-------------|
| `id` | Long | ✅ Oui | ID de la livraison |

#### 📥 Request Body
```json
{
  "status": "LIVREE"
}
```

#### 📋 Statuts disponibles
- `EN_ATTENTE`
- `EN_PREPARATION`
- `EXPEDIEE`
- `LIVREE`
- `ANNULEE`

#### 📤 Response (200 OK)
```json
{
  "id": 1,
  "message": "Statut mis à jour"
}
```

#### ❌ Response (404 Not Found)
Livraison non trouvée.

#### 💻 Exemple JavaScript
```javascript
async function updateDeliveryStatus(deliveryId, newStatus) {
  try {
    const response = await fetch(
      `http://localhost:8082/api/deliveries/${deliveryId}/status`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ status: newStatus })
      }
    );
    
    if (!response.ok) {
      throw new Error('Erreur lors de la mise à jour');
    }
    
    const result = await response.json();
    console.log('Statut mis à jour:', result);
    alert('✅ Statut mis à jour avec succès!');
    return result;
  } catch (error) {
    console.error('Erreur:', error);
    alert('❌ Erreur lors de la mise à jour du statut');
  }
}

// Utilisation
updateDeliveryStatus(1, 'EXPEDIEE');
updateDeliveryStatus(2, 'LIVREE');
```

#### 🎯 Exemple d'interface de gestion
```html
<div class="delivery-manager">
  <h3>Gérer le statut de livraison #<span id="deliveryId"></span></h3>
  
  <div class="current-status">
    <p>Statut actuel: <strong id="currentStatus"></strong></p>
  </div>
  
  <div class="status-buttons">
    <button onclick="changeStatus('EN_ATTENTE')" class="btn-waiting">
      En attente
    </button>
    <button onclick="changeStatus('EN_PREPARATION')" class="btn-preparing">
      En préparation
    </button>
    <button onclick="changeStatus('EXPEDIEE')" class="btn-shipped">
      Expédiée
    </button>
    <button onclick="changeStatus('LIVREE')" class="btn-delivered">
      Livrée
    </button>
    <button onclick="changeStatus('ANNULEE')" class="btn-cancelled">
      Annulée
    </button>
  </div>
</div>

<script>
let currentDeliveryId = 1; // À remplacer par l'ID réel

async function changeStatus(newStatus) {
  if (!confirm(`Confirmer le changement de statut vers: ${newStatus}?`)) {
    return;
  }
  
  const response = await fetch(
    `http://localhost:8082/api/deliveries/${currentDeliveryId}/status`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: newStatus })
    }
  );
  
  if (response.ok) {
    const result = await response.json();
    alert('✅ ' + result.message);
    document.getElementById('currentStatus').textContent = newStatus;
  } else {
    alert('❌ Erreur lors de la mise à jour');
  }
}
</script>
```

---

## 📊 Résumé des Endpoints

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/deliveries` | Liste toutes les livraisons |
| GET | `/api/deliveries/{id}` | Détails d'une livraison |
| GET | `/api/deliveries/track/{trackingNumber}` | Suivre par numéro de suivi |
| GET | `/api/deliveries/status/{statut}` | Filtrer par statut |
| GET | `/api/deliveries/calculate-shipping?city=XXX` | Calculer les frais |
| POST | `/api/deliveries` | Créer une livraison |
| POST | `/api/deliveries/{id}/status` | Mettre à jour le statut |

---

## 🔐 Authentification

Tous les endpoints nécessitent une authentification Basic Auth (sauf si configuré autrement).

**Headers requis**:
```
Authorization: Basic base64(email:password)
```

---

## 🌐 CORS

Le controller est configuré avec:
```java
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
```

Cela permet les requêtes cross-origin depuis n'importe quelle origine.

---

## 🐛 Gestion des erreurs

### Codes HTTP
- `200` - Succès
- `400` - Requête invalide (statut incorrect, etc.)
- `404` - Ressource non trouvée
- `500` - Erreur serveur

### Exemple de gestion d'erreurs
```javascript
async function safeApiCall(url, options = {}) {
  try {
    const response = await fetch(url, options);
    
    if (!response.ok) {
      if (response.status === 404) {
        throw new Error('Ressource non trouvée');
      } else if (response.status === 400) {
        throw new Error('Requête invalide');
      } else {
        throw new Error('Erreur serveur');
      }
    }
    
    return await response.json();
  } catch (error) {
    console.error('Erreur API:', error);
    throw error;
  }
}
```

---

## 💡 Conseils d'intégration

1. **Tracking Number**: Toujours au format `TRK-XXX`
2. **Frais automatiques**: Laissez `fraisLivraison` à 0 pour calcul auto
3. **Dates**: Format `YYYY-MM-DD`
4. **Statuts**: Toujours en MAJUSCULES
5. **Ville**: Sensible à la casse pour le calcul des frais

---

## 📞 Support

Pour toute question:
- Vérifiez les logs Spring Boot
- Testez avec Postman ou curl
- Vérifiez la console du navigateur (Network tab)

