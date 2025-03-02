package com.gigster.skymarket.rowmapper;

import com.gigster.skymarket.model.SuperAdmin;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SuperAdminRowMapper implements RowMapper<SuperAdmin> {
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
