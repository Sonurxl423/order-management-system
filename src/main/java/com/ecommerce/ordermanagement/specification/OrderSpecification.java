package com.ecommerce.ordermanagement.specification;

import com.ecommerce.ordermanagement.entity.Order;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {

    public static Specification<Order> hasProductName(String productName) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("productName"), productName);
    }

    public static Specification<Order> hasMinimumQuantity(Integer quantity) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("quantity"),
                        quantity
                );
    }

    public static Specification<Order> hasMaximumQuantity(Integer quantity) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("quantity"),
                        quantity
                );
    }
}