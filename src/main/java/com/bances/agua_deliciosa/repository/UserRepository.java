package com.bances.agua_deliciosa.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bances.agua_deliciosa.model.Gender;
import com.bances.agua_deliciosa.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setDocumentNumber(rs.getString("document_number"));
        user.setPassword(rs.getString("password"));
        try {
            user.setGender(Gender.valueOf(rs.getString("gender")));
        } catch (IllegalArgumentException e) {
            user.setGender(Gender.UNKNOWN);
        }
        user.setBirthDate(rs.getDate("birth_date").toLocalDate());
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setUserableType(rs.getString("userable_type"));
        user.setUserableId(rs.getLong("userable_id"));
        user.setRoleId(rs.getLong("role_id"));
        user.setActive(rs.getBoolean("active"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return user;
    };

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userMapper);
    }

    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, userMapper, id);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        List<User> users = jdbcTemplate.query(sql, userMapper, email);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public boolean existsByEmailAndIdNot(String email, Long id) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND id != ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email, id);
        return count != null && count > 0;
    }

    public User save(User user) {
        if (user.getId() == null) {
            String sql = """
                INSERT INTO users (name, last_name, email, document_number, password, gender, 
                birth_date, phone_number, userable_type, userable_id, role_id, active, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
            jdbcTemplate.update(sql,
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getDocumentNumber(),
                user.getPassword(),
                user.getGender().toString(),
                user.getBirthDate(),
                user.getPhoneNumber(),
                user.getUserableType(),
                user.getUserableId(),
                user.getRoleId(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt());
            Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
            user.setId(id);
        } else {
            String sql = """
                UPDATE users SET 
                name = ?, last_name = ?, email = ?, document_number = ?, password = ?, 
                gender = ?, birth_date = ?, phone_number = ?, userable_type = ?, 
                userable_id = ?, role_id = ?, active = ?, updated_at = ?
                WHERE id = ?
            """;
            jdbcTemplate.update(sql,
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getDocumentNumber(),
                user.getPassword(),
                user.getGender().toString(),
                user.getBirthDate(),
                user.getPhoneNumber(),
                user.getUserableType(),
                user.getUserableId(),
                user.getRoleId(),
                user.isActive(),
                user.getUpdatedAt(),
                user.getId());
        }
        return user;
    }

    public long count() {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Long.class);
        return count != null ? count : 0L;
    }

    public void deleteById(Long id) {
        if (id != null) {
            String sql = "DELETE FROM users WHERE id = ?";
            jdbcTemplate.update(sql, id);
        }
    }

    public List<User> findByUserableType(String userableType) {
        String sql = "SELECT * FROM users WHERE userable_type = ?";
        return jdbcTemplate.query(sql, userMapper, userableType);
    }

    public long countByUserableType(String userableType) {
        String sql = "SELECT COUNT(*) FROM users WHERE userable_type = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, userableType);
        return count != null ? count : 0L;
    }

    public List<User> findTopByOrders(int limit) {
        String sql = """
            SELECT u.* FROM users u 
            LEFT JOIN orders o ON u.id = o.user_id 
            WHERE u.userable_type = 'Client' 
            GROUP BY u.id 
            ORDER BY COUNT(o.id) DESC 
            LIMIT ?
        """;
        return jdbcTemplate.query(sql, userMapper, limit);
    }
}
