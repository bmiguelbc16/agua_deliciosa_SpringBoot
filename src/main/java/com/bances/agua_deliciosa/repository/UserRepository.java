package com.bances.agua_deliciosa.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.bances.agua_deliciosa.model.User;

@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private final RowMapper<User> userMapper = new RowMapper<User>() {
        @Override
        public User mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setFirstName(rs.getString("name"));
            user.setLastName(rs.getString("last_name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setActive(rs.getBoolean("active"));
            return user;
        }
    };
    
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ? AND active = true";
        List<User> users = jdbcTemplate.query(sql, userMapper, email);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
    
    public boolean existsByEmailAndIdNot(String email, Long id) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND id != ? AND active = true";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email, id);
        return count != null && count > 0;
    }
    
    public User save(User user) {
        if (user.getId() == null) {
            return insert(user);
        }
        return update(user);
    }
    
    private User insert(User user) {
        String sql = "INSERT INTO users (username, name, last_name, email, password, phone_number, active) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        jdbcTemplate.update(sql,
            user.getUsername(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPassword(),
            user.getPhoneNumber(),
            true
        );
        
        String idSql = "SELECT LAST_INSERT_ID()";
        Long id = jdbcTemplate.queryForObject(idSql, Long.class);
        user.setId(id);
        
        return user;
    }
    
    private User update(User user) {
        String sql = "UPDATE users SET username = ?, name = ?, last_name = ?, email = ?, " +
                    "phone_number = ?, active = ? WHERE id = ?";
        
        jdbcTemplate.update(sql,
            user.getUsername(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.isActive(),
            user.getId()
        );
        
        return user;
    }
    
    public void delete(Long id) {
        String sql = "UPDATE users SET active = false WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ? AND active = true";
        List<User> users = jdbcTemplate.query(sql, userMapper, id);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
    
    public List<User> findAll() {
        String sql = "SELECT * FROM users WHERE active = true";
        return jdbcTemplate.query(sql, userMapper);
    }
    
    public int countByRoleId(Long roleId) {
        String sql = "SELECT COUNT(*) FROM users WHERE role_id = ? AND active = true";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, roleId);
        return count != null ? count : 0;
    }
}