package com.bances.agua_deliciosa.repository;

import com.bances.agua_deliciosa.model.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository implements BaseRepository<Order> {
    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order save(Order order) {
        if (order.getId() == null) {
            String sql = """
                INSERT INTO orders (customer_id, orderable_type, orderable_id, 
                delivery_date, delivery_address) 
                VALUES (?, ?, ?, ?, ?)
            """;
            jdbcTemplate.update(sql,
                order.getCustomerId(),
                order.getOrderableType(),
                order.getOrderableId(),
                order.getDeliveryDate(),
                order.getDeliveryAddress()
            );
        } else {
            String sql = """
                UPDATE orders SET customer_id = ?, orderable_type = ?, 
                orderable_id = ?, delivery_date = ?, delivery_address = ? 
                WHERE id = ?
            """;
            jdbcTemplate.update(sql,
                order.getCustomerId(),
                order.getOrderableType(),
                order.getOrderableId(),
                order.getDeliveryDate(),
                order.getDeliveryAddress(),
                order.getId()
            );
        }
        return order;
    }

    @Override
    public Optional<Order> findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try {
            Order order = jdbcTemplate.queryForObject(sql, this::mapRowToOrder, id);
            return Optional.ofNullable(order);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, this::mapRowToOrder);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM orders WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM orders";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    private Order mapRowToOrder(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setCustomerId(rs.getLong("customer_id"));
        order.setOrderableType(rs.getString("orderable_type"));
        order.setOrderableId(rs.getLong("orderable_id"));
        order.setDeliveryAddress(rs.getString("delivery_address"));
        order.setDeliveryDate(rs.getDate("delivery_date").toLocalDate());
        order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        order.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return order;
    }

    public List<Order> findByCustomerId(Long customerId) {
        String sql = "SELECT * FROM orders WHERE customer_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToOrder, customerId);
    }

    public List<Order> findByDeliveryEmployeeId(Long employeeId) {
        String sql = """
            SELECT o.* FROM orders o 
            LEFT JOIN store_orders so ON o.orderable_id = so.id AND o.orderable_type = 'StoreOrder'
            LEFT JOIN web_orders wo ON o.orderable_id = wo.id AND o.orderable_type = 'WebOrder'
            WHERE so.delivery_employee_id = ? OR wo.delivery_employee_id = ?
        """;
        return jdbcTemplate.query(sql, this::mapRowToOrder, employeeId, employeeId);
    }

    public List<Order> findTopByOrderByCreatedAtDesc(int limit) {
        String sql = "SELECT * FROM orders ORDER BY created_at DESC LIMIT ?";
        return jdbcTemplate.query(sql, this::mapRowToOrder, limit);
    }
}
