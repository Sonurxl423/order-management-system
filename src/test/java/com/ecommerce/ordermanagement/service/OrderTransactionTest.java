package com.ecommerce.ordermanagement.service;

import com.ecommerce.ordermanagement.entity.Order;
import com.ecommerce.ordermanagement.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.transaction.TestTransaction;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class OrderTransactionTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void transaction_shouldRollbackDatabaseChanges() {

        long countBefore = orderRepository.count();

        orderRepository.save(
                new Order(null, "Rollback Laptop", 2)
        );

        long countAfterSave = orderRepository.count();

        assertEquals(countBefore + 1, countAfterSave);

        TestTransaction.flagForRollback();
        TestTransaction.end();

        TestTransaction.start();

        long countAfterRollback = orderRepository.count();

        assertEquals(countBefore, countAfterRollback);
    }
}