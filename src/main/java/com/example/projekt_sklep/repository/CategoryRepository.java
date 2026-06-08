package com.example.projekt_sklep.repository;

import com.example.projekt_sklep.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}