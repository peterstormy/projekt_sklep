# Projekt Sklep — Spring Boot

Aplikacja webowa sklepu komputerowego zrealizowana w Spring Boot + Thymeleaf + Spring Security + PostgreSQL.
Projekt łączy wymagania z **Programowania aplikacji WWW w języku Java** oraz **Systemów Baz Danych**.

---

## Uruchomienie

### 1. Baza danych (Docker)

Przed uruchomieniem aplikacji należy wystartować bazę PostgreSQL:

```bash
docker compose up -d
```

> Docker musi być uruchomiony. Baza działa na `localhost:5432`, baza danych: `sklep`.

### 2. Aplikacja

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

Aplikacja dostępna pod: `http://localhost:8080`

> Przy pierwszym uruchomieniu DataLoader automatycznie załaduje 7 kategorii, 20 produktów ze specyfikacjami oraz 3 konta testowe.

### Konta testowe

| Login | Hasło | Rola |
|---|---|---|
| `admin` | `admin` | ADMIN |
| `user` | `user` | USER |
| `employee` | `employee` | EMPLOYEE |

### Konta bazodanowe

| Login | Hasło | Uprawnienia |
|---|---|---|
| `app_identity` | `App_2026!` | Konto aplikacji (READ/WRITE/EXECUTE) |
| `dev_student` | `Dev_2026!` | Konto deweloperskie (tylko READ) |
| `admin_db` | `Admin_2026!` | Administrator bazy danych |

---

## Co już działa

### Infrastruktura

| Element | Status |
|---|---|
| PostgreSQL 16 w Docker | ✅ |
| Role DB: `app_role`, `dev_role`, `db_procexecutor` | ✅ |
| Użytkownicy DB z odpowiednimi uprawnieniami | ✅ |
| Audyt DML (INSERT/UPDATE/DELETE) via triggery PL/pgSQL + tabela `audit_log` (JSONB) | ✅ |

### Elementy techniczne (Java)

| Element | Status |
|---|---|
| Kontrolery MVC (9 kontrolerów) | ✅ |
| Modele JPA: User, Category, Product, ProductSpec, Order, OrderItem | ✅ |
| Widoki Thymeleaf z formularzami i walidacją | ✅ |
| Spring Security z bazą danych, 3 role | ✅ |
| Sesja — koszyk zakupowy przechowywany w `HttpSession` | ✅ |
| `HttpSessionListener` — zapis czasu wylogowania/wygaśnięcia sesji do bazy | ✅ |
| `UserProfileForm` — osobny POJO dla edycji profilu (omija `@NotBlank` na haśle) | ✅ |

### Walidacja Bean Validation

| Pole / Model | Adnotacje | Status |
|---|---|---|
| `User.imie` / `nazwisko` | `@NotBlank`, `@Size(3–20/50)`, `@Pattern` (wielka litera, tylko litery) | ✅ |
| `User.login` | `@NotBlank`, `@Size(3–20)`, `@Pattern` (małe litery i cyfry) | ✅ |
| `User.haslo` | `@NotBlank`, `@Size(min=5)` | ✅ |
| `User.wiek` | `@NotNull`, `@Min(18)` | ✅ |
| `Category.nazwa` | `@NotBlank`, `@Size(3–20)`, `@Pattern` (małe litery, spacje) | ✅ |
| `Category.opis` | `@Size(max=500)` | ✅ |
| `Product.nazwa` / `producent` | `@NotBlank`, `@Size` | ✅ |
| `Product.opis` | `@Size(max=500)` | ✅ |
| `Product.cena` | `@NotNull`, `@DecimalMin("0.01")` | ✅ |
| `Product.ilosc` | `@NotNull`, `@Min(0)` | ✅ |
| Duplikat loginu przy rejestracji | `result.rejectValue(...)` w `RegisterController` | ✅ |

### Funkcjonalności

