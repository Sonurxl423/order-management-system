package com.ecommerce.ordermanagement.service;

import com.ecommerce.ordermanagement.dto.OrderRequest;
import com.ecommerce.ordermanagement.dto.OrderResponse;
import com.ecommerce.ordermanagement.dto.OrderUpdateRequest;
import com.ecommerce.ordermanagement.entity.Order;
import com.ecommerce.ordermanagement.exception.OrderNotFoundException;
import com.ecommerce.ordermanagement.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        Order order = new Order();

        order.setProductName(request.getProductName());
        order.setQuantity(request.getQuantity());

        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with id: " + id
                        )
                );

        return mapToResponse(order);
    }

    @Transactional
    public OrderResponse updateOrder(
            Long id,
            OrderUpdateRequest request) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with id: " + id
                        )
                );

        order.setProductName(request.getProductName());
        order.setQuantity(request.getQuantity());

        return mapToResponse(order);
    }

    @Transactional
    public OrderResponse deleteOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with id: " + id
                        )
                );

        OrderResponse response = mapToResponse(order);

        orderRepository.delete(order);

        return response;
    }

    private OrderResponse mapToResponse(Order order) {

        return new OrderResponse(
                order.getId(),
                order.getProductName(),
                order.getQuantity()
        );
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByProductName(String productName) {

        return orderRepository.findByProductName(productName)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByMinimumQuantity(Integer quantity) {

        return orderRepository.findOrdersByMinimumQuantity(quantity)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrders(Pageable pageable) {

        return orderRepository.findAll(pageable)
                .map(this::mapToResponse);
    }
}