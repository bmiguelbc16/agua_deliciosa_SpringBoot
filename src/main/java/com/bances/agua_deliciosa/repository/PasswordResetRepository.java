package com.bances.agua_deliciosa.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import com.bances.agua_deliciosa.model.PasswordReset;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class PasswordResetRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<PasswordReset> passwordResetMapper = (rs, rowNum) -> {
        PasswordReset reset = new PasswordReset();
        reset.setId(rs.getLong("id"));
        reset.setUserId(rs.getLong("user_id"));
        reset.setToken(rs.getString("token"));
        reset.setExpiresAt(rs.getTimestamp("expires_at").toLocalDateTime());
        reset.setResetAt(rs.getTimestamp("reset_at") != null ? 
            rs.getTimestamp("reset_at").toLocalDateTime() : null);
        reset.setActive(rs.getBoolean("active"));
        reset.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        reset.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return reset;
    };
    
    public PasswordResetRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public Optional<PasswordReset> findByToken(String token) {
        String sql = """
            SELECT * FROM password_resets
            WHERE token = ? AND active = true
            AND reset_at IS NULL
            AND expires_at > CURRENT_TIMESTAMP
            """;
        try {
            PasswordReset reset = jdbcTemplate.queryForObject(sql, passwordResetMapper, token);
            return Optional.ofNullable(reset);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public List<PasswordReset> findByUserId(Long userId) {
        String sql = """
            SELECT * FROM password_resets
            WHERE user_id = ? AND active = true
            ORDER BY created_at DESC
            """;
        return jdbcTemplate.query(sql, passwordResetMapper, userId);
    }
    
    public Optional<PasswordReset> findById(Long id) {
        String sql = """
            SELECT * FROM password_resets
            WHERE id = ? AND active = true
            """;
        try {
            PasswordReset reset = jdbcTemplate.queryForObject(sql, passwordResetMapper, id);
            return Optional.ofNullable(reset);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public PasswordReset save(PasswordReset reset) {
        if (reset.getId() == null) {
            return insert(reset);
        }
        return update(reset);
    }
    
    private PasswordReset insert(PasswordReset reset) {
        String sql = """
            INSERT INTO password_resets (
                user_id, token, reset_at,
                active, created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?)
            """;
            
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, reset.getUserId());
            ps.setString(2, reset.getToken());
            ps.setTimestamp(3, reset.getResetAt() != null ? Timestamp.valueOf(reset.getResetAt()) : null);
            ps.setBoolean(4, reset.isActive());
            ps.setTimestamp(5, Timestamp.valueOf(reset.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(reset.getUpdatedAt()));
            return ps;
        }, keyHolder);
        
        Number key = keyHolder.getKey();
        if (key != null) {
            reset.setId(key.longValue());
        }
        return reset;
    }

    private PasswordReset update(PasswordReset reset) {
        String sql = """
            UPDATE password_resets SET
                user_id = ?, token = ?,
                reset_at = ?, active = ?,
                updated_at = ?
            WHERE id = ?
            """;
        
        jdbcTemplate.update(sql,
            reset.getUserId(),
            reset.getToken(),
            Timestamp.valueOf(reset.getResetAt()),
            reset.isActive(),
            Timestamp.valueOf(reset.getUpdatedAt()),
            reset.getId()
        );
        
        return reset;
    }
    
    public void delete(Long id) {
        String sql = """
            UPDATE password_resets SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
        jdbcTemplate.update(sql, id);
    }
    
    public void deleteByUserId(Long userId) {
        String sql = """
            UPDATE password_resets SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE user_id = ?
            """;
        jdbcTemplate.update(sql, userId);
    }
    
    public void deleteExpired() {
        String sql = """
            UPDATE password_resets SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE reset_at IS NULL
            AND expires_at <= CURRENT_TIMESTAMP
            AND active = true
            """;
        jdbcTemplate.update(sql);
    }
    
    public boolean existsById(Long id) {
        String sql = """
            SELECT COUNT(*) FROM password_resets
            WHERE id = ? AND active = true
            """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }

    public boolean existsByToken(String token) {
        String sql = """
            SELECT COUNT(*) FROM password_resets
            WHERE token = ? AND active = true
            """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, token));
    }
}
