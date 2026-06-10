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

### Funkcjonalności

| Element | Status |
|---|---|
| Pełny CRUD — Product, Category, Order | ✅ |
| Walidacja formularza produktu (`@NotBlank`, `@Size`, `@DecimalMin`, `@Min`) | ✅ |
| Walidacja pola wiek w rejestracji (`@Min(1)` + `min` w HTML) | ✅ |
| Walidacja ceny produktu (`min="0.01"` w HTML + `@DecimalMin` w modelu) | ✅ |
| Strona szczegółów produktu z tabelą specyfikacji technicznej | ✅ |
| 20 produktów w 7 kategoriach z pełnymi specyfikacjami | ✅ |
| Sortowanie po kolumnach (klikalne nagłówki, strzałki, 3-klik reset) | ✅ |
| Filtrowanie wg kategorii | ✅ |
| Filtrowanie wg daty (produkty dodane od wybranej daty) | ✅ |
| Ukrycie akcji admin/employee przed rolą USER (`sec:authorize`) | ✅ |
| Logowanie i rejestracja (Spring Security) | ✅ |
| Publiczny link do produktu (dostępny bez logowania) | ✅ |
| Lista użytkowników + zarządzanie rolami (tylko ADMIN) | ✅ |
| **Koszyk zakupowy** (sesja: dodaj, +/−, usuń, złóż zamówienie) | ✅ |
| **Odejmowanie stanu magazynowego** po złożeniu zamówienia | ✅ |
| **OrderItem** — pozycje zamówienia z ilością (zamiast ManyToMany) | ✅ |
| **USER widzi tylko swoje zamówienia** (admin widzi wszystkie) | ✅ |
| **Podgląd zamówienia** `/orders/{id}` z tabelą produktów i sumą | ✅ |
| **Edycja zamówienia** — +/− ilości, dodaj/usuń produkty z wyszukiwarką | ✅ |
| **Wyszukiwanie zamówień po kliencie** (panel admina) | ✅ |
| **Role-aware UI** — USER widzi "Sklep", ADMIN/EMPLOYEE "System zarządzania" | ✅ |

---

## TODO — co pozostało do zrobienia

### Java — brakuje całkowicie

| Co | Punkty |
|---|---|
| Usługa REST (`@RestController` dla Product/Category/Order) | 10p |
| Klient REST (`RestTemplate` wywołujący własne endpointy) | 2p |
| Ciasteczka (`HttpServletResponse.addCookie`) | 2p |
| Zapis do DB przy wylogowaniu/wygaśnięciu sesji (`HttpSessionListener`) | 2p |

### Java — do uzupełnienia

| Co | Problem |
|---|---|
| Walidacja `User` | Brak `@NotBlank`, `@Size` na polach imie/nazwisko/login + brak `@Valid` w `RegisterController` |
| Walidacja `Order` | Brak adnotacji walidacyjnych + brak `@Valid` w `saveOrder()` |
| Filtr kategorii | Powinien być posortowany od najpopularniejszej (GROUP BY + COUNT) |
| Rola EMPLOYEE | Brak dedykowanych reguł w `SecurityConfig` |
| WCAG 2.1 | Brak `lang="pl"`, `<label for>`, znaczników semantycznych |

### UI — dopracowanie

| Co | Opis |
|---|---|
| Dopracowanie UI | Spójność widoków, drobne poprawki layoutu na pozostałych stronach |

### SBD — dokumentacja

| Co | Opis |
|---|---|
| Dokumentacja | Tabela użytkowników z rolami, opis triggerów, screenshoty `audit_log` |

---

## Struktura projektu

```
src/main/java/com/example/projekt_sklep/
├── config/
│   ├── AuditSetup.java          # Uruchamia audit SQL po starcie aplikacji
│   ├── PasswordConfig.java
│   └── SecurityConfig.java
├── controller/
│   ├── CartController.java      # Koszyk zakupowy (sesja)
│   ├── CategoryController.java
│   ├── HomeController.java
│   ├── LoginController.java
│   ├── OrderController.java
│   ├── ProductController.java
│   ├── RegisterController.java
│   └── UserController.java
├── model/
│   ├── Category.java
│   ├── Order.java
│   ├── OrderItem.java           # Pozycja zamówienia (produkt + ilość)
│   ├── Product.java
│   ├── ProductSpec.java         # Specyfikacje techniczne produktu (klucz/wartość)
│   └── User.java
├── repository/
│   ├── CategoryRepository.java
│   ├── OrderItemRepository.java
│   ├── OrderRepository.java
│   ├── ProductRepository.java
│   ├── ProductSpecRepository.java
│   └── UserRepository.java
├── service/
│   └── CustomUserDetailsService.java
├── DataLoader.java              # Ładuje dane testowe przy pierwszym uruchomieniu
└── ProjektSklepApplication.java

database/
├── 01_roles.sql                 # Role bazodanowe
├── 02_users.sql                 # Użytkownicy bazodanowi
└── 03_audit.sql                 # Tabela audit_log + triggery PL/pgSQL

src/main/resources/
├── audit/
│   └── 03_audit.sql             # Kopia skryptu audytu (wykonywana przez AuditSetup.java)
└── templates/                   # Szablony Thymeleaf
```
