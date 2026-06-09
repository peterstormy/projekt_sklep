package com.example.projekt_sklep.repository;

import com.example.projekt_sklep.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long id);

    List<Product> findByDataDodaniaGreaterThanEqual(
            LocalDate dataDodania
    );

}