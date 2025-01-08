package com.bances.agua_deliciosa.repository;

import com.bances.agua_deliciosa.model.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class RoleRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Role> roleMapper = (rs, rowNum) -> {
        Role role = new Role();
        role.setId(rs.getLong("id"));
        role.setName(rs.getString("name"));
        role.setDescription(rs.getString("description"));
        role.setGuardName(rs.getString("guard_name"));
        role.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        role.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return role;
    };

    public RoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Role> findAll() {
        String sql = "SELECT * FROM roles";
        return jdbcTemplate.query(sql, roleMapper);
    }

    public Optional<Role> findById(Long id) {
        String sql = "SELECT * FROM roles WHERE id = ?";
        List<Role> roles = jdbcTemplate.query(sql, roleMapper, id);
        return roles.isEmpty() ? Optional.empty() : Optional.of(roles.get(0));
    }

    public Optional<Role> findByName(String name) {
        String sql = "SELECT * FROM roles WHERE name = ?";
        List<Role> roles = jdbcTemplate.query(sql, roleMapper, name);
        return roles.isEmpty() ? Optional.empty() : Optional.of(roles.get(0));
    }

    public Role save(Role role) {
        if (role.getId() == null) {
            // Para nuevos roles, primero obtenemos el siguiente ID
            Long nextId = jdbcTemplate.queryForObject(
                "SELECT COALESCE(MAX(id), 0) + 1 FROM roles", Long.class);
            role.setId(nextId);
            
            String sql = "INSERT INTO roles (id, name, description, guard_name, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, 
                role.getId(),
                role.getName(), 
                role.getDescription(), 
                role.getGuardName(),
                role.getCreatedAt(),
                role.getUpdatedAt());
        } else {
            String sql = "UPDATE roles SET name = ?, description = ?, guard_name = ?, updated_at = ? WHERE id = ?";
            jdbcTemplate.update(sql, 
                role.getName(), 
                role.getDescription(), 
                role.getGuardName(),
                role.getUpdatedAt(),
                role.getId());
        }
        return role;
    }

    public void saveAll(List<Role> roles) {
        roles.forEach(this::save);
    }

    public long count() {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM roles", Long.class);
        return count != null ? count : 0L;
    }

    public void addPermissions(Long roleId, Set<Long> permissionIds) {
        String sql = "INSERT INTO role_has_permissions (role_id, permission_id) VALUES (?, ?)";
        permissionIds.forEach(permissionId -> 
            jdbcTemplate.update(sql, roleId, permissionId));
    }

    public void clearPermissions(Long roleId) {
        String sql = "DELETE FROM role_has_permissions WHERE role_id = ?";
        jdbcTemplate.update(sql, roleId);
    }

    public void deleteById(Long id) {
        if (id != null) {
            String sql = "DELETE FROM roles WHERE id = ?";
            jdbcTemplate.update(sql, id);
        }
    }

    public void assignPermissionToRole(Long roleId, Long permissionId) {
        String sql = "INSERT INTO role_has_permissions (role_id, permission_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, roleId, permissionId);
    }

    public void removePermissionFromRole(Long roleId, Long permissionId) {
        String sql = "DELETE FROM role_has_permissions WHERE role_id = ? AND permission_id = ?";
        jdbcTemplate.update(sql, roleId, permissionId);
    }

    public List<Role> findByUserId(Long userId) {
        String sql = """
            SELECT r.* FROM roles r 
            INNER JOIN model_has_roles mhr ON r.id = mhr.role_id 
            WHERE mhr.model_id = ? AND mhr.model_type = 'User'
        """;
        return jdbcTemplate.query(sql, roleMapper, userId);
    }

    public List<Role> findByIds(List<Long> ids) {
        String sql = "SELECT * FROM roles WHERE id IN (" + String.join(",", ids.stream().map(String::valueOf).toArray(String[]::new)) + ")";
        return jdbcTemplate.query(sql, roleMapper);
    }

    public List<String> findRoleNamesByUserId(Long userId) {
        String sql = """
            SELECT r.name 
            FROM roles r 
            INNER JOIN model_has_roles mhr ON r.id = mhr.role_id 
            WHERE mhr.model_id = ? AND mhr.model_type = 'User'
        """;
        return jdbcTemplate.queryForList(sql, String.class, userId);
    }
}
