package com.bances.agua_deliciosa.repository;

import com.bances.agua_deliciosa.model.Client;
import com.bances.agua_deliciosa.model.Gender;
import com.bances.agua_deliciosa.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;

import java.util.List;
import java.util.Optional;

@Repository
public class ClientRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Client> clientMapper = (rs, rowNum) -> {
        Client client = new Client();
        client.setId(rs.getLong("id"));
        
        User user = new User();
        user.setName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setDocumentNumber(rs.getString("document_number"));
        user.setPhoneNumber(rs.getString("phone"));
        user.setBirthDate(rs.getDate("birth_date").toLocalDate());
        try {
            user.setGender(Gender.valueOf(rs.getString("gender")));
        } catch (IllegalArgumentException e) {
            user.setGender(Gender.UNKNOWN);
        }
        user.setActive(rs.getBoolean("active"));
        
        client.setUser(user);
        client.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        client.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return client;
    };

    public ClientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Client> findAll() {
        String sql = "SELECT * FROM clients c JOIN users u ON c.id = u.userable_id WHERE u.userable_type = 'Client'";
        return jdbcTemplate.query(sql, clientMapper);
    }

    public Optional<Client> findById(Long id) {
        String sql = "SELECT * FROM clients c JOIN users u ON c.id = u.userable_id WHERE c.id = ? AND u.userable_type = 'Client'";
        List<Client> clients = jdbcTemplate.query(sql, clientMapper, id);
        return clients.isEmpty() ? Optional.empty() : Optional.of(clients.get(0));
    }

    public Optional<Client> findByDocumentNumber(String documentNumber) {
        String sql = "SELECT * FROM clients c JOIN users u ON c.id = u.userable_id WHERE u.document_number = ? AND u.userable_type = 'Client'";
        List<Client> clients = jdbcTemplate.query(sql, clientMapper, documentNumber);
        return clients.isEmpty() ? Optional.empty() : Optional.of(clients.get(0));
    }

    public Client save(Client client) {
        if (client.getId() == null) {
            // Primero insertamos el client
            KeyHolder keyHolder = new GeneratedKeyHolder();
            String clientSql = "INSERT INTO clients (created_at, updated_at) VALUES (?, ?)";
            
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(clientSql, Statement.RETURN_GENERATED_KEYS);
                ps.setObject(1, client.getCreatedAt());
                ps.setObject(2, client.getUpdatedAt());
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key == null) {
                throw new RuntimeException("Failed to retrieve generated ID for client");
            }
            client.setId(key.longValue());

            // Luego insertamos el user
            String userSql = """
                INSERT INTO users (name, last_name, email, document_number, phone_number, 
                birth_date, gender, active, userable_type, userable_id, password, role_id) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Client', ?, ?, ?)
            """;
            jdbcTemplate.update(userSql,
                client.getUser().getName(),
                client.getUser().getLastName(),
                client.getUser().getEmail(),
                client.getUser().getDocumentNumber(),
                client.getUser().getPhoneNumber(),
                client.getUser().getBirthDate(),
                client.getUser().getGender().toString(),
                client.getUser().isActive(),
                client.getId(),
                client.getUser().getPassword(),
                client.getUser().getRoleId()
            );
        } else {
            // Actualizamos client
            String clientSql = "UPDATE clients SET updated_at = ? WHERE id = ?";
            jdbcTemplate.update(clientSql,
                client.getUpdatedAt(),
                client.getId()
            );

            // Actualizamos user
            String userSql = """
                UPDATE users SET 
                name = ?, last_name = ?, email = ?, document_number = ?, phone_number = ?,
                birth_date = ?, gender = ?, active = ?, password = ?, role_id = ?
                WHERE userable_id = ? AND userable_type = 'Client'
            """;
            jdbcTemplate.update(userSql,
                client.getUser().getName(),
                client.getUser().getLastName(),
                client.getUser().getEmail(),
                client.getUser().getDocumentNumber(),
                client.getUser().getPhoneNumber(),
                client.getUser().getBirthDate(),
                client.getUser().getGender().toString(),
                client.getUser().isActive(),
                client.getUser().getPassword(),
                client.getUser().getRoleId(),
                client.getId()
            );
        }
        return client;
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE userable_id = ? AND userable_type = 'Client'", id);
        jdbcTemplate.update("DELETE FROM clients WHERE id = ?", id);
    }

    public boolean existsByDocumentNumber(String documentNumber) {
        String sql = "SELECT COUNT(*) FROM users WHERE document_number = ? AND userable_type = 'Client'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, documentNumber);
        return count != null && count > 0;
    }

    public boolean existsByDocumentNumberAndIdNot(String documentNumber, Long id) {
        String sql = "SELECT COUNT(*) FROM users WHERE document_number = ? AND userable_type = 'Client' AND userable_id != ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, documentNumber, id);
        return count != null && count > 0;
    }
}