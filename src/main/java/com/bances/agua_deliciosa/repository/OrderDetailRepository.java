package com.bances.agua_deliciosa.repository;

import com.bances.agua_deliciosa.model.OrderDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDetailRepository implements BaseRepository<OrderDetail> {
    private final JdbcTemplate jdbcTemplate;

    public OrderDetailRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public OrderDetail save(OrderDetail orderDetail) {
        if (orderDetail.getId() == null) {
            String sql = """
                INSERT INTO order_details (order_id, product_id, quantity, unit_price) 
                VALUES (?, ?, ?, ?)
            """;
            jdbcTemplate.update(sql,
                orderDetail.getOrderId(),
                orderDetail.getProductId(),
                orderDetail.getQuantity(),
                orderDetail.getUnitPrice()
            );
        } else {
            String sql = """
                UPDATE order_details SET order_id = ?, product_id = ?, 
                quantity = ?, unit_price = ? WHERE id = ?
            """;
            jdbcTemplate.update(sql,
                orderDetail.getOrderId(),
                orderDetail.getProductId(),
                orderDetail.getQuantity(),
                orderDetail.getUnitPrice(),
                orderDetail.getId()
            );
        }
        return orderDetail;
    }

    @Override
    public Optional<OrderDetail> findById(Long id) {
        String sql = "SELECT * FROM order_details WHERE id = ?";
        try {
            OrderDetail orderDetail = jdbcTemplate.queryForObject(sql, this::mapRowToOrderDetail, id);
            return Optional.ofNullable(orderDetail);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<OrderDetail> findAll() {
        String sql = "SELECT * FROM order_details";
        return jdbcTemplate.query(sql, this::mapRowToOrderDetail);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM order_details WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM order_details WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM order_details";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    private OrderDetail mapRowToOrderDetail(ResultSet rs, int rowNum) throws SQLException {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(rs.getLong("id"));
        orderDetail.setOrderId(rs.getLong("order_id"));
        orderDetail.setProductId(rs.getLong("product_id"));
        orderDetail.setQuantity(rs.getInt("quantity"));
        orderDetail.setUnitPrice(rs.getBigDecimal("unit_price"));
        orderDetail.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        orderDetail.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return orderDetail;
    }

    public List<OrderDetail> findByOrderId(Long orderId) {
        String sql = "SELECT * FROM order_details WHERE order_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToOrderDetail, orderId);
    }

    public List<OrderDetail> findByProductId(Long productId) {
        String sql = "SELECT * FROM order_details WHERE product_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToOrderDetail, productId);
    }
}
