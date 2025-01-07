package com.bances.agua_deliciosa.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import com.bances.agua_deliciosa.model.Permission;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class PermissionRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Permission> permissionMapper = (rs, rowNum) -> {
        Permission permission = new Permission();
        permission.setId(rs.getLong("id"));
        permission.setName(rs.getString("name"));
        permission.setDescription(rs.getString("description"));
        permission.setActive(rs.getBoolean("active"));
        permission.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        permission.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return permission;
    };
    
    public PermissionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<Permission> findAll() {
        String sql = """
            SELECT * FROM permissions
            WHERE active = true
            ORDER BY name
            """;
        return jdbcTemplate.query(sql, permissionMapper);
    }
    
    public Optional<Permission> findById(Long id) {
        String sql = """
            SELECT * FROM permissions
            WHERE id = ? AND active = true
            """;
        try {
            Permission permission = jdbcTemplate.queryForObject(sql, permissionMapper, id);
            return Optional.ofNullable(permission);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public Optional<Permission> findByName(String name) {
        String sql = """
            SELECT * FROM permissions
            WHERE name = ? AND active = true
            """;
        try {
            Permission permission = jdbcTemplate.queryForObject(sql, permissionMapper, name);
            return Optional.ofNullable(permission);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public List<Permission> findByRoleId(Long roleId) {
        String sql = """
            SELECT p.* FROM permissions p
            INNER JOIN role_permissions rp ON p.id = rp.permission_id
            WHERE rp.role_id = ? AND p.active = true
            ORDER BY p.name
            """;
        return jdbcTemplate.query(sql, permissionMapper, roleId);
    }
    
    public Permission save(Permission permission) {
        if (permission.getId() == null) {
            return insert(permission);
        }
        return update(permission);
    }
    
    private Permission insert(Permission permission) {
        String sql = """
            INSERT INTO permissions (
                name, description, active,
                created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?)
            """;
            
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, permission.getName());
            ps.setString(2, permission.getDescription());
            ps.setBoolean(3, permission.isActive());
            ps.setTimestamp(4, Timestamp.valueOf(permission.getCreatedAt()));
            ps.setTimestamp(5, Timestamp.valueOf(permission.getUpdatedAt()));
            return ps;
        }, keyHolder);
        
        Number key = keyHolder.getKey();
        if (key != null) {
            permission.setId(key.longValue());
        }
        return permission;
    }
    
    private Permission update(Permission permission) {
        String sql = """
            UPDATE permissions SET
                name = ?, description = ?,
                active = ?, updated_at = ?
            WHERE id = ?
            """;
            
        jdbcTemplate.update(sql,
            permission.getName(),
            permission.getDescription(),
            permission.isActive(),
            Timestamp.valueOf(permission.getUpdatedAt()),
            permission.getId()
        );
        
        return permission;
    }
    
    public void delete(Long id) {
        String sql = """
            UPDATE permissions SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
        jdbcTemplate.update(sql, id);
    }
    
    public boolean existsById(Long id) {
        String sql = """
            SELECT COUNT(*) FROM permissions
            WHERE id = ? AND active = true
            """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
    
    public boolean existsByName(String name) {
        String sql = """
            SELECT COUNT(*) FROM permissions
            WHERE name = ? AND active = true
            """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }
}
