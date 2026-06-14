package com.example.projekt_sklep.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Imię jest wymagane")
    @Size(min = 3, max = 20, message = "Imię musi mieć od 3 do 20 znaków")
    @Pattern(regexp = "\\p{Lu}\\p{L}+", message = "Imię musi zaczynać się wielką literą i zawierać tylko litery")
    private String imie;

    @NotBlank(message = "Nazwisko jest wymagane")
    @Size(min = 3, max = 50, message = "Nazwisko musi mieć od 3 do 50 znaków")
    @Pattern(regexp = "\\p{Lu}\\p{L}+", message = "Nazwisko musi zaczynać się wielką literą i zawierać tylko litery")
    private String nazwisko;

    @NotBlank(message = "Login jest wymagany")
    @Size(min = 3, max = 20, message = "Login musi mieć od 3 do 20 znaków")
    @Pattern(regexp = "[a-z0-9]+", message = "Login może zawierać tylko małe litery i cyfry")
    private String login;

    @NotBlank(message = "Hasło jest wymagane")
    @Size(min = 5, message = "Hasło musi mieć co najmniej 5 znaków")
    private String haslo;

    @NotNull(message = "Wiek jest wymagany")
    @Min(value = 18, message = "Musisz mieć co najmniej 18 lat")
    private Integer wiek;

    private String rola;

    private LocalDateTime ostatnieLogowanie;
}
