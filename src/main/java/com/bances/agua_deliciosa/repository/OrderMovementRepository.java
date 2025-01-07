package com.bances.agua_deliciosa.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import com.bances.agua_deliciosa.model.OrderMovement;
import com.bances.agua_deliciosa.model.MovementType;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderMovementRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<OrderMovement> orderMovementMapper = (rs, rowNum) -> {
        OrderMovement movement = new OrderMovement();
        movement.setId(rs.getLong("id"));
        movement.setOrderId(rs.getLong("order_id"));
        movement.setEmployeeId(rs.getLong("employee_id"));
        movement.setMovementType(MovementType.valueOf(rs.getString("movement_type")));
        movement.setDescription(rs.getString("description"));
        movement.setActive(rs.getBoolean("active"));
        movement.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        movement.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return movement;
    };
    
    public OrderMovementRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<OrderMovement> findByOrderId(Long orderId) {
        String sql = """
            SELECT * FROM order_movements
            WHERE order_id = ? AND active = true
            ORDER BY created_at DESC
            """;
        return jdbcTemplate.query(sql, orderMovementMapper, orderId);
    }
    
    public List<OrderMovement> findByEmployeeId(Long employeeId) {
        String sql = """
            SELECT * FROM order_movements
            WHERE employee_id = ? AND active = true
            ORDER BY created_at DESC
            """;
        return jdbcTemplate.query(sql, orderMovementMapper, employeeId);
    }
    
    public List<OrderMovement> findByMovementType(MovementType movementType) {
        String sql = """
            SELECT * FROM order_movements
            WHERE movement_type = ? AND active = true
            ORDER BY created_at DESC
            """;
        return jdbcTemplate.query(sql, orderMovementMapper, movementType.name());
    }
    
    public Optional<OrderMovement> findById(Long id) {
        String sql = """
            SELECT * FROM order_movements
            WHERE id = ? AND active = true
            """;
        try {
            OrderMovement movement = jdbcTemplate.queryForObject(sql, orderMovementMapper, id);
            return Optional.ofNullable(movement);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public OrderMovement save(OrderMovement movement) {
        if (movement.getId() == null) {
            return insert(movement);
        }
        return update(movement);
    }
    
    private OrderMovement insert(OrderMovement movement) {
        String sql = """
            INSERT INTO order_movements (
                order_id, employee_id,
                movement_type, description,
                active, created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
            
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, movement.getOrderId());
            ps.setLong(2, movement.getEmployeeId());
            ps.setString(3, movement.getMovementType().name());
            ps.setString(4, movement.getDescription());
            ps.setBoolean(5, movement.isActive());
            ps.setTimestamp(6, Timestamp.valueOf(movement.getCreatedAt()));
            ps.setTimestamp(7, Timestamp.valueOf(movement.getUpdatedAt()));
            return ps;
        }, keyHolder);
        
        // Obtener el ID generado y asignarlo al movimiento
        Number key = keyHolder.getKey();
        if (key != null) {
            movement.setId(key.longValue());
        }
        return movement;
    }
    
    private OrderMovement update(OrderMovement movement) {
        String sql = """
            UPDATE order_movements SET
                order_id = ?, employee_id = ?,
                movement_type = ?, description = ?,
                active = ?, updated_at = ?
            WHERE id = ?
            """;
            
        jdbcTemplate.update(sql,
            movement.getOrderId(),
            movement.getEmployeeId(),
            movement.getMovementType().name(),
            movement.getDescription(),
            movement.isActive(),
            Timestamp.valueOf(movement.getUpdatedAt()),
            movement.getId()
        );
        
        return movement;
    }
    
    public void delete(Long id) {
        String sql = """
            UPDATE order_movements SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
        jdbcTemplate.update(sql, id);
    }
    
    public void deleteByOrderId(Long orderId) {
        String sql = """
            UPDATE order_movements SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE order_id = ?
            """;
        jdbcTemplate.update(sql, orderId);
    }
    
    public boolean existsById(Long id) {
        String sql = """
            SELECT COUNT(*) FROM order_movements
            WHERE id = ? AND active = true
            """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null ? count > 0 : false;
    }
}
