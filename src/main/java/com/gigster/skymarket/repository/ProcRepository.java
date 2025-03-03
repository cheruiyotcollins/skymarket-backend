package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.Admin;
import com.gigster.skymarket.model.SuperAdmin;
import com.gigster.skymarket.rowmapper.AdminRowMapper;
import com.gigster.skymarket.rowmapper.SuperAdminRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public class ProcRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Page<Admin> getAdmins(Pageable pageable) {
        String QUERY = "SELECT * FROM get_admins() ORDER BY " + pageable.getSort().toString().replace(":", "") + " LIMIT ? OFFSET ?;";
        List<Admin> admins = jdbcTemplate.query(QUERY, new AdminRowMapper(), pageable.getPageSize(), pageable.getOffset());

        String COUNT_QUERY = "SELECT COUNT(*) FROM get_admins();";
        int totalRecords = jdbcTemplate.queryForObject(COUNT_QUERY, Integer.class);

        return new PageImpl<>(admins, pageable, totalRecords);
    }

    public Page<SuperAdmin> getSuperAdmins(Pageable pageable) {
        String QUERY = "SELECT * FROM get_super_admins() ORDER BY " + pageable.getSort().toString().replace(":", "") + " LIMIT ? OFFSET ?;";
        List<SuperAdmin> superAdmins = jdbcTemplate.query(QUERY, new SuperAdminRowMapper(), pageable.getPageSize(), pageable.getOffset());

        String COUNT_QUERY = "SELECT COUNT(*) FROM get_super_admins();";
        int totalRecords = jdbcTemplate.queryForObject(COUNT_QUERY, Integer.class);

        return new PageImpl<>(superAdmins, pageable, totalRecords);
    }
}
