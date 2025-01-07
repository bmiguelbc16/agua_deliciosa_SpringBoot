package com.bances.agua_deliciosa.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import com.bances.agua_deliciosa.model.OrderMovementDetail;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderMovementDetailRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<OrderMovementDetail> orderMovementDetailMapper = (rs, rowNum) -> {
        OrderMovementDetail detail = new OrderMovementDetail();
        detail.setId(rs.getLong("id"));
        detail.setOrderMovementId(rs.getLong("order_movement_id"));
        detail.setOrderDetailId(rs.getLong("order_detail_id"));
        detail.setQuantity(rs.getInt("quantity"));
        detail.setActive(rs.getBoolean("active"));
        detail.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        detail.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return detail;
    };
    
    public OrderMovementDetailRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<OrderMovementDetail> findByOrderMovementId(Long orderMovementId) {
        String sql = """
            SELECT * FROM order_movement_details
            WHERE order_movement_id = ? AND active = true
            ORDER BY id
            """;
        return jdbcTemplate.query(sql, orderMovementDetailMapper, orderMovementId);
    }
    
    public List<OrderMovementDetail> findByOrderDetailId(Long orderDetailId) {
        String sql = """
            SELECT * FROM order_movement_details
            WHERE order_detail_id = ? AND active = true
            ORDER BY created_at DESC
            """;
        return jdbcTemplate.query(sql, orderMovementDetailMapper, orderDetailId);
    }
    
    public Optional<OrderMovementDetail> findById(Long id) {
        String sql = """
            SELECT * FROM order_movement_details
            WHERE id = ? AND active = true
            """;
        try {
            OrderMovementDetail detail = jdbcTemplate.queryForObject(sql, orderMovementDetailMapper, id);
            return Optional.ofNullable(detail);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public OrderMovementDetail save(OrderMovementDetail detail) {
        if (detail.getId() == null) {
            return insert(detail);
        }
        return update(detail);
    }
    
    private OrderMovementDetail insert(OrderMovementDetail detail) {
        String sql = """
            INSERT INTO order_movement_details (
                order_movement_id, order_detail_id,
                quantity, active,
                created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?)
            """;
            
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, detail.getOrderMovementId());
            ps.setLong(2, detail.getOrderDetailId());
            ps.setInt(3, detail.getQuantity());
            ps.setBoolean(4, detail.isActive());
            ps.setTimestamp(5, Timestamp.valueOf(detail.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(detail.getUpdatedAt()));
            return ps;
        }, keyHolder);
        
        // Obtener el ID generado y asignarlo al detalle
        Number key = keyHolder.getKey();
        if (key != null) {
            detail.setId(key.longValue());
        }
        return detail;
    }
    
    private OrderMovementDetail update(OrderMovementDetail detail) {
        String sql = """
            UPDATE order_movement_details SET
                order_movement_id = ?, order_detail_id = ?,
                quantity = ?, active = ?,
                updated_at = ?
            WHERE id = ?
            """;
            
        jdbcTemplate.update(sql,
            detail.getOrderMovementId(),
            detail.getOrderDetailId(),
            detail.getQuantity(),
            detail.isActive(),
            Timestamp.valueOf(detail.getUpdatedAt()),
            detail.getId()
        );
        
        return detail;
    }
    
    public void delete(Long id) {
        String sql = """
            UPDATE order_movement_details SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
        jdbcTemplate.update(sql, id);
    }
    
    public void deleteByOrderMovementId(Long orderMovementId) {
        String sql = """
            UPDATE order_movement_details SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE order_movement_id = ?
            """;
        jdbcTemplate.update(sql, orderMovementId);
    }
    
    public boolean existsById(Long id) {
        String sql = """
            SELECT COUNT(*) FROM order_movement_details
            WHERE id = ? AND active = true
            """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null ? count > 0 : false;
    }
}
