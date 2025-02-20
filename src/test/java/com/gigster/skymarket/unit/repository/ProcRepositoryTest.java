package com.gigster.skymarket.unit.repository;

import com.gigster.skymarket.model.Admin;
import com.gigster.skymarket.model.SuperAdmin;
import com.gigster.skymarket.repository.ProcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ProcRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ProcRepository procRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAdmins() {
        // Arrange
        List<Admin> expectedAdmins = Arrays.asList(
                Admin.builder()
                        .email("admin1@example.com")
                        .adminId(1L)
                        .fullName("Admin One")
                        .createdOn(Instant.parse("2025-01-01T00:00:00Z"))
                        .contact("123456789")
                        .build(),
                Admin.builder()
                        .email("admin2@example.com")
                        .adminId(2L)
                        .fullName("Admin Two")
                        .createdOn(Instant.parse("2025-01-02T00:00:00Z"))
                        .contact("987654321")
                        .build()
        );

        ArgumentCaptor<RowMapper<Admin>> captor = ArgumentCaptor.forClass(RowMapper.class);
        when(jdbcTemplate.query(anyString(), captor.capture())).thenReturn(expectedAdmins);

        // Act
        List<Admin> admins = procRepository.getAdmins();

        // Assert
        assertEquals(expectedAdmins, admins);
        verify(jdbcTemplate, times(1)).query(eq("SELECT * from get_admins();"), any(RowMapper.class));
    }

    @Test
    void testGetSuperAdmins() {
        // Arrange
        List<SuperAdmin> expectedSuperAdmins = Collections.singletonList(
                SuperAdmin.builder()
                        .email("superadmin1@example.com")
                        .superAdminId(1L)
                        .fullName("SuperAdmin One")
                        .createdOn(Instant.parse("2025-01-01T00:00:00Z"))
                        .contact("123456789")
                        .employeeNo("EMP001")
                        .build()
        );

        ArgumentCaptor<RowMapper<SuperAdmin>> captor = ArgumentCaptor.forClass(RowMapper.class);
        when(jdbcTemplate.query(anyString(), captor.capture())).thenReturn(expectedSuperAdmins);

        // Act
        List<SuperAdmin> superAdmins = procRepository.getSuperAdmins();

        // Assert
        assertEquals(expectedSuperAdmins, superAdmins);
        verify(jdbcTemplate, times(1)).query(eq("SELECT * FROM get_super_admins();"), any(RowMapper.class));
    }
}
