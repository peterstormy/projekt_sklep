package com.example.projekt_sklep.repository;

import com.example.projekt_sklep.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long id);

    List<Product> findByDataDodaniaGreaterThanEqual(LocalDate dataDodania);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.ilosc = CASE WHEN p.ilosc > :qty THEN p.ilosc - :qty ELSE 0 END WHERE p.id = :id")
    void decrementStock(@Param("id") Long id, @Param("qty") int qty);

}