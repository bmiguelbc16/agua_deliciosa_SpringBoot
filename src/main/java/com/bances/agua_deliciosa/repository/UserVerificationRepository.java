package com.bances.agua_deliciosa.repository;

import com.bances.agua_deliciosa.model.UserVerification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserVerificationRepository implements BaseRepository<UserVerification> {
    private final JdbcTemplate jdbcTemplate;

    public UserVerificationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserVerification save(UserVerification verification) {
        if (verification.getId() == null) {
            String sql = """
                INSERT INTO user_verifications (user_id, token, verification_type, 
                expiry_date, verified) VALUES (?, ?, ?, ?, ?)
            """;
            jdbcTemplate.update(sql,
                verification.getUserId(),
                verification.getToken(),
                verification.getVerificationType(),
                verification.getExpiryDate(),
                verification.isVerified()
            );
        } else {
            String sql = """
                UPDATE user_verifications SET user_id = ?, token = ?, 
                verification_type = ?, expiry_date = ?, verified = ? WHERE id = ?
            """;
            jdbcTemplate.update(sql,
                verification.getUserId(),
                verification.getToken(),
                verification.getVerificationType(),
                verification.getExpiryDate(),
                verification.isVerified(),
                verification.getId()
            );
        }
        return verification;
    }

    @Override
    public Optional<UserVerification> findById(Long id) {
        String sql = "SELECT * FROM user_verifications WHERE id = ?";
        try {
            UserVerification verification = jdbcTemplate.queryForObject(sql, this::mapRowToVerification, id);
            return Optional.ofNullable(verification);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<UserVerification> findAll() {
        String sql = "SELECT * FROM user_verifications";
        return jdbcTemplate.query(sql, this::mapRowToVerification);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM user_verifications WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM user_verifications WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM user_verifications";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    private UserVerification mapRowToVerification(ResultSet rs, int rowNum) throws SQLException {
        UserVerification verification = new UserVerification();
        verification.setId(rs.getLong("id"));
        verification.setUserId(rs.getLong("user_id"));
        verification.setToken(rs.getString("token"));
        verification.setVerificationType(rs.getString("verification_type"));
        verification.setExpiryDate(rs.getTimestamp("expiry_date").toLocalDateTime());
        verification.setVerified(rs.getBoolean("verified"));
        verification.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        verification.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return verification;
    }

    public Optional<UserVerification> findByToken(String token) {
        String sql = "SELECT * FROM user_verifications WHERE token = ?";
        try {
            UserVerification verification = jdbcTemplate.queryForObject(sql, this::mapRowToVerification, token);
            return Optional.ofNullable(verification);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<UserVerification> findByUserId(Long userId) {
        String sql = "SELECT * FROM user_verifications WHERE user_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToVerification, userId);
    }

    public void deleteExpiredTokens() {
        String sql = "DELETE FROM user_verifications WHERE expiry_date < NOW()";
        jdbcTemplate.update(sql);
    }
}
