package com.bances.agua_deliciosa.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import com.bances.agua_deliciosa.model.PaymentProof;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class PaymentProofRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<PaymentProof> paymentProofMapper = (rs, rowNum) -> {
        PaymentProof proof = new PaymentProof();
        proof.setId(rs.getLong("id"));
        proof.setOrderId(rs.getLong("order_id"));
        proof.setAmount(rs.getBigDecimal("amount"));
        proof.setPaymentType(rs.getString("payment_type"));
        proof.setProofImage(rs.getBytes("proof_image"));
        proof.setVerified(rs.getBoolean("verified"));
        proof.setActive(rs.getBoolean("active"));
        proof.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        proof.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return proof;
    };
    
    public PaymentProofRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<PaymentProof> findByOrderId(Long orderId) {
        String sql = """
            SELECT * FROM payment_proofs
            WHERE order_id = ? AND active = true
            ORDER BY created_at DESC
            """;
        return jdbcTemplate.query(sql, paymentProofMapper, orderId);
    }
    
    public Optional<PaymentProof> findById(Long id) {
        String sql = """
            SELECT * FROM payment_proofs
            WHERE id = ? AND active = true
            """;
        try {
            PaymentProof proof = jdbcTemplate.queryForObject(sql, paymentProofMapper, id);
            return Optional.ofNullable(proof);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public PaymentProof save(PaymentProof proof) {
        if (proof.getId() == null) {
            return insert(proof);
        }
        return update(proof);
    }
    
    private PaymentProof insert(PaymentProof proof) {
        String sql = """
            INSERT INTO payment_proofs (
                order_id, amount, payment_type,
                proof_image, verified, active,
                created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
            
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, proof.getOrderId());
            ps.setBigDecimal(2, proof.getAmount());
            ps.setString(3, proof.getPaymentType());
            ps.setBytes(4, proof.getProofImage());
            ps.setBoolean(5, proof.isVerified());
            ps.setBoolean(6, proof.isActive());
            ps.setTimestamp(7, Timestamp.valueOf(proof.getCreatedAt()));
            ps.setTimestamp(8, Timestamp.valueOf(proof.getUpdatedAt()));
            return ps;
        }, keyHolder);
        
        Number key = keyHolder.getKey();
        if (key != null) {
            proof.setId(key.longValue());
        }
        return proof;
    }
    
    private PaymentProof update(PaymentProof proof) {
        String sql = """
            UPDATE payment_proofs SET
                order_id = ?, amount = ?,
                payment_type = ?, proof_image = ?,
                verified = ?, active = ?,
                updated_at = ?
            WHERE id = ?
            """;
            
        jdbcTemplate.update(sql,
            proof.getOrderId(),
            proof.getAmount(),
            proof.getPaymentType(),
            proof.getProofImage(),
            proof.isVerified(),
            proof.isActive(),
            Timestamp.valueOf(proof.getUpdatedAt()),
            proof.getId()
        );
        
        return proof;
    }
    
    public void delete(Long id) {
        String sql = """
            UPDATE payment_proofs SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
        jdbcTemplate.update(sql, id);
    }
    
    public void deleteByOrderId(Long orderId) {
        String sql = """
            UPDATE payment_proofs SET
                active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE order_id = ?
            """;
        jdbcTemplate.update(sql, orderId);
    }
    
    public boolean existsById(Long id) {
        String sql = """
            SELECT COUNT(*) FROM payment_proofs
            WHERE id = ? AND active = true
            """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }
    
    public BigDecimal getTotalVerifiedAmount(Long orderId) {
        String sql = """
            SELECT COALESCE(SUM(amount), 0)
            FROM payment_proofs
            WHERE order_id = ?
                AND verified = true
                AND active = true
            """;
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, orderId);
    }
}
