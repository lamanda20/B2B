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

- **Page d'accueil:** http://localhost:8080/
- **Dashboard:** http://localhost:8080/dashboard
- **API Health:** http://localhost:8080/api/public/health
- **H2 Console (test):** http://localhost:8080/api/h2-console

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

