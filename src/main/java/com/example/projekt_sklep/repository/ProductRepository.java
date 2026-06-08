package com.example.projekt_sklep.repository;

import com.example.projekt_sklep.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}