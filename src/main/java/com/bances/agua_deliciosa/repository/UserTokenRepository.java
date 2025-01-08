package com.bances.agua_deliciosa.repository;

import com.bances.agua_deliciosa.model.UserToken;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class UserTokenRepository implements BaseRepository<UserToken> {
    private final JdbcTemplate jdbcTemplate;

    public UserTokenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserToken save(UserToken userToken) {
        if (userToken.getId() == null) {
            String sql = """
                INSERT INTO user_tokens (user_id, token, expires_at, created_at, updated_at) 
                VALUES (?, ?, ?, ?, ?)
            """;
            jdbcTemplate.update(sql,
                userToken.getUserId(),
                userToken.getToken(),
                userToken.getExpiresAt(),
                userToken.getCreatedAt(),
                userToken.getUpdatedAt()
            );
        } else {
            String sql = """
                UPDATE user_tokens SET user_id = ?, token = ?, 
                expires_at = ?, updated_at = ? WHERE id = ?
            """;
            jdbcTemplate.update(sql,
                userToken.getUserId(),
                userToken.getToken(),
                userToken.getExpiresAt(),
                LocalDateTime.now(),
                userToken.getId()
            );
        }
        return userToken;
    }

    @Override
    public Optional<UserToken> findById(Long id) {
        String sql = "SELECT * FROM user_tokens WHERE id = ?";
        try {
            UserToken userToken = jdbcTemplate.queryForObject(sql, this::mapRowToUserToken, id);
            return Optional.ofNullable(userToken);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<UserToken> findAll() {
        String sql = "SELECT * FROM user_tokens";
        return jdbcTemplate.query(sql, this::mapRowToUserToken);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM user_tokens WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM user_tokens WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM user_tokens";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    private UserToken mapRowToUserToken(ResultSet rs, int rowNum) throws SQLException {
        UserToken userToken = new UserToken();
        userToken.setId(rs.getLong("id"));
        userToken.setUserId(rs.getLong("user_id"));
        userToken.setToken(rs.getString("token"));
        userToken.setExpiresAt(rs.getTimestamp("expires_at").toLocalDateTime());
        userToken.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        userToken.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return userToken;
    }

    public Optional<UserToken> findByToken(String token) {
        String sql = "SELECT * FROM user_tokens WHERE token = ?";
        try {
            UserToken userToken = jdbcTemplate.queryForObject(sql, this::mapRowToUserToken, token);
            return Optional.ofNullable(userToken);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<UserToken> findByUserId(Long userId) {
        String sql = "SELECT * FROM user_tokens WHERE user_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToUserToken, userId);
    }

    public void deleteExpiredTokens() {
        String sql = "DELETE FROM user_tokens WHERE expires_at < NOW()";
        jdbcTemplate.update(sql);
    }
}