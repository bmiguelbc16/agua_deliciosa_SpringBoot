package com.bances.agua_deliciosa.repository;

import com.bances.agua_deliciosa.model.ProductEntry;
import com.bances.agua_deliciosa.model.MovementType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductEntryRepository implements BaseRepository<ProductEntry> {
    private final JdbcTemplate jdbcTemplate;

    public ProductEntryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ProductEntry save(ProductEntry entry) {
        if (entry.getId() == null) {
            String sql = """
                INSERT INTO product_entries (employee_id, movement_type, 
                description, total_amount) VALUES (?, ?, ?, ?)
            """;
            jdbcTemplate.update(sql,
                entry.getEmployeeId(),
                entry.getMovementType().name(),
                entry.getDescription(),
                entry.getTotalAmount()
            );
        } else {
            String sql = """
                UPDATE product_entries SET employee_id = ?, movement_type = ?, 
                description = ?, total_amount = ? WHERE id = ?
            """;
            jdbcTemplate.update(sql,
                entry.getEmployeeId(),
                entry.getMovementType().name(),
                entry.getDescription(),
                entry.getTotalAmount(),
                entry.getId()
            );
        }
        return entry;
    }

    @Override
    public Optional<ProductEntry> findById(Long id) {
        String sql = "SELECT * FROM product_entries WHERE id = ?";
        try {
            ProductEntry entry = jdbcTemplate.queryForObject(sql, this::mapRowToProductEntry, id);
            return Optional.ofNullable(entry);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ProductEntry> findAll() {
        String sql = "SELECT * FROM product_entries";
        return jdbcTemplate.query(sql, this::mapRowToProductEntry);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM product_entries WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM product_entries WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM product_entries";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    private ProductEntry mapRowToProductEntry(ResultSet rs, int rowNum) throws SQLException {
        ProductEntry entry = new ProductEntry();
        entry.setId(rs.getLong("id"));
        entry.setEmployeeId(rs.getLong("employee_id"));
        entry.setMovementType(MovementType.valueOf(rs.getString("movement_type")));
        entry.setDescription(rs.getString("description"));
        entry.setTotalAmount(rs.getBigDecimal("total_amount"));
        entry.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        entry.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return entry;
    }

    public List<ProductEntry> findByEmployeeId(Long employeeId) {
        String sql = "SELECT * FROM product_entries WHERE employee_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToProductEntry, employeeId);
    }

    public List<ProductEntry> findByMovementType(MovementType movementType) {
        String sql = "SELECT * FROM product_entries WHERE movement_type = ?";
        return jdbcTemplate.query(sql, this::mapRowToProductEntry, movementType.name());
    }
}
