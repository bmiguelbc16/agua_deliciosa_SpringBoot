package com.bances.agua_deliciosa.repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bances.agua_deliciosa.model.Order;
import com.bances.agua_deliciosa.model.OrderStatus;

@Repository
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Order> orderMapper = (ResultSet rs, int rowNum) -> {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setCode(rs.getString("code"));
        order.setStatus(OrderStatus.valueOf(rs.getString("status")));
        order.setTotal(rs.getBigDecimal("total"));
        order.setActive(rs.getBoolean("active"));
        return order;
    };

    public Optional<Order> findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ? AND active = true";
        List<Order> orders = jdbcTemplate.query(sql, orderMapper, id);
        return orders.isEmpty() ? Optional.empty() : Optional.of(orders.get(0));
    }

    public List<Order> findAll() {
        String sql = "SELECT * FROM orders WHERE active = true";
        return jdbcTemplate.query(sql, orderMapper);
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM orders WHERE active = true";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }

    public long countByStatus(OrderStatus status) {
        String sql = "SELECT COUNT(*) FROM orders WHERE status = ? AND active = true";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, status.name());
        return count != null ? count : 0L;
    }

    public BigDecimal calculateTotalSales() {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM orders WHERE status = ? AND active = true";
        BigDecimal total = jdbcTemplate.queryForObject(sql, BigDecimal.class, OrderStatus.COMPLETED.name());
        return total != null ? total : BigDecimal.ZERO;
    }
}