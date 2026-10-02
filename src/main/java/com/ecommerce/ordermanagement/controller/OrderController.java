package com.ecommerce.ordermanagement.controller;

import com.ecommerce.ordermanagement.dto.OrderRequest;
import com.ecommerce.ordermanagement.dto.OrderResponse;
import com.ecommerce.ordermanagement.dto.OrderUpdateRequest;
import com.ecommerce.ordermanagement.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse createOrder(@Valid @RequestBody OrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @DeleteMapping("/{id}")
    public OrderResponse deleteOrderById(@PathVariable Long id){
        return orderService.deleteOrderById(id);
    }

    @PutMapping("/{id}")
    public OrderResponse updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderUpdateRequest request) {

        return orderService.updateOrder(id, request);
    }

    @GetMapping("/search")
    public List<OrderResponse> getOrdersByProductName(
            @RequestParam String productName) {

        return orderService.getOrdersByProductName(productName);
    }

    @GetMapping("/search/quantity")
    public List<OrderResponse> getOrdersByMinimumQuantity(
            @RequestParam Integer quantity) {

        return orderService.getOrdersByMinimumQuantity(quantity);
    }

    @GetMapping("/page")
    public Page<OrderResponse> getOrders(Pageable pageable) {
        return orderService.getOrders(pageable);
    }
}