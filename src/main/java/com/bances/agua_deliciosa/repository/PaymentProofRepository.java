package com.bances.agua_deliciosa.repository;

import com.bances.agua_deliciosa.model.PaymentProof;
import com.bances.agua_deliciosa.model.PaymentType;
import com.bances.agua_deliciosa.model.VerificationStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class PaymentProofRepository implements BaseRepository<PaymentProof> {
    private final JdbcTemplate jdbcTemplate;

    public PaymentProofRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public PaymentProof save(PaymentProof proof) {
        if (proof.getId() == null) {
            String sql = """
                INSERT INTO payment_proofs (order_id, payment_type, amount, 
                proof_image, image_name, image_type, verification_status, 
                verified_by_employee_id, verification_date, verification_notes, 
                created_by_employee_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
            jdbcTemplate.update(sql,
                proof.getOrderId(),
                proof.getPaymentType().name(),
                proof.getAmount(),
                proof.getProofImage(),
                proof.getImageName(),
                proof.getImageType(),
                proof.getVerificationStatus().name(),
                proof.getVerifiedByEmployeeId(),
                proof.getVerificationDate(),
                proof.getVerificationNotes(),
                proof.getCreatedByEmployeeId()
            );
        } else {
            String sql = """
                UPDATE payment_proofs SET order_id = ?, payment_type = ?, 
                amount = ?, proof_image = ?, image_name = ?, image_type = ?, 
                verification_status = ?, verified_by_employee_id = ?, 
                verification_date = ?, verification_notes = ?, 
                created_by_employee_id = ? WHERE id = ?
            """;
            jdbcTemplate.update(sql,
                proof.getOrderId(),
                proof.getPaymentType().name(),
                proof.getAmount(),
                proof.getProofImage(),
                proof.getImageName(),
                proof.getImageType(),
                proof.getVerificationStatus().name(),
                proof.getVerifiedByEmployeeId(),
                proof.getVerificationDate(),
                proof.getVerificationNotes(),
                proof.getCreatedByEmployeeId(),
                proof.getId()
            );
        }
        return proof;
    }

    @Override
    public Optional<PaymentProof> findById(Long id) {
        String sql = "SELECT * FROM payment_proofs WHERE id = ?";
        try {
            PaymentProof proof = jdbcTemplate.queryForObject(sql, this::mapRowToPaymentProof, id);
            return Optional.ofNullable(proof);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<PaymentProof> findAll() {
        String sql = "SELECT * FROM payment_proofs";
        return jdbcTemplate.query(sql, this::mapRowToPaymentProof);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM payment_proofs WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM payment_proofs WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM payment_proofs";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    private PaymentProof mapRowToPaymentProof(ResultSet rs, int rowNum) throws SQLException {
        PaymentProof proof = new PaymentProof();
        proof.setId(rs.getLong("id"));
        proof.setOrderId(rs.getLong("order_id"));
        proof.setPaymentType(PaymentType.valueOf(rs.getString("payment_type")));
        proof.setAmount(rs.getBigDecimal("amount"));
        proof.setProofImage(rs.getBytes("proof_image"));
        proof.setImageName(rs.getString("image_name"));
        proof.setImageType(rs.getString("image_type"));
        proof.setVerificationStatus(VerificationStatus.valueOf(rs.getString("verification_status")));
        proof.setVerifiedByEmployeeId(rs.getLong("verified_by_employee_id"));
        proof.setVerificationDate(rs.getTimestamp("verification_date") != null ? 
            rs.getTimestamp("verification_date").toLocalDateTime() : null);
        proof.setVerificationNotes(rs.getString("verification_notes"));
        proof.setCreatedByEmployeeId(rs.getLong("created_by_employee_id"));
        proof.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        proof.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return proof;
    }

    public List<PaymentProof> findByOrderId(Long orderId) {
        String sql = "SELECT * FROM payment_proofs WHERE order_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToPaymentProof, orderId);
    }

    public List<PaymentProof> findByVerificationStatus(VerificationStatus status) {
        String sql = "SELECT * FROM payment_proofs WHERE verification_status = ?";
        return jdbcTemplate.query(sql, this::mapRowToPaymentProof, status.name());
    }

    public List<PaymentProof> findByPaymentType(PaymentType paymentType) {
        String sql = "SELECT * FROM payment_proofs WHERE payment_type = ?";
        return jdbcTemplate.query(sql, this::mapRowToPaymentProof, paymentType.name());
    }

    public List<PaymentProof> findByVerifiedByEmployeeId(Long employeeId) {
        String sql = "SELECT * FROM payment_proofs WHERE verified_by_employee_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToPaymentProof, employeeId);
    }

    public List<PaymentProof> findByCreatedByEmployeeId(Long employeeId) {
        String sql = "SELECT * FROM payment_proofs WHERE created_by_employee_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToPaymentProof, employeeId);
    }
}
