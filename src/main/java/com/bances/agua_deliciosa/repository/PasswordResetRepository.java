package com.bances.agua_deliciosa.repository;

import com.bances.agua_deliciosa.model.PasswordReset;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class PasswordResetRepository implements BaseRepository<PasswordReset> {
    private final JdbcTemplate jdbcTemplate;

    public PasswordResetRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public PasswordReset save(PasswordReset passwordReset) {
        if (passwordReset.getId() == null) {
            String sql = """
                INSERT INTO password_resets (user_id, token, expires_at) 
                VALUES (?, ?, ?)
            """;
            jdbcTemplate.update(sql,
                passwordReset.getUserId(),
                passwordReset.getToken(),
                passwordReset.getExpiresAt()
            );
        } else {
            String sql = """
                UPDATE password_resets SET user_id = ?, token = ?, 
                expires_at = ? WHERE id = ?
            """;
            jdbcTemplate.update(sql,
                passwordReset.getUserId(),
                passwordReset.getToken(),
                passwordReset.getExpiresAt(),
                passwordReset.getId()
            );
        }
        return passwordReset;
    }

    @Override
    public Optional<PasswordReset> findById(Long id) {
        String sql = "SELECT * FROM password_resets WHERE id = ?";
        try {
            PasswordReset passwordReset = jdbcTemplate.queryForObject(sql, this::mapRowToPasswordReset, id);
            return Optional.ofNullable(passwordReset);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<PasswordReset> findAll() {
        String sql = "SELECT * FROM password_resets";
        return jdbcTemplate.query(sql, this::mapRowToPasswordReset);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM password_resets WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM password_resets WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM password_resets";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    private PasswordReset mapRowToPasswordReset(ResultSet rs, int rowNum) throws SQLException {
        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setId(rs.getLong("id"));
        passwordReset.setUserId(rs.getLong("user_id"));
        passwordReset.setToken(rs.getString("token"));
        passwordReset.setExpiresAt(rs.getTimestamp("expires_at").toLocalDateTime());
        passwordReset.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        passwordReset.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return passwordReset;
    }

    public Optional<PasswordReset> findByToken(String token) {
        String sql = "SELECT * FROM password_resets WHERE token = ?";
        try {
            PasswordReset passwordReset = jdbcTemplate.queryForObject(sql, this::mapRowToPasswordReset, token);
            return Optional.ofNullable(passwordReset);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<PasswordReset> findByUserId(Long userId) {
        String sql = "SELECT * FROM password_resets WHERE user_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToPasswordReset, userId);
    }

    public void deleteExpiredTokens() {
        String sql = "DELETE FROM password_resets WHERE expires_at < NOW()";
        jdbcTemplate.update(sql);
    }
}
