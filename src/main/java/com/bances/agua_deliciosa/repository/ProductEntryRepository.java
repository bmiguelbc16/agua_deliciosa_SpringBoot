package com.bances.agua_deliciosa.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import com.bances.agua_deliciosa.model.ProductEntry;
import com.bances.agua_deliciosa.model.MovementType;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductEntryRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<ProductEntry> productEntryMapper = (rs, rowNum) -> {
        ProductEntry entry = new ProductEntry();
        entry.setId(rs.getLong("id"));
        entry.setEmployeeId(rs.getLong("employee_id"));
        entry.setMovementType(MovementType.valueOf(rs.getString("movement_type")));
        entry.setDescription(rs.getString("description"));
        entry.setActive(rs.getBoolean("active"));
        entry.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        entry.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return entry;
    };
    
    public ProductEntryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<ProductEntry> findAll() {
        String sql = """
            SELECT * FROM product_entries
            WHERE active = true
            ORDER BY created_at DESC
            """;
        return jdbcTemplate.query(sql, productEntryMapper);
    }
    
    public Optional<ProductEntry> findById(Long id) {
        String sql = """
            SELECT * FROM product_entries
            WHERE id = ? AND active = true
            """;
        try {
            ProductEntry entry = jdbcTemplate.queryForObject(sql, productEntryMapper, id);
            return Optional.ofNullable(entry);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public List<ProductEntry> findByEmployeeId(Long employeeId) {
        String sql = """
            SELECT * FROM product_entries
            WHERE employee_id = ? AND active = true
            ORDER BY created_at DESC
            """;
        return jdbcTemplate.query(sql, productEntryMapper, employeeId);
    }
    
    public List<ProductEntry> findByMovementType(MovementType movementType) {
        String sql = """
            SELECT * FROM product_entries
            WHERE movement_type = ? AND active = true
            ORDER BY created_at DESC
            """;
        return jdbcTemplate.query(sql, productEntryMapper, movementType.name());
    }
    
    public ProductEntry save(ProductEntry entry) {
        if (entry.getId() == null) {
            return insert(entry);
        }
        return update(entry);
    }
    
    private ProductEntry insert(ProductEntry entry) {
        String sql = """
            INSERT INTO product_entries (
                employee_id, movement_type, description,
                active, created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?)
            """;
            
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, entry.getEmployeeId());
            ps.setString(2, entry.getMovementType().toString());
            ps.setString(3, entry.getDescription());
            ps.setBoolean(4, entry.isActive());
            ps.setTimestamp(5, Timestamp.valueOf(entry.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(entry.getUpdatedAt()));
            return ps;
        }, keyHolder);
        
        Number key = keyHolder.getKey();
        if (key != null) {
            entry.setId(key.longValue());
        }
        return entry;
    }
    
    private ProductEntry update(ProductEntry entry) {
        String sql = """
            UPDATE product_entries SET
                employee_id = ?, movement_type = ?,
                description = ?, active = ?,
                updated_at = ?
            WHERE id = ?
            """;
            
        jdbcTemplate.update(sql,
            entry.getEmployeeId(),
            entry.getMovementType().name(),
            entry.getDescription(),
            entry.isActive(),
            Timestamp.valueOf(entry.getUpdatedAt()),
            entry.getId()
        );
        
        return entry;
    }
    
    public void delete(Long id) {
        String sql = """
            UPDATE product_entries SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
        jdbcTemplate.update(sql, id);
    }
    
    public boolean existsById(Long id) {
        String sql = """
            SELECT COUNT(*) FROM product_entries
            WHERE id = ? AND active = true
            """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }
}
