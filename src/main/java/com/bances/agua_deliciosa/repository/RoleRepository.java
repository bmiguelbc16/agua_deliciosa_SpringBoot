package com.bances.agua_deliciosa.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import com.bances.agua_deliciosa.model.Role;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class RoleRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Role> roleMapper = (rs, rowNum) -> {
        Role role = new Role();
        role.setId(rs.getLong("id"));
        role.setName(rs.getString("name"));
        role.setDescription(rs.getString("description"));
        role.setActive(rs.getBoolean("active"));
        role.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        role.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return role;
    };
    
    public RoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<Role> findAll() {
        String sql = """
            SELECT * FROM roles
            WHERE active = true
            ORDER BY name
            """;
        return jdbcTemplate.query(sql, roleMapper);
    }
    
    public Optional<Role> findById(Long id) {
        String sql = """
            SELECT * FROM roles
            WHERE id = ? AND active = true
            """;
        try {
            Role role = jdbcTemplate.queryForObject(sql, roleMapper, id);
            return Optional.ofNullable(role);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public Optional<Role> findByName(String name) {
        String sql = """
            SELECT * FROM roles
            WHERE name = ? AND active = true
            """;
        try {
            Role role = jdbcTemplate.queryForObject(sql, roleMapper, name);
            return Optional.ofNullable(role);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public Role save(Role role) {
        if (role.getId() == null) {
            return insert(role);
        }
        return update(role);
    }
    
    private Role insert(Role role) {
        String sql = """
            INSERT INTO roles (
                name, description, active,
                created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?)
            """;
            
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, role.getName());
            ps.setString(2, role.getDescription());
            ps.setBoolean(3, role.isActive());
            ps.setTimestamp(4, Timestamp.valueOf(role.getCreatedAt()));
            ps.setTimestamp(5, Timestamp.valueOf(role.getUpdatedAt()));
            return ps;
        }, keyHolder);
        
        Number key = keyHolder.getKey();
        if (key != null) {
            role.setId(key.longValue());
        }
        return role;
    }
    
    private Role update(Role role) {
        String sql = """
            UPDATE roles SET
                name = ?, description = ?,
                active = ?, updated_at = ?
            WHERE id = ?
            """;
            
        jdbcTemplate.update(sql,
            role.getName(),
            role.getDescription(),
            role.isActive(),
            Timestamp.valueOf(role.getUpdatedAt()),
            role.getId()
        );
        
        return role;
    }
    
    public void delete(Long id) {
        String sql = """
            UPDATE roles SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
        jdbcTemplate.update(sql, id);
    }
    
    public boolean existsById(Long id) {
        String sql = """
            SELECT COUNT(*) FROM roles
            WHERE id = ? AND active = true
            """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
    
    public boolean existsByName(String name) {
        String sql = """
            SELECT COUNT(*) FROM roles
            WHERE name = ? AND active = true
            """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }
    
    public int countByPermissionId(Long permissionId) {
        String sql = "SELECT COUNT(*) FROM role_permissions WHERE permission_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, permissionId);
        return count != null ? count : 0;
    }
}