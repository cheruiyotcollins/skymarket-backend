package com.gigster.skymarket.unit.repository;

import com.gigster.skymarket.model.Admin;
import com.gigster.skymarket.model.SuperAdmin;
import com.gigster.skymarket.repository.ProcRepository;
import com.gigster.skymarket.rowmapper.AdminRowMapper;
import com.gigster.skymarket.rowmapper.SuperAdminRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ProcRepository procRepository;

    private List<Admin> adminList;
    private List<SuperAdmin> superAdminList;

    @BeforeEach
    void setUp() {
        adminList = List.of(
                Admin.builder().adminId(1L).email("admin@example.com").fullName("Admin One").contact("1234567890").build()
        );

        superAdminList = List.of(
                SuperAdmin.builder().superAdminId(1L).email("superadmin@example.com").fullName("SuperAdmin One").contact("0987654321").employeeNo("EMP001").build()
        );
    }

    @Test
    void getAdmins_ShouldReturnPagedAdmins() {
        Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());
        String query = "SELECT * FROM get_admins() ORDER BY UNSORTED LIMIT ? OFFSET ?;";

        doReturn(adminList).when(jdbcTemplate).query(eq(query), any(AdminRowMapper.class), eq(10), eq(0L));
        doReturn(1).when(jdbcTemplate).queryForObject(anyString(), eq(Integer.class));

        Page<Admin> result = procRepository.getAdmins(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(adminList, result.getContent());
    }

    @Test
    void getSuperAdmins_ShouldReturnPagedSuperAdmins() {
        Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());
        String query = "SELECT * FROM get_super_admins() ORDER BY UNSORTED LIMIT ? OFFSET ?;";

        doReturn(superAdminList).when(jdbcTemplate).query(eq(query), any(SuperAdminRowMapper.class), eq(10), eq(0L));
        doReturn(1).when(jdbcTemplate).queryForObject(anyString(), eq(Integer.class));

        Page<SuperAdmin> result = procRepository.getSuperAdmins(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(superAdminList, result.getContent());
    }
}
