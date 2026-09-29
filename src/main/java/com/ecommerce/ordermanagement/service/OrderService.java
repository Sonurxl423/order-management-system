package com.ecommerce.ordermanagement.service;

import com.ecommerce.ordermanagement.entity.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final List<Order> orders = new ArrayList<>();

    private Long nextId = 1L;

    public Order createOrder(Order order) {

        order.setId(nextId++);

        orders.add(order);

        return order;
    }

    public List<Order> getAllOrders() {
        return orders;
    }

    public Order getOrderById(Long id) {

        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}