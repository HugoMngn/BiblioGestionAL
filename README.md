# BiblioGestionAL — Documentation du code source

## 1. Présentation générale

Projet Spring Boot fournissant une application de gestion de bibliothèque (API REST + couche métier + persistance). Le code est organisé selon les bonnes pratiques courantes : contrôleurs (API), services (logique métier), repositories (accès aux données), entités (modèles de domaine), DTOs (contrats d'entrée/sortie), configuration et utilitaires.

But principal

- Permettre la gestion des utilisateurs, des livres, des prêts et des notifications.

Stack principale

- Java + Spring Boot
- Lombok pour réduire le boilerplate (getters/setters, builders, constructeurs)
- Persistance via Spring Data JPA / repositories (hypothèse basée sur la structure)
- Gradle comme système de build

## 2. Arborescence importante (extraits)

- `src/main/java/com/example/BiblioGestionAL/`
  - `config/` — classes de configuration (CORS, sécurité, base de données, web)
  - `controller/` — API endpoints (AdminController, AuthController, BookController, LoanController)
  - `dto/` — objets de transfert (AuthDTOs, BookDTO, LoanDTO)
  - `entity/` — entités métier (Book, Loan, Notification, Role, User)
  - `facade/` — façade du domaine (LibraryFacade, LibraryFacadeImpl, LibraryFacadeProxy)
  - `factory/` — fabriques (ex : UserFactory)
  - `repository/` — interfaces d'accès aux données (BookRepository, LoanRepository, NotificationRepository, UserRepository)
  - `service/` — services métier (BookService, LoanService, NotificationService, UserService)
  - `util/` — utilitaires (DBConnectionSingleton)
- `src/main/resources/` — configuration (`application.yml` / `application.properties`, `data.sql`, ressources statiques)

> Remarque : cette documentation s'appuie sur l'arborescence fournie et décrit les responsabilités et relations attendues entre les composants.

## 3. Description des packages et classes clés

### config

- `CORSConfig` — configuration des politiques CORS pour l'API.
- `DatabaseConnection` — configuration / utilitaire pour établir la connexion à la base.
- `SecurityConfig` — configuration de la sécurité (authentification/autorisation). Peut contenir : filtres JWT, gestion des endpoints publics, encodage des mots de passe.
- `WebConfig` — configuration web générale (résolveurs de templates, localisation, formatters).

But : centraliser la configuration applicative de niveau framework (Spring MVC, sécurité, DB).

### controller

Responsabilités générales : exposer des endpoints REST, valider/sanitiser les requêtes, convertir DTO ↔ entités ou déléguer aux façades/services.

- `AuthController` — endpoints d'authentification et gestion du compte : inscription (register), connexion (login), mise à jour du profil, changement de mot de passe.
- `BookController` — CRUD sur les livres : création, lecture (par id / recherche), mise à jour, suppression. Peut également exposer des endpoints pour le catalogue (pagination, filtres).
- `LoanController` — endpoints pour emprunter / rendre un livre, lister les prêts d'un utilisateur, état des prêts.
- `AdminController` — endpoints réservés aux administrateurs (gestion des utilisateurs, des livres en masse, rapports).

Principes : controllers doivent rester fins — la logique métier principale est dans les services/facade.

### dto

- `AuthDTOs` — classes internes : `RegisterRequest`, `LoginRequest`, `LoginResponse`, `ProfileUpdateRequest`, `PasswordChangeRequest`. Utilisation de Lombok (`@Getter/@Setter/@Builder/@NoArgsConstructor/@AllArgsConstructor`), facilite sérialisation/desérialisation JSON.
- `BookDTO`, `LoanDTO` — contrats d'API pour limiter l'exposition directe des entités (sécurité, découplage).

Règles : valider les DTO (ex : `@NotNull`, `@Size`) aux contrôleurs pour éviter d'entrer en logique métier des données invalides.

### entity

- `User` — modèle utilisateur (username, password hash, fullName, roles, ...).
- `Role` — droits (ex. ROLE_USER, ROLE_ADMIN).
- `Book` — informations sur les livres (titre, auteur, isbn, nombre d'exemplaires, etc.).
- `Loan` — représente un emprunt (user, book, dateEmprunt, dateRetourPrévue, dateRetourRéelle, état).
- `Notification` — notifications liées aux prêts (rappels, retards).

Conventions attendues : entités annotées JPA (`@Entity`), relations appropriées (`@ManyToOne`, `@OneToMany`), gestion des indices/contraintes via annotations.

### repository

Interfaces d'accès aux données, typiquement héritant de `JpaRepository<T, ID>` et exposant méthodes de recherche personnalisées (ex : findByUsername, findByIsbn, findByUserAndReturnedFalse).

### service

Couche métier : validation avancée, règles d'emprunt (limites, disponibilité), envoi de notifications, orchestration de transactions et appels aux repositories.

- `UserService` — gestion des comptes, encodage des mots de passe, récupération par username.
- `BookService` — logique CRUD et disponibilité des exemplaires.
- `LoanService` — créer un prêt, vérifier disponibilité, marquer un retour, pénalités éventuelles.
- `NotificationService` — création et dispatch de notifications (peut déléguer à un système d'email/queue).

Règle : services doivent être testables (injection des repositories), exécuter la logique métier et laisser la persistance aux repositories.

### facade

`LibraryFacade` / `LibraryFacadeImpl` / `LibraryFacadeProxy` — pattern façade pour centraliser certaines opérations complexes ou exposer une interface simplifiée aux controllers, tout en permettant ajout d'un proxy (ex : gestion des transactions, journalisation, cache, ou autorisations transversales).

### factory

`UserFactory` — centralise la création d'objets User (hashing du mot de passe, assignation de rôles par défaut), utile pour éviter la duplication.

### util

`DBConnectionSingleton` — utilitaire pour fournir une source de connection unique (attention : dans un contexte Spring Boot, préférez `DataSource` géré par le conteneur plutôt qu'un singleton manuel).

## 4. Principaux choix d'implémentation (justifications)

- Architecture en couches (Controller → Facade → Service → Repository) : séparation des responsabilités, testabilité, maintenabilité.
- Usage de DTOs : empêche l'exposition des entités JPA directement dans l'API et facilite l'évolution du contrat API.
- Lombok : réduit le code répétitif, clarifie les POJOs. Attention : IDE doit supporter Lombok pour éviter des faux positifs de compilation dans l'IDE.
- Façade + Proxy : utile pour ajouter des comportements transverses (logging, métriques, contrôle d'accès) sans polluer la logique métier.
- Scripts `data.sql` : pratique pour des jeux de données initiaux en développement, mais en production préférer des migrations (Flyway/Liquibase).

#### Patterns utilisés et pourquoi

- `Facade` : centralise et simplifie les opérations complexes côté application (ex. coordination entre plusieurs services/repositories). Les controllers appellent la façade plutôt que plusieurs services à la fois, ce qui réduit le couplage et facilite l'ajout de logique transversale.
- `Proxy` : utilisé (via `LibraryFacadeProxy`) pour intercepter les appels à la façade afin d'ajouter des comportements transverses (journalisation, gestion des transactions, contrôle d'accès, métriques) sans modifier la façade métier.
- `Factory` : (`UserFactory`) pour encapsuler la création d'objets complexes (initialisation d'un `User`, hashing du mot de passe, assignation de rôles par défaut). Facilite la réutilisation et la testabilité.
- `Repository` (DAO) : séparation de la persistance (interfaces `*Repository`) et de la logique métier. Facilite le mocking en tests et permet d'exploiter les abstractions Spring Data JPA.
- `Singleton` (utilitaire) : `DBConnectionSingleton` fournit une instance unique pour la connexion DB quand un accès centralisé est nécessaire. Note : dans Spring Boot, préférer un `DataSource` géré par Spring plutôt qu'un singleton manuel.
- `DTO` / `Builder` : les DTOs (ex : `AuthDTOs`, `BookDTO`) découpent le modèle de persistance et le contrat API. Le pattern `Builder` (via Lombok `@Builder`) facilite la construction d'objets immuables ou partiellement renseignés et améliore la lisibilité des tests et du code.
- `MVC` (Controller → Service → Repository) : architecture en couches qui suit implicitement le pattern Model-View-Controller côté API (controllers exposent les routes, services contiennent la logique, repositories gèrent la persistance).
- `Observer` / Événements (conceptuel) : même si le code fourni utilise un `NotificationService`, il est recommandé d'utiliser un pattern d'événements / observer (ou Spring Events) pour découpler la génération d'événements (p.ex. prêt en retard) et le dispatch des notifications.

Pourquoi ces patterns ?

- Ils améliorent la séparation des responsabilités (SRP) : chaque pattern vise à isoler une responsabilité claire (création, persistance, orchestration, aspects transverses).
- Ils facilitent la testabilité : factories et repositories rendent le code plus simple à mocker; les façades limitent le nombre de dépendances à mocker dans les controllers.
- Ils simplifient l'évolution : Proxy et Facade permettent d'ajouter des comportements transverses sans toucher la logique métier. DTOs et Builders limitent l'impact des changements de schéma sur l'API.
- Ils aident la maintenabilité et la lecture du code pour de futurs contributeurs en suivant des conventions bien connues.

## 5. Contrat rapide (inputs/outputs, modes d'erreur)

- Endpoints JSON-REST.
- Inputs : DTOs JSON (p. ex. `RegisterRequest`), validés côté serveur.
- Outputs : DTOs ou objets JSON contenant statut/message/données.
- Erreurs : retourner des codes HTTP adaptés (400 pour validation, 401/403 pour auth, 404 ressource non trouvée, 500 pour erreur serveur). Utiliser un handler global (`@ControllerAdvice`) pour uniformiser les réponses d'erreur.

## 6. Cas limites et points d'attention

- Données nulles / champs manquants → valider DTOs.
- Concurrence sur l'emprunt d'un même livre → transactions + verrouillage optimiste/pessimiste.
- Gestion des mots de passe → stocker uniquement des hash (BCrypt conseillé).
- Taille des réponses (pagination) pour les listes de livres.
- Sécurité des endpoints (exposer correctement ADMIN vs USER).

## 7. Comment builder / lancer (Windows PowerShell)

Build et tests (Gradle wrapper fourni) :

```powershell
# Nettoyer, compiler et exécuter les tests
.
\gradlew.bat clean build --refresh-dependencies

# Lancer l'application
.
\gradlew.bat bootRun

# ou exécuter le jar généré
.
\gradlew.bat bootJar
java -jar build\libs\<nom-du-jar>.jar
```

Remarques : sous PowerShell, utilisez `. \gradlew.bat` depuis la racine du projet. Remplacez `<nom-du-jar>` par le nom généré.

## 8. Tests et quality gates

- Commande : `. \gradlew.bat test`.
- Ajouter tests unitaires pour services et controllers (MockMVC pour controllers, mocks pour repositories).
- Intégration : tests d'intégration avec une base en mémoire (H2) et jeu de données `data.sql`.

## 9. Améliorations recommandées (prochaines étapes)

- Ajouter une documentation API (Swagger/OpenAPI).
- Utiliser Flyway/Liquibase pour les migrations DB.
- Améliorer la sécurité : JWT avec refresh tokens, gestion robuste des rôles.
- Monitoring : exposer métriques (Micrometer + Prometheus) et logs structurés.
- Ajouter tests automatisés CI (GitHub Actions) pour build et tests.