| Element | Status |
|---|---|
| Pełny CRUD — Product, Category, Order | ✅ |
| Strona szczegółów produktu z tabelą specyfikacji technicznej | ✅ |
| Strona szczegółów kategorii z listą produktów | ✅ |
| 20 produktów w 7 kategoriach z pełnymi specyfikacjami | ✅ |
| Sortowanie po kolumnach (klikalne nagłówki, strzałki, 3-klik reset) | ✅ |
| Filtrowanie wg kategorii (ciasteczko zapamiętuje ostatnio przeglądaną) | ✅ |
| Filtrowanie wg daty (produkty dodane od wybranej daty) | ✅ |
| Sortowanie kategorii wg liczby produktów (native query) | ✅ |
| Ukrycie akcji admin/employee przed rolą USER (`sec:authorize`) | ✅ |
| Logowanie i rejestracja (Spring Security) | ✅ |
| Publiczny link do produktu (dostępny bez logowania) | ✅ |
| Lista użytkowników + zarządzanie rolami (tylko ADMIN) | ✅ |
| Usuwanie użytkownika przez ADMIN (z nullowaniem powiązanych zamówień) | ✅ |
| **Profil użytkownika** — podgląd, edycja (imię, nazwisko, wiek), usuwanie konta | ✅ |
| **Ostatnie wylogowanie** — zapisywane w bazie, widoczne na profilu | ✅ |
| **Koszyk zakupowy** (sesja: dodaj, +/−, usuń, złóż zamówienie) | ✅ |
| **Odejmowanie stanu magazynowego** po złożeniu zamówienia | ✅ |
| **OrderItem** — pozycje zamówienia z ilością (zamiast ManyToMany) | ✅ |
| **USER widzi tylko swoje zamówienia** (admin widzi wszystkie) | ✅ |
| **Podgląd zamówienia** `/orders/{id}` z tabelą produktów i sumą | ✅ |
| **Edycja zamówienia** — +/− ilości, dodaj/usuń produkty z wyszukiwarką | ✅ |
| **Wyszukiwanie zamówień po kliencie** (panel admina) | ✅ |
| **Role-aware UI** — USER widzi "Sklep", ADMIN/EMPLOYEE "System zarządzania" | ✅ |
| **WCAG 2.1** — `lang="pl"`, `<label for>`, `scope="col"`, `<main>`, `role="alert"` na wszystkich 20 szablonach | ✅ |

---

## TODO — co pozostało

| Co | Opis |
|---|---|
| Dokumentacja SBD | Tabela użytkowników z rolami, opis triggerów, screenshoty `audit_log` |

---

## Struktura projektu

```
src/main/java/com/example/projekt_sklep/
├── config/
│   ├── AuditSetup.java          # Uruchamia audit SQL po starcie aplikacji
│   ├── PasswordConfig.java
│   ├── SecurityConfig.java
│   ├── SessionListener.java     # HttpSessionListener — zapis ostatnieLogowanie do DB
│   └── WebConfig.java           # Rejestracja SessionListener jako bean
├── controller/
│   ├── CartController.java      # Koszyk zakupowy (sesja)
│   ├── CategoryController.java
│   ├── HomeController.java
│   ├── LoginController.java
│   ├── OrderController.java
│   ├── ProductController.java
│   ├── RegisterController.java
│   └── UserController.java      # Profil, edycja, usuwanie konta, zarządzanie użytkownikami
├── model/
│   ├── Category.java            # +opis, dataDodania, aktywna
│   ├── Order.java
│   ├── OrderItem.java           # Pozycja zamówienia (produkt + ilość)
│   ├── Product.java
│   ├── ProductSpec.java         # Specyfikacje techniczne produktu (klucz/wartość)
│   ├── User.java                # +ostatnieLogowanie, pełna walidacja
│   └── UserProfileForm.java     # POJO do edycji profilu (bez @Entity)
├── repository/
│   ├── CategoryRepository.java  # +findAllOrderByProductCount (native query)
│   ├── OrderItemRepository.java
│   ├── OrderRepository.java
│   ├── ProductRepository.java
│   ├── ProductSpecRepository.java
│   └── UserRepository.java
├── service/
│   └── CustomUserDetailsService.java
├── DataLoader.java              # Ładuje dane testowe + migrateCategories()
└── ProjektSklepApplication.java

database/
├── 01_roles.sql                 # Role bazodanowe
├── 02_users.sql                 # Użytkownicy bazodanowi
└── 03_audit.sql                 # Tabela audit_log + triggery PL/pgSQL

src/main/resources/
├── audit/
│   └── 03_audit.sql             # Kopia skryptu audytu (wykonywana przez AuditSetup.java)
└── templates/                   # 20 szablonów Thymeleaf (wszystkie zgodne z WCAG 2.1)
    ├── fragments/sidebar.html
    ├── addCategory.html / editCategory.html / categoryDetail.html
    ├── addProduct.html / editProduct.html / productDetail.html / publicProduct.html
    ├── addOrder.html / editOrder.html / orderDetail.html
    ├── cart.html
    ├── profile.html / editProfile.html
    ├── register.html / login.html
    ├── products.html / categories.html / orders.html / users.html
    └── index.html
```
