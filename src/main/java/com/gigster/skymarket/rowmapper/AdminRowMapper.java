package com.gigster.skymarket.rowmapper;

import com.gigster.skymarket.model.Admin;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminRowMapper implements RowMapper<Admin> {
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
