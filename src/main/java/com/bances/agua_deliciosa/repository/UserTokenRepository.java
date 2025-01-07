package com.bances.agua_deliciosa.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import com.bances.agua_deliciosa.model.UserToken;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class UserTokenRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<UserToken> userTokenMapper = (rs, rowNum) -> {
        UserToken token = new UserToken();
        token.setId(rs.getLong("id"));
        token.setUserId(rs.getLong("user_id"));
        token.setToken(rs.getString("token"));
        token.setExpiresAt(rs.getTimestamp("expires_at").toLocalDateTime());
        token.setActive(rs.getBoolean("active"));
        token.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        token.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return token;
    };
    
    public UserTokenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public Optional<UserToken> findByToken(String token) {
        String sql = """
            SELECT * FROM user_tokens
            WHERE token = ? AND active = true
            AND expires_at > CURRENT_TIMESTAMP
            """;
        try {
            UserToken userToken = jdbcTemplate.queryForObject(sql, userTokenMapper, token);
            return Optional.ofNullable(userToken);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public List<UserToken> findByUserId(Long userId) {
        String sql = """
            SELECT * FROM user_tokens
            WHERE user_id = ? AND active = true
            ORDER BY expires_at DESC
            """;
        return jdbcTemplate.query(sql, userTokenMapper, userId);
    }
    
    public Optional<UserToken> findById(Long id) {
        String sql = """
            SELECT * FROM user_tokens
            WHERE id = ? AND active = true
            """;
        try {
            UserToken token = jdbcTemplate.queryForObject(sql, userTokenMapper, id);
            return Optional.ofNullable(token);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public UserToken save(UserToken token) {
        if (token.getId() == null) {
            return insert(token);
        }
        return update(token);
    }
    
    private UserToken insert(UserToken token) {
        String sql = """
            INSERT INTO user_tokens (
                user_id, token, expires_at,
                active, created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?)
            """;
            
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, token.getUserId());
            ps.setString(2, token.getToken());
            ps.setTimestamp(3, Timestamp.valueOf(token.getExpiresAt()));
            ps.setBoolean(4, token.isActive());
            ps.setTimestamp(5, Timestamp.valueOf(token.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(token.getUpdatedAt()));
            return ps;
        }, keyHolder);
        
        // Obtener el ID generado y asignarlo al token
        Number key = keyHolder.getKey();
        if (key != null) {
            token.setId(key.longValue());
        }
        return token;
    }
    
    private UserToken update(UserToken token) {
        String sql = """
            UPDATE user_tokens SET
                user_id = ?, token = ?,
                expires_at = ?, active = ?,
                updated_at = ?
            WHERE id = ?
            """;
            
        jdbcTemplate.update(sql,
            token.getUserId(),
            token.getToken(),
            Timestamp.valueOf(token.getExpiresAt()),
            token.isActive(),
            Timestamp.valueOf(token.getUpdatedAt()),
            token.getId()
        );
        
        return token;
    }
    
    public void delete(Long id) {
        String sql = """
            UPDATE user_tokens SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
        jdbcTemplate.update(sql, id);
    }
    
    public void deleteByUserId(Long userId) {
        String sql = """
            UPDATE user_tokens SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE user_id = ?
            """;
        jdbcTemplate.update(sql, userId);
    }
    
    public void deleteExpired() {
        String sql = """
            UPDATE user_tokens SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE expires_at <= CURRENT_TIMESTAMP
            AND active = true
            """;
        jdbcTemplate.update(sql);
    }
    
    public boolean existsById(Long id) {
        String sql = """
            SELECT COUNT(*) FROM user_tokens
            WHERE id = ? AND active = true
            """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null ? count > 0 : false;
    }
    
    public boolean existsByToken(String token) {
        String sql = """
            SELECT COUNT(*) FROM user_tokens
            WHERE token = ? AND active = true
            AND expires_at > CURRENT_TIMESTAMP
            """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, token);
        return count != null ? count > 0 : false;
    }
    
    public Optional<UserToken> findByUserEmailAndTokenAndExpiresAtAfter(String email, String token, LocalDateTime now) {
        String sql = """
            SELECT ut.* FROM user_tokens ut
            INNER JOIN users u ON u.id = ut.user_id
            WHERE u.email = ? AND ut.token = ? 
            AND ut.expires_at > ? AND ut.active = true
            """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userTokenMapper, email, token, Timestamp.valueOf(now)));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteByExpiresAtBefore(LocalDateTime dateTime) {
        String sql = """
            UPDATE user_tokens SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE expires_at < ?
            """;
        jdbcTemplate.update(sql, Timestamp.valueOf(dateTime));
    }
}
