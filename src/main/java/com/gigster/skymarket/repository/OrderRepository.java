package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId")
    List<Order> findByCustomerId(@Param("customerId") Long customerId);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    @Query("SELECT o FROM Order o JOIN FETCH o.orderProducts WHERE o.id = :orderId")
    Optional<Order> findByIdWithProducts(@Param("orderId") Long orderId);
}
