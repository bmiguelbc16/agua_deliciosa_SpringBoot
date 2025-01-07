package com.bances.agua_deliciosa.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import com.bances.agua_deliciosa.model.UserVerification;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class UserVerificationRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<UserVerification> userVerificationMapper = (rs, rowNum) -> {
        UserVerification verification = new UserVerification();
        verification.setId(rs.getLong("id"));
        verification.setUserId(rs.getLong("user_id"));
        verification.setToken(rs.getString("token"));
        verification.setExpiresAt(rs.getTimestamp("expires_at").toLocalDateTime());
        verification.setVerifiedAt(rs.getTimestamp("verified_at") != null ? 
            rs.getTimestamp("verified_at").toLocalDateTime() : null);
        verification.setActive(rs.getBoolean("active"));
        verification.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        verification.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return verification;
    };
    
    public UserVerificationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public Optional<UserVerification> findByToken(String token) {
        String sql = """
            SELECT * FROM user_verifications
            WHERE token = ? AND active = true
            AND verified_at IS NULL
            AND expires_at > CURRENT_TIMESTAMP
            """;
        try {
            UserVerification verification = jdbcTemplate.queryForObject(sql, userVerificationMapper, token);
            return Optional.ofNullable(verification);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public List<UserVerification> findByUserId(Long userId) {
        String sql = """
            SELECT * FROM user_verifications
            WHERE user_id = ? AND active = true
            ORDER BY created_at DESC
            """;
        return jdbcTemplate.query(sql, userVerificationMapper, userId);
    }
    
    public Optional<UserVerification> findById(Long id) {
        String sql = """
            SELECT * FROM user_verifications
            WHERE id = ? AND active = true
            """;
        try {
            UserVerification verification = jdbcTemplate.queryForObject(sql, userVerificationMapper, id);
            return Optional.ofNullable(verification);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public UserVerification save(UserVerification verification) {
        if (verification.getId() == null) {
            return insert(verification);
        }
        return update(verification);
    }
    
    private UserVerification insert(UserVerification verification) {
        String sql = """
            INSERT INTO user_verifications (
                user_id, token, expires_at,
                verified_at, active,
                created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
            
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, verification.getUserId());
            ps.setString(2, verification.getToken());
            ps.setTimestamp(3, Timestamp.valueOf(verification.getExpiresAt()));
            if (verification.getVerifiedAt() != null) {
                ps.setTimestamp(4, Timestamp.valueOf(verification.getVerifiedAt()));
            } else {
                ps.setNull(4, java.sql.Types.TIMESTAMP);
            }
            ps.setBoolean(5, verification.isActive());
            ps.setTimestamp(6, Timestamp.valueOf(verification.getCreatedAt()));
            ps.setTimestamp(7, Timestamp.valueOf(verification.getUpdatedAt()));
            return ps;
        }, keyHolder);
        
        // Obtener el ID generado y asignarlo a la verificación
        Number key = keyHolder.getKey();
        if (key != null) {
            verification.setId(key.longValue());
        }
        return verification;
    }
    
    private UserVerification update(UserVerification verification) {
        String sql = """
            UPDATE user_verifications SET
                user_id = ?, token = ?,
                expires_at = ?, verified_at = ?,
                active = ?, updated_at = ?
            WHERE id = ?
            """;
            
        jdbcTemplate.update(sql,
            verification.getUserId(),
            verification.getToken(),
            Timestamp.valueOf(verification.getExpiresAt()),
            verification.getVerifiedAt() != null ? 
                Timestamp.valueOf(verification.getVerifiedAt()) : null,
            verification.isActive(),
            Timestamp.valueOf(verification.getUpdatedAt()),
            verification.getId()
        );
        
        return verification;
    }
    
    public void delete(Long id) {
        String sql = """
            UPDATE user_verifications SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
        jdbcTemplate.update(sql, id);
    }
    
    public void deleteByUserId(Long userId) {
        String sql = """
            UPDATE user_verifications SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE user_id = ?
            """;
        jdbcTemplate.update(sql, userId);
    }
    
    public void deleteExpired() {
        String sql = """
            UPDATE user_verifications SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE verified_at IS NULL
            AND expires_at <= CURRENT_TIMESTAMP
            AND active = true
            """;
        jdbcTemplate.update(sql);
    }
    
    public boolean existsById(Long id) {
        String sql = """
            SELECT COUNT(*) FROM user_verifications
            WHERE id = ? AND active = true
            """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null ? count > 0 : false;
    }
    
    public boolean existsByToken(String token) {
        String sql = """
            SELECT COUNT(*) FROM user_verifications
            WHERE token = ? AND active = true
            AND verified_at IS NULL
            AND expires_at > CURRENT_TIMESTAMP
            """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, token);
        return count != null ? count > 0 : false;
    }
}
