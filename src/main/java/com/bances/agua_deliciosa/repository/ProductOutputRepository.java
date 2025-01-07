package com.bances.agua_deliciosa.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import com.bances.agua_deliciosa.model.ProductOutput;
import com.bances.agua_deliciosa.model.MovementType;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductOutputRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<ProductOutput> productOutputMapper = (rs, rowNum) -> {
        ProductOutput output = new ProductOutput();
        output.setId(rs.getLong("id"));
        output.setEmployeeId(rs.getLong("employee_id"));
        output.setMovementType(MovementType.valueOf(rs.getString("movement_type")));
        output.setDescription(rs.getString("description"));
        output.setActive(rs.getBoolean("active"));
        output.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        output.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return output;
    };
    
    public ProductOutputRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<ProductOutput> findAll() {
        String sql = """
            SELECT * FROM product_outputs
            WHERE active = true
            ORDER BY created_at DESC
            """;
        return jdbcTemplate.query(sql, productOutputMapper);
    }
    
    public Optional<ProductOutput> findById(Long id) {
        String sql = """
            SELECT * FROM product_outputs
            WHERE id = ? AND active = true
            """;
        try {
            ProductOutput output = jdbcTemplate.queryForObject(sql, productOutputMapper, id);
            return Optional.ofNullable(output);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public List<ProductOutput> findByEmployeeId(Long employeeId) {
        String sql = """
            SELECT * FROM product_outputs
            WHERE employee_id = ? AND active = true
            ORDER BY created_at DESC
            """;
        return jdbcTemplate.query(sql, productOutputMapper, employeeId);
    }
    
    public List<ProductOutput> findByMovementType(MovementType movementType) {
        String sql = """
            SELECT * FROM product_outputs
            WHERE movement_type = ? AND active = true
            ORDER BY created_at DESC
            """;
        return jdbcTemplate.query(sql, productOutputMapper, movementType.name());
    }
    
    public ProductOutput save(ProductOutput output) {
        if (output.getId() == null) {
            return insert(output);
        }
        return update(output);
    }
    
    private ProductOutput insert(ProductOutput output) {
        String sql = """
            INSERT INTO product_outputs (
                employee_id, movement_type, description,
                active, created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?)
            """;
            
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, output.getEmployeeId());
            ps.setString(2, output.getMovementType().toString());
            ps.setString(3, output.getDescription());
            ps.setBoolean(4, output.isActive());
            ps.setTimestamp(5, Timestamp.valueOf(output.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(output.getUpdatedAt()));
            return ps;
        }, keyHolder);
        
        Number key = keyHolder.getKey();
        if (key != null) {
            output.setId(key.longValue());
        }
        return output;
    }
    
    private ProductOutput update(ProductOutput output) {
        String sql = """
            UPDATE product_outputs SET
                employee_id = ?, movement_type = ?,
                description = ?, active = ?,
                updated_at = ?
            WHERE id = ?
            """;
            
        jdbcTemplate.update(sql,
            output.getEmployeeId(),
            output.getMovementType().name(),
            output.getDescription(),
            output.isActive(),
            Timestamp.valueOf(output.getUpdatedAt()),
            output.getId()
        );
        
        return output;
    }
    
    public void delete(Long id) {
        String sql = """
            UPDATE product_outputs SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
        jdbcTemplate.update(sql, id);
    }
    
    public boolean existsById(Long id) {
        String sql = """
            SELECT COUNT(*) FROM product_outputs
            WHERE id = ? AND active = true
            """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }
}
