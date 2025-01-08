package com.bances.agua_deliciosa.repository;

import com.bances.agua_deliciosa.model.ProductOutputDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductOutputDetailRepository implements BaseRepository<ProductOutputDetail> {
    private final JdbcTemplate jdbcTemplate;

    public ProductOutputDetailRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ProductOutputDetail save(ProductOutputDetail detail) {
        if (detail.getId() == null) {
            String sql = """
                INSERT INTO product_output_details (product_output_id, product_id, 
                quantity, unit_price) VALUES (?, ?, ?, ?)
            """;
            jdbcTemplate.update(sql,
                detail.getProductOutputId(),
                detail.getProductId(),
                detail.getQuantity(),
                detail.getUnitPrice()
            );
        } else {
            String sql = """
                UPDATE product_output_details SET product_output_id = ?, 
                product_id = ?, quantity = ?, unit_price = ? WHERE id = ?
            """;
            jdbcTemplate.update(sql,
                detail.getProductOutputId(),
                detail.getProductId(),
                detail.getQuantity(),
                detail.getUnitPrice(),
                detail.getId()
            );
        }
        return detail;
    }

    @Override
    public Optional<ProductOutputDetail> findById(Long id) {
        String sql = "SELECT * FROM product_output_details WHERE id = ?";
        try {
            ProductOutputDetail detail = jdbcTemplate.queryForObject(sql, this::mapRowToProductOutputDetail, id);
            return Optional.ofNullable(detail);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ProductOutputDetail> findAll() {
        String sql = "SELECT * FROM product_output_details";
        return jdbcTemplate.query(sql, this::mapRowToProductOutputDetail);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM product_output_details WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM product_output_details WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM product_output_details";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    private ProductOutputDetail mapRowToProductOutputDetail(ResultSet rs, int rowNum) throws SQLException {
        ProductOutputDetail detail = new ProductOutputDetail();
        detail.setId(rs.getLong("id"));
        detail.setProductOutputId(rs.getLong("product_output_id"));
        detail.setProductId(rs.getLong("product_id"));
        detail.setQuantity(rs.getInt("quantity"));
        detail.setUnitPrice(rs.getBigDecimal("unit_price"));
        detail.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        detail.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return detail;
    }

    public List<ProductOutputDetail> findByProductOutputId(Long productOutputId) {
        String sql = "SELECT * FROM product_output_details WHERE product_output_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToProductOutputDetail, productOutputId);
    }

    public List<ProductOutputDetail> findByProductId(Long productId) {
        String sql = "SELECT * FROM product_output_details WHERE product_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToProductOutputDetail, productId);
    }
}
