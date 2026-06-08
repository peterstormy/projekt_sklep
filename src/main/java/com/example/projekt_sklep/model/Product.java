package com.example.projekt_sklep.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nazwa jest wymagana")
    @Size(min = 3, max = 30)
    private String nazwa;

    @NotBlank(message = "Opis jest wymagany")
    @Size(min = 5, max = 200)
    private String opis;

    @DecimalMin(value = "0.01")
    private Double cena;

    @Min(0)
    private Integer ilosc;

    private LocalDate dataDodania;

    @ManyToOne
    private Category category;

    private boolean publiczny;

}