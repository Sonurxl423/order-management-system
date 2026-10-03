package com.ecommerce.ordermanagement.controller;

import com.ecommerce.ordermanagement.entity.Order;
import com.ecommerce.ordermanagement.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@SpringBootTest
class OrderControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private OrderRepository orderRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        orderRepository.deleteAll();

        orderRepository.save(
                new Order(null, "Gaming Laptop", 3)
        );
    }

    @Test
    void getOrderById_shouldReturnOrder() throws Exception {

        Order order = orderRepository.findAll()
                .get(0);

        mockMvc.perform(
                        get("/orders/{id}", order.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.productName").value("Gaming Laptop"))
                .andExpect(jsonPath("$.quantity").value(3));
    }

    @Test
    void getOrderById_shouldReturn404_whenOrderDoesNotExist() throws Exception {

        mockMvc.perform(
                        get("/orders/{id}", 999999L)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(
                        jsonPath("$.message")
                                .value("Order not found with id: 999999")
                );
    }

    @Test
    void getAllOrders_shouldReturnOrders() throws Exception {

        mockMvc.perform(
                        get("/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Gaming Laptop"))
                .andExpect(jsonPath("$[0].quantity").value(3));
    }

    @Test
    void createOrder_shouldCreateOrder() throws Exception {

        String request = """
            {
                "productName": "Phone",
                "quantity": 2
            }
            """;

        mockMvc.perform(
                        post("/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Phone"))
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @Test
    void updateOrder_shouldUpdateOrder() throws Exception {

        Order order = orderRepository.findAll().get(0);

        String request = """
            {
                "productName": "Updated Laptop",
                "quantity": 5
            }
            """;

        mockMvc.perform(
                        put("/orders/{id}", order.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Updated Laptop"))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void deleteOrder_shouldDeleteOrder() throws Exception {

        Order order = orderRepository.findAll().get(0);

        mockMvc.perform(
                        delete("/orders/{id}", order.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.productName").value("Gaming Laptop"));
    }

    @Test
    void createOrder_shouldReturn400_whenRequestIsInvalid() throws Exception {

        String request = """
            {
                "productName": "",
                "quantity": 0
            }
            """;

        mockMvc.perform(
                        post("/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }


}