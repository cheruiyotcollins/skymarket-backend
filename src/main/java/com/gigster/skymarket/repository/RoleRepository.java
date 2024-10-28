package com.gigster.skymarket.repository;


import com.gigster.skymarket.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gigster.skymarket.model.Role;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(RoleName roleName);
}
