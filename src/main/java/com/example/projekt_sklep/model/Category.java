package com.example.projekt_sklep.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nazwa jest wymagana")
    @Size(min = 3, max = 20, message = "Nazwa musi mieć od 3 do 20 znaków")
    @Pattern(regexp = "[a-ząćęłńóśźż ]+", message = "Nazwa może zawierać tylko małe litery")
    private String nazwa;

    @Size(max = 500, message = "Opis nie może przekraczać 500 znaków")
    private String opis;

    private LocalDate dataDodania;

    private boolean aktywna = true;
}
