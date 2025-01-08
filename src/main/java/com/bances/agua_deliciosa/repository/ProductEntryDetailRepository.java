package com.bances.agua_deliciosa.repository;

import com.bances.agua_deliciosa.model.ProductEntryDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductEntryDetailRepository implements BaseRepository<ProductEntryDetail> {
    private final JdbcTemplate jdbcTemplate;

    public ProductEntryDetailRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ProductEntryDetail save(ProductEntryDetail detail) {
        if (detail.getId() == null) {
            String sql = """
                INSERT INTO product_entry_details (product_entry_id, product_id, 
                quantity, unit_price) VALUES (?, ?, ?, ?)
            """;
            jdbcTemplate.update(sql,
                detail.getProductEntryId(),
                detail.getProductId(),
                detail.getQuantity(),
                detail.getUnitPrice()
            );
        } else {
            String sql = """
                UPDATE product_entry_details SET product_entry_id = ?, 
                product_id = ?, quantity = ?, unit_price = ? WHERE id = ?
            """;
            jdbcTemplate.update(sql,
                detail.getProductEntryId(),
                detail.getProductId(),
                detail.getQuantity(),
                detail.getUnitPrice(),
                detail.getId()
            );
        }
        return detail;
    }

    @Override
    public Optional<ProductEntryDetail> findById(Long id) {
        String sql = "SELECT * FROM product_entry_details WHERE id = ?";
        try {
            ProductEntryDetail detail = jdbcTemplate.queryForObject(sql, this::mapRowToProductEntryDetail, id);
            return Optional.ofNullable(detail);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ProductEntryDetail> findAll() {
        String sql = "SELECT * FROM product_entry_details";
        return jdbcTemplate.query(sql, this::mapRowToProductEntryDetail);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM product_entry_details WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM product_entry_details WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM product_entry_details";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    private ProductEntryDetail mapRowToProductEntryDetail(ResultSet rs, int rowNum) throws SQLException {
        ProductEntryDetail detail = new ProductEntryDetail();
        detail.setId(rs.getLong("id"));
        detail.setProductEntryId(rs.getLong("product_entry_id"));
        detail.setProductId(rs.getLong("product_id"));
        detail.setQuantity(rs.getInt("quantity"));
        detail.setUnitPrice(rs.getBigDecimal("unit_price"));
        detail.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        detail.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return detail;
    }

    public List<ProductEntryDetail> findByProductEntryId(Long productEntryId) {
        String sql = "SELECT * FROM product_entry_details WHERE product_entry_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToProductEntryDetail, productEntryId);
    }

    public List<ProductEntryDetail> findByProductId(Long productId) {
        String sql = "SELECT * FROM product_entry_details WHERE product_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToProductEntryDetail, productId);
    }
}
