package com.bances.agua_deliciosa.repository;

import com.bances.agua_deliciosa.model.StoreOrder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class StoreOrderRepository implements BaseRepository<StoreOrder> {
    private final JdbcTemplate jdbcTemplate;

    public StoreOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public StoreOrder save(StoreOrder storeOrder) {
        if (storeOrder.getId() == null) {
            String sql = """
                INSERT INTO store_orders (seller_employee_id, delivery_employee_id) 
                VALUES (?, ?)
            """;
            jdbcTemplate.update(sql,
                storeOrder.getSellerEmployeeId(),
                storeOrder.getDeliveryEmployeeId()
            );
        } else {
            String sql = """
                UPDATE store_orders SET seller_employee_id = ?, 
                delivery_employee_id = ? WHERE id = ?
            """;
            jdbcTemplate.update(sql,
                storeOrder.getSellerEmployeeId(),
                storeOrder.getDeliveryEmployeeId(),
                storeOrder.getId()
            );
        }
        return storeOrder;
    }

    @Override
    public Optional<StoreOrder> findById(Long id) {
        String sql = "SELECT * FROM store_orders WHERE id = ?";
        try {
            StoreOrder storeOrder = jdbcTemplate.queryForObject(sql, this::mapRowToStoreOrder, id);
            return Optional.ofNullable(storeOrder);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<StoreOrder> findAll() {
        String sql = "SELECT * FROM store_orders";
        return jdbcTemplate.query(sql, this::mapRowToStoreOrder);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM store_orders WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM store_orders WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM store_orders";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    private StoreOrder mapRowToStoreOrder(ResultSet rs, int rowNum) throws SQLException {
        StoreOrder storeOrder = new StoreOrder();
        storeOrder.setId(rs.getLong("id"));
        storeOrder.setSellerEmployeeId(rs.getLong("seller_employee_id"));
        storeOrder.setDeliveryEmployeeId(rs.getLong("delivery_employee_id"));
        storeOrder.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        storeOrder.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return storeOrder;
    }

    public List<StoreOrder> findBySellerEmployeeId(Long employeeId) {
        String sql = "SELECT * FROM store_orders WHERE seller_employee_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToStoreOrder, employeeId);
    }

    public List<StoreOrder> findByDeliveryEmployeeId(Long employeeId) {
        String sql = "SELECT * FROM store_orders WHERE delivery_employee_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToStoreOrder, employeeId);
    }
}
