# Architecture MVC du Projet B2B

## Structure MVC Complete

### 1. **Model** (Modele)
**Emplacement:** `src/main/java/com/b2b/model/`

Les modeles representent les donnees et la logique metier:
- **Entities JPA** - Classes annotees avec `@Entity`
- **BaseEntity** - Classe de base pour toutes les entites
- Mapping avec la base de donnees

**Exemple:**
```java
@Entity
public class Product extends BaseEntity {
    private String name;
    private Double price;
    // getters/setters
}
```

### 2. **View** (Vue)
**Emplacement:** `src/main/resources/templates/`

Les vues utilisent Thymeleaf pour afficher les donnees:
- **Templates HTML** - Fichiers `.html` avec syntaxe Thymeleaf
- **CSS/JS** - Dans `src/main/resources/static/`
- Rendering cote serveur

**Fichiers de vue:**
- `index.html` - Page d'accueil
- `dashboard.html` - Tableau de bord
- `css/style.css` - Styles CSS

**Syntaxe Thymeleaf:**
```html
<h1 th:text="${title}">Titre</h1>
<a th:href="@{/dashboard}">Dashboard</a>
```

### 3. **Controller** (Controleur)
**Emplacement:** `src/main/java/com/b2b/controller/`

Deux types de controllers:

#### A. View Controllers (MVC)
Utilisent `@Controller` et retournent des vues:
```java
@Controller
public class HomeViewController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Accueil");
        return "index"; // retourne la vue index.html
    }
}
```

#### B. REST Controllers (API)
Utilisent `@RestController` et retournent du JSON:
```java
@RestController
@RequestMapping("/api")
public class ProductRestController {
    @GetMapping("/products")
    public List<Product> getProducts() {
        return productService.findAll();
    }
}
```

## Flux MVC

```
Client (Browser)
    |
    | HTTP Request
    v
Controller (@Controller)
    |
    | Prepare data
    v
Model (Entities + Services)
    |
    | Add to Model
    v
View (Thymeleaf Template)
    |
    | Render HTML
    v
Client (Browser)
```

## Organisation des Packages

```
com.b2b/
├── model/              # Model Layer
│   ├── BaseEntity.java
│   └── Product.java (example)
│
├── controller/         # Controller Layer
│   ├── view/          # Controllers pour les vues HTML
│   │   └── HomeViewController.java
│   └── api/           # Controllers REST API (JSON)
│       └── HealthController.java
│
├── service/           # Business Logic
│   └── ProductService.java
│
├── repository/        # Data Access
│   └── ProductRepository.java
│
└── dto/              # Data Transfer Objects
    └── ApiResponse.java
```

## Ressources

```
resources/
├── templates/         # Vues Thymeleaf (HTML)
│   ├── index.html
│   └── dashboard.html
│
├── static/           # Ressources statiques
│   ├── css/
│   │   └── style.css
│   ├── js/
│   └── images/
│
└── application.properties  # Configuration
```

## Acces aux Pages

- **Page d'accueil:** http://localhost:8082/
- **Dashboard:** http://localhost:8082/dashboard
- **API Health:** http://localhost:8082/api/public/health
- **H2 Console (test):** http://localhost:8082/api/h2-console

## Accès API pour le frontend JavaFX

Important: le backend Spring Boot est configuré pour écouter par défaut sur le port 8082 (voir `src/main/resources/application.properties`). Si ton client JavaFX utilisait `http://localhost:8080`, il recevra la réponse d'un autre service (ex: Oracle) et potentiellement du HTML au lieu du JSON attendu.

Voici deux snippets Java prêts à coller dans ton code JavaFX (ou tout client Java) : l'un avec `HttpClient` (Java 11+) et l'autre avec `HttpURLConnection`. Les deux vérifient le Content-Type et évitent de parser du HTML comme du JSON.

-- HttpClient (Java 11+)

```java
import java.net.http.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.*;

HttpClient client = HttpClient.newHttpClient();
String villeEncoded = URLEncoder.encode(ville, StandardCharsets.UTF_8);
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("http://localhost:8082/api/livraisons/calculate-frais?ville=" + villeEncoded))
    .GET()
    .header("Accept", "application/json")
    .build();

HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
String body = response.body();
String contentType = response.headers().firstValue("Content-Type").orElse("");

if (contentType.contains("application/json") || body.trim().startsWith("{")) {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(body);
    double frais = root.path("frais").asDouble();
    String villeResp = root.path("ville").asText();
    // utiliser frais et villeResp dans l'UI
} else {
    // Réponse inattendue (HTML ou autre) — afficher/logguer le corps pour debug
    System.err.println("Réponse inattendue (content-type=" + contentType + "):\n" + body);
    // afficher une erreur utilisateur dans l'UI JavaFX
}
```

-- HttpURLConnection (compatible Java 8)

```java
import java.net.*;
import java.io.*;

URL url = new URL("http://localhost:8082/api/livraisons/calculate-frais?ville=" + URLEncoder.encode(ville, "UTF-8"));
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
conn.setRequestMethod("GET");
conn.setRequestProperty("Accept", "application/json");
int status = conn.getResponseCode();
InputStream is = (status >= 200 && status < 400) ? conn.getInputStream() : conn.getErrorStream();
String contentType = conn.getHeaderField("Content-Type");
String body;
try (BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = in.readLine()) != null) sb.append(line).append('\n');
    body = sb.toString();
}
if (contentType != null && contentType.contains("application/json") || body.trim().startsWith("{")) {
    // parser JSON (ex: avec Jackson ou org.json)
} else {
    // Réponse inattendue : log + afficher message à l'utilisateur
    System.err.println("Réponse inattendue (" + contentType + "):\n" + body);
}
```

Notes pratiques :
- Si tu veux conserver `http://localhost:8080` comme adresse backend, il faudra libérer le port 8080 (arrêter le service Oracle qui écoute sur ce port) puis modifier `application.properties` (ou supprimer `server.port`) et redémarrer l'application Spring Boot.
- Pour éviter toute confusion, préfère utiliser `http://localhost:8082/api/...` dans ton frontend ou harmoniser les docs.

## Configuration Thymeleaf

Dans `application.properties`:
```properties
spring.thymeleaf.cache=false
spring.thymeleaf.enabled=true
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
```

## Bonnes Pratiques

1. **Separation des responsabilites:**
   - Model = Donnees
   - View = Presentation
   - Controller = Logique de navigation

2. **Controllers:**
   - View Controllers dans `controller.view`
   - REST Controllers dans `controller.api`

3. **Views:**
   - Utiliser les layouts Thymeleaf pour eviter la duplication
   - Nommer les templates en minuscules

4. **Security:**
   - Configurer les acces dans `SecurityConfig.java`
   - Proteger les routes sensibles

## Prochaines Etapes

1. Creer des entities dans `model/`
2. Creer des repositories dans `repository/`
3. Creer des services dans `service/`
4. Creer des controllers dans `controller/view/`
5. Creer des templates dans `templates/`

