package com.bances.agua_deliciosa.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import com.bances.agua_deliciosa.model.OrderDetail;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDetailRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<OrderDetail> orderDetailMapper = (rs, rowNum) -> {
        OrderDetail detail = new OrderDetail();
        detail.setId(rs.getLong("id"));
        detail.setOrderId(rs.getLong("order_id"));
        detail.setProductId(rs.getLong("product_id"));
        detail.setQuantity(rs.getInt("quantity"));
        detail.setUnitPrice(rs.getBigDecimal("unit_price"));
        detail.setActive(rs.getBoolean("active"));
        detail.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        detail.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return detail;
    };
    
    public OrderDetailRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<OrderDetail> findByOrderId(Long orderId) {
        String sql = """
            SELECT * FROM order_details
            WHERE order_id = ? AND active = true
            ORDER BY id
            """;
        return jdbcTemplate.query(sql, orderDetailMapper, orderId);
    }
    
    public Optional<OrderDetail> findById(Long id) {
        String sql = """
            SELECT * FROM order_details
            WHERE id = ? AND active = true
            """;
        try {
            OrderDetail detail = jdbcTemplate.queryForObject(sql, orderDetailMapper, id);
            return Optional.ofNullable(detail);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public OrderDetail save(OrderDetail detail) {
        if (detail.getId() == null) {
            return insert(detail);
        }
        return update(detail);
    }
    
    private OrderDetail insert(OrderDetail detail) {
        String sql = """
            INSERT INTO order_details (
                order_id, product_id, quantity,
                unit_price, active, created_at,
                updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
            
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, detail.getOrderId());
            ps.setLong(2, detail.getProductId());
            ps.setInt(3, detail.getQuantity());
            ps.setBigDecimal(4, detail.getUnitPrice());
            ps.setBoolean(5, detail.isActive());
            ps.setTimestamp(6, Timestamp.valueOf(detail.getCreatedAt()));
            ps.setTimestamp(7, Timestamp.valueOf(detail.getUpdatedAt()));
            return ps;
        }, keyHolder);
        
        // Obtener el ID generado y asignarlo al detalle
        Number key = keyHolder.getKey();
        if (key != null) {
            detail.setId(key.longValue());
        }
        return detail;
    }
    
    private OrderDetail update(OrderDetail detail) {
        String sql = """
            UPDATE order_details SET
                order_id = ?, product_id = ?,
                quantity = ?, unit_price = ?,
                active = ?, updated_at = ?
            WHERE id = ?
            """;
            
        jdbcTemplate.update(sql,
            detail.getOrderId(),
            detail.getProductId(),
            detail.getQuantity(),
            detail.getUnitPrice(),
            detail.isActive(),
            Timestamp.valueOf(detail.getUpdatedAt()),
            detail.getId()
        );
        
        return detail;
    }
    
    public void delete(Long id) {
        String sql = """
            UPDATE order_details SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
        jdbcTemplate.update(sql, id);
    }
    
    public void deleteByOrderId(Long orderId) {
        String sql = """
            UPDATE order_details SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE order_id = ?
            """;
        jdbcTemplate.update(sql, orderId);
    }
    
    public boolean existsById(Long id) {
        String sql = """
            SELECT COUNT(*) FROM order_details
            WHERE id = ? AND active = true
            """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null ? count > 0 : false;
    }
}
