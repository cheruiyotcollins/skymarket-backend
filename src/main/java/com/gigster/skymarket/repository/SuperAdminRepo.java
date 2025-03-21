package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuperAdminRepo extends JpaRepository<SuperAdmin,Long> {

    SuperAdmin findBySuperAdminId(Long adminId);
}