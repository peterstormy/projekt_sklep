# Projekt Sklep — Spring Boot

Aplikacja webowa sklepu komputerowego zrealizowana w Spring Boot + Thymeleaf + Spring Security.
Projekt łączy wymagania z **Programowania aplikacji WWW w języku Java** oraz **Systemów Baz Danych**.

---

## Uruchomienie

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

Aplikacja dostępna pod: `http://localhost:8080`

### Konta testowe

| Login | Hasło | Rola |
|---|---|---|
| `admin` | `admin` | ADMIN |
| `user` | `user` | USER |
| `employee` | `employee` | EMPLOYEE |

---

## TODO — co pozostało do zrobienia

### Java — brakuje całkowicie

| Co | Punkty | Priorytet |
|---|---|---|
| Usługa REST (`@RestController` dla Product/Category/Order) | 10p | KRYTYCZNY |
| Klient REST (`RestTemplate` wywołujący własne endpointy REST) | 2p | Wysoki |
| Ciasteczka (`HttpServletResponse.addCookie`) | 2p | Wysoki |
| Zapis do DB dopiero przy wylogowaniu/wygaśnięciu sesji (`HttpSessionListener`) | 2p | Wysoki |

### Java — do naprawienia/uzupełnienia

| Co | Problem |
|---|---|
| Walidacja `User` | Brak `@NotBlank`, `@Size`, `@Min` na polach + brak `@Valid` w `RegisterController.saveUser()` |
| Walidacja `Order` | Brak adnotacji walidacyjnych na klasie + brak `@Valid` w `saveOrder()` |
| Filtr daty | Szuka dokładnej daty zamiast `>=` — powinno być `findByDataDodaniaGreaterThanEqual()` |
| Filtr kategorii | Powinien być posortowany od najpopularniejszej (zapytanie `GROUP BY category + COUNT`) |
| WCAG 2.1 | Brak `lang="pl"` w `<html>`, brak `<label for>` przy inputach, użycie `<center>`, brak `<main>`/`<nav>`/`<header>` |
| Rola EMPLOYEE | Istnieje w danych ale brak reguł w `SecurityConfig` — rola nic nie robi |
| `pom.xml` | Błędne nazwy zależności (`spring-boot-starter-webmvc`, `spring-boot-starter-data-jpa-test` itp.) |
| Sesja | Używana tylko do sortowania — warto rozszerzyć żeby uzasadnić 3p |

### SBD — brakuje całkowicie

| Co | Opis |
|---|---|
| Zmiana H2 → PostgreSQL | H2 in-memory nie wspiera zarządzania użytkownikami DB ani audytu |
| Konta użytkowników DB | `Administrator` (owner), `ApplicationIdentity` (READ/WRITE/EXECUTE), konta deweloperskie (READ) |
| Rola `db_procexecutor` | Rola z uprawnieniem EXECUTE do procedur składowanych |
| Audyt bazy | Logowanie operacji DML (INSERT, UPDATE, DELETE) — przez `pgaudit` lub triggery |
| Dokumentacja SBD | Tabela użytkowników z rolami, tabela ról z opisem, screenshoty logów audytu |

---

## Co już działa

### Elementy techniczne

| Element | Status |
|---|---|
| Kontrolery MVC (7 kontrolerów) | ✅ |
| Baza danych — 4 tabele z relacjami JPA | ✅ |
| Widoki Thymeleaf (8+ znaczników, formularze z walidacją) | ✅ |
| Spring Security z bazą danych, 3 role | ✅ |
| Sesja — zapamiętanie kryterium i kierunku sortowania | ✅ (częściowo) |

### Funkcjonalności

| Element | Status |
|---|---|
| Pełny CRUD — Product, Category, Order | ✅ |
| Walidacja formularza — 6 reguł na Product | ✅ |
| Edycja na danych bieżących | ✅ |
| Sortowanie wg 3 kryteriów w obu kierunkach | ✅ |
| Zapamiętanie kryterium i kierunku sortowania | ✅ |
| Filtrowanie wg daty i kategorii | ✅ (częściowo) |
| Logowanie (Spring Security) | ✅ |
| Rejestracja bez logowania | ✅ |
| Strona powitalna | ✅ |
| Publiczny link do produktu (niezalogowany) | ✅ |
| Lista użytkowników (tylko ADMIN) | ✅ |
| Zarządzanie rolami (tylko ADMIN) | ✅ |

---

## Struktura projektu

```
src/main/java/com/example/projekt_sklep/
├── config/
│   ├── PasswordConfig.java
│   └── SecurityConfig.java
├── controller/
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
│   ├── Product.java
│   └── User.java
├── repository/
│   ├── CategoryRepository.java
│   ├── OrderRepository.java
│   ├── ProductRepository.java
│   └── UserRepository.java
├── service/
│   └── CustomUserDetailsService.java
├── DataLoader.java
└── ProjektSklepApplication.java
```
