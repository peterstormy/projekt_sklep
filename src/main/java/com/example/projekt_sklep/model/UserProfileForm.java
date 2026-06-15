package com.example.projekt_sklep.model;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileForm {

    @NotBlank(message = "Imię jest wymagane")
    @Size(min = 3, max = 20, message = "Imię musi mieć od 3 do 20 znaków")
    @Pattern(regexp = "\\p{Lu}\\p{L}+", message = "Imię musi zaczynać się wielką literą i zawierać tylko litery")
    private String imie;

    @NotBlank(message = "Nazwisko jest wymagane")
    @Size(min = 3, max = 50, message = "Nazwisko musi mieć od 3 do 50 znaków")
    @Pattern(regexp = "\\p{Lu}\\p{L}+", message = "Nazwisko musi zaczynać się wielką literą i zawierać tylko litery")
    private String nazwisko;

    @NotNull(message = "Wiek jest wymagany")
    @Min(value = 18, message = "Musisz mieć co najmniej 18 lat")
    private Integer wiek;
}
