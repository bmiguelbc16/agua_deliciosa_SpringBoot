package com.bances.agua_deliciosa.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import com.bances.agua_deliciosa.model.ProductOutputDetail;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductOutputDetailRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<ProductOutputDetail> productOutputDetailMapper = (rs, rowNum) -> {
        ProductOutputDetail detail = new ProductOutputDetail();
        detail.setId(rs.getLong("id"));
        detail.setProductOutputId(rs.getLong("product_output_id"));
        detail.setProductId(rs.getLong("product_id"));
        detail.setQuantity(rs.getInt("quantity"));
        detail.setActive(rs.getBoolean("active"));
        detail.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        detail.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return detail;
    };
    
    public ProductOutputDetailRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<ProductOutputDetail> findByProductOutputId(Long productOutputId) {
        String sql = """
            SELECT * FROM product_output_details
            WHERE product_output_id = ? AND active = true
            ORDER BY id
            """;
        return jdbcTemplate.query(sql, productOutputDetailMapper, productOutputId);
    }
    
    public Optional<ProductOutputDetail> findById(Long id) {
        String sql = """
            SELECT * FROM product_output_details
            WHERE id = ? AND active = true
            """;
        try {
            ProductOutputDetail detail = jdbcTemplate.queryForObject(sql, productOutputDetailMapper, id);
            return Optional.ofNullable(detail);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public ProductOutputDetail save(ProductOutputDetail detail) {
        if (detail.getId() == null) {
            return insert(detail);
        }
        return update(detail);
    }
    
    private ProductOutputDetail insert(ProductOutputDetail detail) {
        String sql = """
            INSERT INTO product_output_details (
                product_output_id, product_id,
                quantity, unit_price, active,
                subtotal, created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
            
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, detail.getProductOutputId());
            ps.setLong(2, detail.getProductId());
            ps.setInt(3, detail.getQuantity());
            ps.setBigDecimal(4, detail.getUnitPrice());
            ps.setBoolean(5, detail.isActive());
            ps.setBigDecimal(6, detail.getSubtotal());
            ps.setTimestamp(7, Timestamp.valueOf(detail.getCreatedAt()));
            ps.setTimestamp(8, Timestamp.valueOf(detail.getUpdatedAt()));
            return ps;
        }, keyHolder);
        
        Number key = keyHolder.getKey();
        if (key != null) {
            detail.setId(key.longValue());
        }
        return detail;
    }
    
    private ProductOutputDetail update(ProductOutputDetail detail) {
        String sql = """
            UPDATE product_output_details SET
                product_output_id = ?, product_id = ?,
                quantity = ?, unit_price = ?, subtotal = ?,
                active = ?,
                updated_at = ?
            WHERE id = ?
            """;
            
        jdbcTemplate.update(sql,
            detail.getProductOutputId(),
            detail.getProductId(),
            detail.getQuantity(),
            detail.getUnitPrice(),
            detail.getSubtotal(),
            detail.isActive(),
            Timestamp.valueOf(detail.getUpdatedAt()),
            detail.getId()
        );
        
        return detail;
    }
    
    public void delete(Long id) {
        String sql = """
            UPDATE product_output_details SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
        jdbcTemplate.update(sql, id);
    }
    
    public void deleteByProductOutputId(Long productOutputId) {
        String sql = """
            UPDATE product_output_details SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE product_output_id = ?
            """;
        jdbcTemplate.update(sql, productOutputId);
    }
    
    public boolean existsById(Long id) {
        String sql = """
            SELECT COUNT(*) FROM product_output_details
            WHERE id = ? AND active = true
            """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }
}
