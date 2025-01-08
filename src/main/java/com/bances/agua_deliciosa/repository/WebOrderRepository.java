package com.bances.agua_deliciosa.repository;

import com.bances.agua_deliciosa.model.WebOrder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class WebOrderRepository implements BaseRepository<WebOrder> {
    private final JdbcTemplate jdbcTemplate;

    public WebOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public WebOrder save(WebOrder webOrder) {
        if (webOrder.getId() == null) {
            String sql = "INSERT INTO web_orders (delivery_employee_id) VALUES (?)";
            jdbcTemplate.update(sql, webOrder.getDeliveryEmployeeId());
        } else {
            String sql = "UPDATE web_orders SET delivery_employee_id = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                webOrder.getDeliveryEmployeeId(),
                webOrder.getId()
            );
        }
        return webOrder;
    }

    @Override
    public Optional<WebOrder> findById(Long id) {
        String sql = "SELECT * FROM web_orders WHERE id = ?";
        try {
            WebOrder webOrder = jdbcTemplate.queryForObject(sql, this::mapRowToWebOrder, id);
            return Optional.ofNullable(webOrder);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<WebOrder> findAll() {
        String sql = "SELECT * FROM web_orders";
        return jdbcTemplate.query(sql, this::mapRowToWebOrder);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM web_orders WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM web_orders WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM web_orders";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    private WebOrder mapRowToWebOrder(ResultSet rs, int rowNum) throws SQLException {
        WebOrder webOrder = new WebOrder();
        webOrder.setId(rs.getLong("id"));
        webOrder.setDeliveryEmployeeId(rs.getLong("delivery_employee_id"));
        webOrder.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        webOrder.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return webOrder;
    }

    public List<WebOrder> findByDeliveryEmployeeId(Long employeeId) {
        String sql = "SELECT * FROM web_orders WHERE delivery_employee_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToWebOrder, employeeId);
    }
}
