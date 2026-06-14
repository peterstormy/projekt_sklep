package com.example.projekt_sklep.repository;

import com.example.projekt_sklep.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT c.* FROM category c LEFT JOIN product p ON p.category_id = c.id GROUP BY c.id ORDER BY COUNT(p) DESC", nativeQuery = true)
    List<Category> findAllOrderByProductCount();
}
