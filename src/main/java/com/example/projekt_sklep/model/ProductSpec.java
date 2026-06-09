package com.example.projekt_sklep.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProductSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String klucz;

    @Column(length = 500)
    private String wartosc;

    private int kolejnosc;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
