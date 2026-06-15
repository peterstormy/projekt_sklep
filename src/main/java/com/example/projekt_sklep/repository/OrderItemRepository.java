package com.example.projekt_sklep.repository;

import com.example.projekt_sklep.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByOrderIdAndProductId(Long orderId, Long productId);
}
