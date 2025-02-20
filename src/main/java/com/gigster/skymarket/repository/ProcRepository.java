package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.Admin;
import com.gigster.skymarket.model.SuperAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@Repository
public class ProcRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Admin> getAdmins() {
        String QUERY = "SELECT * from get_admins();";
        return jdbcTemplate.query(QUERY, new AdminRowMapper());
    }

    public List<SuperAdmin> getSuperAdmins() {
        String QUERY = "SELECT * FROM get_super_admins();";
        return jdbcTemplate.query(QUERY, new SuperAdminRowMapper());
    }

    private static class AdminRowMapper implements RowMapper<Admin> {
        @Override
        public Admin mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Admin.builder()
                    .email(rs.getString("email"))
                    .adminId(rs.getLong("admin_id"))
                    .fullName(rs.getString("full_name"))
                    .createdOn(rs.getTimestamp("created_on") != null ? rs.getTimestamp("created_on").toInstant() : null)
                    .contact(rs.getString("contact"))
                    .build();
        }
    }

    private static class SuperAdminRowMapper implements RowMapper<SuperAdmin> {
        @Override
        public SuperAdmin mapRow(ResultSet rs, int rowNum) throws SQLException {
            return SuperAdmin.builder()
                    .email(rs.getString("email"))
                    .superAdminId(rs.getLong("superAdmin_id"))
                    .fullName(rs.getString("full_name"))
                    .createdOn(rs.getTimestamp("created_on") != null ? rs.getTimestamp("created_on").toInstant() : null)
                    .contact(rs.getString("contact"))
                    .employeeNo(rs.getString("employee_no"))
                    .build();
        }
    }
}
