package com.ecommerce.ordermanagement.service;

import com.ecommerce.ordermanagement.dto.OrderUpdateRequest;
import com.ecommerce.ordermanagement.exception.OrderNotFoundException;
import com.ecommerce.ordermanagement.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ecommerce.ordermanagement.dto.OrderResponse;
import com.ecommerce.ordermanagement.entity.Order;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.ecommerce.ordermanagement.specification.OrderSpecification;
import com.ecommerce.ordermanagement.dto.OrderRequest;
import static org.mockito.ArgumentMatchers.any;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        orderService = new OrderService(orderRepository);
    }

    @Test
    void getOrderById_shouldReturnOrder_whenOrderExists() {

        Order order = new Order(1L, "Laptop", 2);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        OrderResponse response = orderService.getOrderById(1L);

        assertEquals(1L, response.getId());
        assertEquals("Laptop", response.getProductName());
        assertEquals(2, response.getQuantity());

        verify(orderRepository).findById(1L);
    }


    @Test
    void getOrderById_shouldThrowException_whenOrderDoesNotExist() {

        when(orderRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                OrderNotFoundException.class,
                () -> orderService.getOrderById(999L)
        );
    }

    @Test
    void createOrder_shouldSaveAndReturnOrder() {

        OrderRequest request = new OrderRequest();
        request.setProductName("Laptop");
        request.setQuantity(2);

        Order savedOrder = new Order(1L, "Laptop", 2);

        when(orderRepository.save(any(Order.class)))
                .thenReturn(savedOrder);

        OrderResponse response = orderService.createOrder(request);

        assertEquals(1L, response.getId());
        assertEquals("Laptop", response.getProductName());
        assertEquals(2, response.getQuantity());

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void deleteOrderById_shouldDeleteAndReturnOrder() {

        Order order = new Order(1L, "Laptop", 2);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        OrderResponse response = orderService.deleteOrderById(1L);

        assertEquals(1L, response.getId());
        assertEquals("Laptop", response.getProductName());
        assertEquals(2, response.getQuantity());

        verify(orderRepository).findById(1L);
        verify(orderRepository).delete(order);
    }

    @Test
    void updateOrder_shouldUpdateAndReturnOrder() {

        Order order = new Order(1L, "Laptop", 2);

        OrderUpdateRequest request = new OrderUpdateRequest();
        request.setProductName("Gaming Laptop");
        request.setQuantity(3);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        OrderResponse response = orderService.updateOrder(1L, request);

        assertEquals(1L, response.getId());
        assertEquals("Gaming Laptop", response.getProductName());
        assertEquals(3, response.getQuantity());

        verify(orderRepository).findById(1L);
    }

    @Test
    void searchByProductName_shouldReturnMatchingOrders() {

        Order order = new Order(1L, "Gaming Laptop", 3);

        when(orderRepository.findAll(
                any(org.springframework.data.jpa.domain.Specification.class)
        )).thenReturn(List.of(order));

        List<OrderResponse> response =
                orderService.searchByProductName("Gaming Laptop");

        assertEquals(1, response.size());
        assertEquals("Gaming Laptop", response.get(0).getProductName());
        assertEquals(3, response.get(0).getQuantity());
    }

    @Test
    void getAllOrders_shouldReturnAllOrders() {

        List<Order> orders = List.of(
                new Order(1L, "Laptop", 2),
                new Order(2L, "Phone", 1)
        );

        when(orderRepository.findAll()).thenReturn(orders);

        List<OrderResponse> response = orderService.getAllOrders();

        assertEquals(2, response.size());
        assertEquals("Laptop", response.get(0).getProductName());
        assertEquals("Phone", response.get(1).getProductName());

        verify(orderRepository).findAll();
    }
}