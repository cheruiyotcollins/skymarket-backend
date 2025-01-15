package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    @Query("SELECT c FROM Customer c JOIN c.likedProducts lp WHERE lp.id IN :productIds")
    List<Customer> findCustomersByLikedProducts(@Param("productIds") Set<Long> productIds);
}
