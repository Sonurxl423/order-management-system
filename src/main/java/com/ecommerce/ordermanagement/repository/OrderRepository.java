package com.ecommerce.ordermanagement.repository;

import com.ecommerce.ordermanagement.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByProductName(String productName);

    @Query("SELECT o FROM Order o WHERE o.quantity >= :quantity")
    List<Order> findOrdersByMinimumQuantity(@Param("quantity") Integer quantity);

    Page<Order> findAll(Pageable pageable);
}