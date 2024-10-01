package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
