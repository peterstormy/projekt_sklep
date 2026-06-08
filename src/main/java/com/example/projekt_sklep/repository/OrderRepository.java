package com.example.projekt_sklep.repository;

import com.example.projekt_sklep.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}