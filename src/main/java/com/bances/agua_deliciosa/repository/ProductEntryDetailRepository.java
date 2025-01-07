package com.bances.agua_deliciosa.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.EmptyResultDataAccessException;
import com.bances.agua_deliciosa.model.ProductEntryDetail;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductEntryDetailRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<ProductEntryDetail> productEntryDetailMapper = (rs, rowNum) -> {
        ProductEntryDetail detail = new ProductEntryDetail();
        detail.setId(rs.getLong("id"));
        detail.setProductEntryId(rs.getLong("product_entry_id"));
        detail.setProductId(rs.getLong("product_id"));
        detail.setQuantity(rs.getInt("quantity"));
        detail.setUnitPrice(rs.getBigDecimal("unit_price"));
        detail.setActive(rs.getBoolean("active"));
        detail.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        detail.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return detail;
    };
    
    public ProductEntryDetailRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<ProductEntryDetail> findByProductEntryId(Long productEntryId) {
        String sql = """
            SELECT * FROM product_entry_details
            WHERE product_entry_id = ? AND active = true
            ORDER BY id
            """;
        return jdbcTemplate.query(sql, productEntryDetailMapper, productEntryId);
    }
    
    public Optional<ProductEntryDetail> findById(Long id) {
        String sql = """
            SELECT * FROM product_entry_details
            WHERE id = ? AND active = true
            """;
        try {
            ProductEntryDetail detail = jdbcTemplate.queryForObject(sql, productEntryDetailMapper, id);
            return Optional.ofNullable(detail);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    @Transactional
    public ProductEntryDetail save(ProductEntryDetail detail) {
        // Calcular el subtotal antes de guardar
        detail.setSubtotal(detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));
        
        if (detail.getId() == null) {
            return insert(detail);
        }
        return update(detail);
    }
    
    private ProductEntryDetail insert(ProductEntryDetail detail) {
        String sql = """
            INSERT INTO product_entry_details (
                product_entry_id, product_id,
                quantity, unit_price, active,
                subtotal, created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
            
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, detail.getProductEntryId());
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
    
    private ProductEntryDetail update(ProductEntryDetail detail) {
        String sql = """
            UPDATE product_entry_details SET
                product_entry_id = ?, product_id = ?,
                quantity = ?, unit_price = ?, subtotal = ?,
                active = ?, updated_at = ?
            WHERE id = ?
            """;
            
        jdbcTemplate.update(sql,
            detail.getProductEntryId(),
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
            UPDATE product_entry_details SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
        jdbcTemplate.update(sql, id);
    }
    
    public void deleteByProductEntryId(Long productEntryId) {
        String sql = """
            UPDATE product_entry_details SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE product_entry_id = ?
            """;
        jdbcTemplate.update(sql, productEntryId);
    }
    
    public boolean existsById(Long id) {
        String sql = """
            SELECT COUNT(*) FROM product_entry_details
            WHERE id = ? AND active = true
            """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }
    
    @Transactional
    public void updateQuantityAndPrice(Long id, int quantity, BigDecimal unitPrice) {
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        
        int updatedRows = jdbcTemplate.update(
            """
            UPDATE product_entry_details 
            SET quantity = ?,
                unit_price = ?,
                subtotal = ?,
                updated_at = CURRENT_TIMESTAMP 
            WHERE id = ?
            """,
            quantity,
            unitPrice,
            subtotal,
            id
        );
        
        if (updatedRows == 0) {
            throw new RuntimeException("Product entry detail not found with id: " + id);
        }
    }
    
    public List<ProductEntryDetail> saveAll(List<ProductEntryDetail> details) {
        return details.stream()
            .map(this::save)
            .toList();
    }
    
    public void deleteById(Long id) {
        int deletedRows = jdbcTemplate.update("DELETE FROM product_entry_details WHERE id = ?", id);
        if (deletedRows == 0) {
            throw new RuntimeException("Product entry detail not found with id: " + id);
        }
    }
    
    public BigDecimal calculateTotalByEntryId(Long entryId) {
        BigDecimal total = jdbcTemplate.queryForObject(
            "SELECT COALESCE(SUM(subtotal), 0) FROM product_entry_details WHERE product_entry_id = ?",
            BigDecimal.class,
            entryId
        );
        return total != null ? total : BigDecimal.ZERO;
    }
}
