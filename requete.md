## 🧩 1️⃣ AuthController → `/api/auth`

### 🔹 Register Member

**POST** → `http://localhost:8080/api/auth/register`

**Body (JSON)** :

<pre class="overflow-visible!" data-start="367" data-end="460"><div class="contain-inline-size rounded-2xl relative bg-token-sidebar-surface-primary"><div class="sticky top-9"><div class="absolute end-0 bottom-0 flex h-9 items-center pe-2"><div class="bg-token-bg-elevated-secondary text-token-text-secondary flex items-center gap-4 rounded-sm px-2 font-sans text-xs"></div></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="whitespace-pre! language-json"><span><span>{</span><span>
  </span><span>"username"</span><span>:</span><span></span><span>"john_doe"</span><span>,</span><span>
  </span><span>"password"</span><span>:</span><span></span><span>"secret123"</span><span>,</span><span>
  </span><span>"fullName"</span><span>:</span><span></span><span>"John Doe"</span><span>
</span><span>}</span><span>
</span></span></code></div></div></pre>

---

### 🔹 Login

**POST** → `http://localhost:8080/api/auth/login`

**Body (JSON)** :

<pre class="overflow-visible!" data-start="550" data-end="617"><div class="contain-inline-size rounded-2xl relative bg-token-sidebar-surface-primary"><div class="sticky top-9"><div class="absolute end-0 bottom-0 flex h-9 items-center pe-2"><div class="bg-token-bg-elevated-secondary text-token-text-secondary flex items-center gap-4 rounded-sm px-2 font-sans text-xs"></div></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="whitespace-pre! language-json"><span><span>{</span><span>
  </span><span>"username"</span><span>:</span><span></span><span>"john_doe"</span><span>,</span><span>
  </span><span>"password"</span><span>:</span><span></span><span>"secret123"</span><span>
</span><span>}</span><span>
</span></span></code></div></div></pre>

---

### 🔹 Change Password

**POST** → `http://localhost:8080/api/auth/change-password`

**Body (JSON)** :

<pre class="overflow-visible!" data-start="727" data-end="830"><div class="contain-inline-size rounded-2xl relative bg-token-sidebar-surface-primary"><div class="sticky top-9"><div class="absolute end-0 bottom-0 flex h-9 items-center pe-2"><div class="bg-token-bg-elevated-secondary text-token-text-secondary flex items-center gap-4 rounded-sm px-2 font-sans text-xs"></div></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="whitespace-pre! language-json"><span><span>{</span><span>
  </span><span>"username"</span><span>:</span><span></span><span>"john_doe"</span><span>,</span><span>
  </span><span>"oldPassword"</span><span>:</span><span></span><span>"secret123"</span><span>,</span><span>
  </span><span>"newPassword"</span><span>:</span><span></span><span>"newSecret456"</span><span>
</span><span>}</span><span>
</span></span></code></div></div></pre>

---

### 🔹 Get User Info

**GET** → `http://localhost:8080/api/auth/user/john_doe`

---

### 🔹 Create Admin

**POST** → `http://localhost:8080/api/auth/admin/create`

**Body (JSON)** :

<pre class="overflow-visible!" data-start="1018" data-end="1112"><div class="contain-inline-size rounded-2xl relative bg-token-sidebar-surface-primary"><div class="sticky top-9"><div class="absolute end-0 bottom-0 flex h-9 items-center pe-2"><div class="bg-token-bg-elevated-secondary text-token-text-secondary flex items-center gap-4 rounded-sm px-2 font-sans text-xs"></div></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="whitespace-pre! language-json"><span><span>{</span><span>
  </span><span>"username"</span><span>:</span><span></span><span>"admin1"</span><span>,</span><span>
  </span><span>"password"</span><span>:</span><span></span><span>"adminPass"</span><span>,</span><span>
  </span><span>"fullName"</span><span>:</span><span></span><span>"Super Admin"</span><span>
</span><span>}</span><span>
</span></span></code></div></div></pre>

---

### 🔹 Update User Profile

**PUT** → `http://localhost:8080/api/auth/user/update`

**Body (JSON)** :

<pre class="overflow-visible!" data-start="1221" data-end="1292"><div class="contain-inline-size rounded-2xl relative bg-token-sidebar-surface-primary"><div class="sticky top-9"><div class="absolute end-0 bottom-0 flex h-9 items-center pe-2"><div class="bg-token-bg-elevated-secondary text-token-text-secondary flex items-center gap-4 rounded-sm px-2 font-sans text-xs"></div></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="whitespace-pre! language-json"><span><span>{</span><span>
  </span><span>"username"</span><span>:</span><span></span><span>"john_doe"</span><span>,</span><span>
  </span><span>"fullName"</span><span>:</span><span></span><span>"Johnathan Doe"</span><span>
</span><span>}</span><span>
</span></span></code></div></div></pre>

---

## 🧩 2️⃣ AdminController → `/api/admin`

### 🔹 Create Librarian

**POST** → `http://localhost:8080/api/admin/create-librarian`

**Params** :

| Key      | Value           |
| -------- | --------------- |
| username | librarian1      |
| password | libpass123      |
| fullName | Alice Librarian |

---

### 🔹 Get All Users

**GET** → `http://localhost:8080/api/admin/users`

---

### 🔹 Promote User

**POST** → `http://localhost:8080/api/admin/promote`

**Params** :

| Key      | Value          |
| -------- | -------------- |
| username | bob            |
| newRole  | ROLE_LIBRARIAN |

*(autres rôles possibles : `ROLE_ADMIN`, `ROLE_MEMBER`)*

