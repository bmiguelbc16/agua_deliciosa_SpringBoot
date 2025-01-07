package com.bances.agua_deliciosa.repository;

import java.sql.ResultSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bances.agua_deliciosa.model.Product;

@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Product> productMapper = (ResultSet rs, int rowNum) -> {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setStock(rs.getInt("stock"));
        product.setActive(rs.getBoolean("active"));
        return product;
    };

    public List<Product> findAll() {
        String sql = "SELECT * FROM products WHERE active = true";
        return jdbcTemplate.query(sql, productMapper);
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM products WHERE active = true";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }
}