package com.bances.agua_deliciosa.repository;

import com.bances.agua_deliciosa.model.ProductOutput;
import com.bances.agua_deliciosa.model.MovementType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductOutputRepository implements BaseRepository<ProductOutput> {
    private final JdbcTemplate jdbcTemplate;

    public ProductOutputRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ProductOutput save(ProductOutput output) {
        if (output.getId() == null) {
            String sql = """
                INSERT INTO product_outputs (employee_id, order_id, movement_type, 
                description, total_amount) VALUES (?, ?, ?, ?, ?)
            """;
            jdbcTemplate.update(sql,
                output.getEmployeeId(),
                output.getOrderId(),
                output.getMovementType().name(),
                output.getDescription(),
                output.getTotalAmount()
            );
        } else {
            String sql = """
                UPDATE product_outputs SET employee_id = ?, order_id = ?, 
                movement_type = ?, description = ?, total_amount = ? WHERE id = ?
            """;
            jdbcTemplate.update(sql,
                output.getEmployeeId(),
                output.getOrderId(),
                output.getMovementType().name(),
                output.getDescription(),
                output.getTotalAmount(),
                output.getId()
            );
        }
        return output;
    }

    @Override
    public Optional<ProductOutput> findById(Long id) {
        String sql = "SELECT * FROM product_outputs WHERE id = ?";
        try {
            ProductOutput output = jdbcTemplate.queryForObject(sql, this::mapRowToProductOutput, id);
            return Optional.ofNullable(output);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ProductOutput> findAll() {
        String sql = "SELECT * FROM product_outputs";
        return jdbcTemplate.query(sql, this::mapRowToProductOutput);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM product_outputs WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM product_outputs WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM product_outputs";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    private ProductOutput mapRowToProductOutput(ResultSet rs, int rowNum) throws SQLException {
        ProductOutput output = new ProductOutput();
        output.setId(rs.getLong("id"));
        output.setEmployeeId(rs.getLong("employee_id"));
        output.setOrderId(rs.getLong("order_id"));
        output.setMovementType(MovementType.valueOf(rs.getString("movement_type")));
        output.setDescription(rs.getString("description"));
        output.setTotalAmount(rs.getBigDecimal("total_amount"));
        output.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        output.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return output;
    }

    public List<ProductOutput> findByEmployeeId(Long employeeId) {
        String sql = "SELECT * FROM product_outputs WHERE employee_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToProductOutput, employeeId);
    }

    public List<ProductOutput> findByOrderId(Long orderId) {
        String sql = "SELECT * FROM product_outputs WHERE order_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToProductOutput, orderId);
    }

    public List<ProductOutput> findByMovementType(MovementType movementType) {
        String sql = "SELECT * FROM product_outputs WHERE movement_type = ?";
        return jdbcTemplate.query(sql, this::mapRowToProductOutput, movementType.name());
    }
}