---

### 🔹 Delete User

**DELETE** → `http://localhost:8080/api/admin/users/john_doe`

---

## 🧩 3️⃣ BookController → `/api/books`

### 🔹 Add Book

**POST** → `http://localhost:8080/api/books`

**Headers** :

| Key          | Value            |
| ------------ | ---------------- |
| Content-Type | application/json |
| X-User       | librarian1       |

**Body (JSON)** :

<pre class="overflow-visible!" data-start="2199" data-end="2306"><div class="contain-inline-size rounded-2xl relative bg-token-sidebar-surface-primary"><div class="sticky top-9"><div class="absolute end-0 bottom-0 flex h-9 items-center pe-2"><div class="bg-token-bg-elevated-secondary text-token-text-secondary flex items-center gap-4 rounded-sm px-2 font-sans text-xs"></div></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="whitespace-pre! language-json"><span><span>{</span><span>
  </span><span>"title"</span><span>:</span><span></span><span>"1984"</span><span>,</span><span>
  </span><span>"author"</span><span>:</span><span></span><span>"George Orwell"</span><span>,</span><span>
  </span><span>"genre"</span><span>:</span><span></span><span>"Dystopian"</span><span>,</span><span>
  </span><span>"available"</span><span>:</span><span></span><span>true</span><span>
</span><span>}</span><span>
</span></span></code></div></div></pre>

---

### 🔹 Search Books

**GET** → `http://localhost:8080/api/books/search?title=1984`

*(ou `?author=Orwell` ou `?genre=Dystopian`)*

---

### 🔹 Get Book by ID

**GET** → `http://localhost:8080/api/books/1`

---

### 🔹 Update Book

**PUT** → `http://localhost:8080/api/books/1`

**Body (JSON)** :

<pre class="overflow-visible!" data-start="2608" data-end="2742"><div class="contain-inline-size rounded-2xl relative bg-token-sidebar-surface-primary"><div class="sticky top-9"><div class="absolute end-0 bottom-0 flex h-9 items-center pe-2"><div class="bg-token-bg-elevated-secondary text-token-text-secondary flex items-center gap-4 rounded-sm px-2 font-sans text-xs"></div></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="whitespace-pre! language-json"><span><span>{</span><span>
  </span><span>"title"</span><span>:</span><span></span><span>"1984 (Updated Edition)"</span><span>,</span><span>
  </span><span>"author"</span><span>:</span><span></span><span>"George Orwell"</span><span>,</span><span>
  </span><span>"genre"</span><span>:</span><span></span><span>"Political Fiction"</span><span>,</span><span>
  </span><span>"available"</span><span>:</span><span></span><span>false</span><span>
</span><span>}</span><span>
</span></span></code></div></div></pre>

---

### 🔹 Delete Book

**DELETE** → `http://localhost:8080/api/books/1`

---

## 🧩 4️⃣ LoanController → `/api/loans`

### 🔹 Request Loan

**POST** → `http://localhost:8080/api/loans/request`

**Params** :

| Key      | Value    |
| -------- | -------- |
| username | john_doe |
| bookId   | 1        |

---

### 🔹 Approve Loan

**POST** → `http://localhost:8080/api/loans/approve`

**Params** :

| Key      | Value      |
| -------- | ---------- |
| loanId   | 1          |
| approver | librarian1 |

---

### 🔹 Return Book

**POST** → `http://localhost:8080/api/loans/return`

**Params** :

| Key      | Value    |
| -------- | -------- |
| loanId   | 1        |
| username | john_doe |

---

### 🔹 Get Loans of a User

**GET** → `http://localhost:8080/api/loans/my?username=john_doe`

---

### 🔹 Get Pending Loans

**GET** → `http://localhost:8080/api/loans/pending`

---

### 🔹 Get All Loans

**GET** → `http://localhost:8080/api/loans/all`

---

## 🧾 Résumé pour Postman

| Controller | Méthode | URL                         | Description             |
| ---------- | -------- | --------------------------- | ----------------------- |
| Auth       | POST     | /api/auth/register          | Créer un membre        |
| Auth       | POST     | /api/auth/login             | Connexion               |
| Auth       | POST     | /api/auth/change-password   | Changer le mot de passe |
| Auth       | GET      | /api/auth/user/{username}   | Infos utilisateur       |
| Auth       | POST     | /api/auth/admin/create      | Créer un admin         |
| Auth       | PUT      | /api/auth/user/update       | Modifier profil         |
| Admin      | POST     | /api/admin/create-librarian | Créer bibliothécaire  |
| Admin      | GET      | /api/admin/users            | Lister utilisateurs     |
| Admin      | POST     | /api/admin/promote          | Promouvoir utilisateur  |
| Admin      | DELETE   | /api/admin/users/{username} | Supprimer utilisateur   |
| Books      | POST     | /api/books                  | Ajouter un livre        |
| Books      | GET      | /api/books/search           | Rechercher livre        |
| Books      | GET      | /api/books/{id}             | Obtenir livre par ID    |
| Books      | PUT      | /api/books/{id}             | Modifier livre          |
| Books      | DELETE   | /api/books/{id}             | Supprimer livre         |
| Loans      | POST     | /api/loans/request          | Demander prêt          |
| Loans      | POST     | /api/loans/approve          | Approuver prêt         |
| Loans      | POST     | /api/loans/return           | Retourner livre         |
| Loans      | GET      | /api/loans/my               | Mes prêts              |
| Loans      | GET      | /api/loans/pending          | Prêts en attente       |
| Loans      | GET      | /api/loans/all              | Tous les prêts         |
