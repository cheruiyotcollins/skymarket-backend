package com.gigster.skymarket.repository;

import com.gigster.skymarket.enums.RoleName;
import com.gigster.skymarket.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = :roleName")
    int countByRoleName(@Param("roleName") RoleName roleName);

    @Query("SELECT COUNT(c) > 0 FROM Cart c WHERE c.customer.customerId = :customerId")
    boolean existsByCustomerId(@Param("customerId") Long customerId);

}