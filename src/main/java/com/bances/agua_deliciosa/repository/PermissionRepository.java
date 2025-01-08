package com.bances.agua_deliciosa.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bances.agua_deliciosa.model.Permission;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class PermissionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Permission> permissionMapper = (rs, rowNum) -> {
        Permission permission = new Permission();
        permission.setId(rs.getLong("id"));
        permission.setName(rs.getString("name"));
        permission.setDescription(rs.getString("description"));
        permission.setGuardName(rs.getString("guard_name"));
        permission.setActive(rs.getBoolean("active"));
        permission.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        permission.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return permission;
    };

    public PermissionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Permission> findAll() {
        String sql = "SELECT * FROM permissions";
        return jdbcTemplate.query(sql, permissionMapper);
    }

    public Optional<Permission> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        String sql = "SELECT * FROM permissions WHERE id = ?";
        List<Permission> permissions = jdbcTemplate.query(sql, permissionMapper, id);
        return permissions.isEmpty() ? Optional.empty() : Optional.of(permissions.get(0));
    }

    public Optional<Permission> findByName(String name) {
        String sql = "SELECT * FROM permissions WHERE name = ?";
        List<Permission> permissions = jdbcTemplate.query(sql, permissionMapper, name);
        return permissions.isEmpty() ? Optional.empty() : Optional.of(permissions.get(0));
    }

    public Permission save(Permission permission) {
        if (permission.getId() == null) {
            // Para nuevos permisos, primero obtenemos el siguiente ID
            Long nextId = jdbcTemplate.queryForObject(
                "SELECT COALESCE(MAX(id), 0) + 1 FROM permissions", Long.class);
            permission.setId(nextId);
            
            String sql = "INSERT INTO permissions (id, name, description, guard_name, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, 
                permission.getId(),
                permission.getName(), 
                permission.getDescription(), 
                permission.getGuardName(),
                permission.getCreatedAt(),
                permission.getUpdatedAt());
        } else {
            String sql = "UPDATE permissions SET name = ?, description = ?, guard_name = ?, updated_at = ? WHERE id = ?";
            jdbcTemplate.update(sql, 
                permission.getName(), 
                permission.getDescription(), 
                permission.getGuardName(),
                permission.getUpdatedAt(),
                permission.getId());
        }
        return permission;
    }

    public void saveAll(List<Permission> permissions) {
        permissions.forEach(this::save);
    }

    public long count() {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM permissions", Long.class);
        return count != null ? count : 0L;
    }

    public Set<Permission> findByRoleId(Long roleId) {
        if (roleId == null) {
            return Set.of();
        }
        String sql = """
            SELECT p.* FROM permissions p 
            INNER JOIN role_has_permissions rhp ON p.id = rhp.permission_id 
            WHERE rhp.role_id = ?
        """;
        return Set.copyOf(jdbcTemplate.query(sql, permissionMapper, roleId));
    }

    public void deleteById(Long id) {
        if (id != null) {
            String sql = "DELETE FROM permissions WHERE id = ?";
            jdbcTemplate.update(sql, id);
        }
    }
}
