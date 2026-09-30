package com.ecommerce.ordermanagement.service;

import com.ecommerce.ordermanagement.dto.OrderRequest;
import com.ecommerce.ordermanagement.dto.OrderResponse;
import com.ecommerce.ordermanagement.entity.Order;
import com.ecommerce.ordermanagement.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final List<Order> orders = new ArrayList<>();

    private Long nextId = 1L;

    public OrderResponse createOrder(OrderRequest request) {

        Order order = new Order();

        order.setId(nextId++);
        order.setProductName(request.getProductName());
        order.setQuantity(request.getQuantity());

        orders.add(order);

        return mapToResponse(order);
    }

    public List<OrderResponse> getAllOrders() {

        return orders.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OrderResponse getOrderById(Long id) {

        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .map(this::mapToResponse)
                .findFirst()
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found with id: " + id)
                );
    }

    private OrderResponse mapToResponse(Order order) {

        return new OrderResponse(
                order.getId(),
                order.getProductName(),
                order.getQuantity()
        );
    }

    public OrderResponse deleteOrderById(Long id) {

        Order order = orders.stream()
                .filter(orderItem -> orderItem.getId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with id: " + id
                        )
                );

        OrderResponse response = mapToResponse(order);

        orders.remove(order);

        return response;
    }
}