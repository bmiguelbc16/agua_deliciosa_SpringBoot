package com.bances.agua_deliciosa.repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bances.agua_deliciosa.model.Client;

@Repository
public class ClientRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Client> clientMapper = (ResultSet rs, int rowNum) -> {
        Client client = new Client();
        client.setId(rs.getLong("id"));
        client.setName(rs.getString("name"));
        client.setLastName(rs.getString("last_name"));
        client.setEmail(rs.getString("email"));
        client.setPhoneNumber(rs.getString("phone_number"));
        client.setAddress(rs.getString("address"));
        client.setActive(rs.getBoolean("active"));
        return client;
    };

    public Optional<Client> findById(Long id) {
        String sql = "SELECT * FROM clients WHERE id = ? AND active = true";
        List<Client> clients = jdbcTemplate.query(sql, clientMapper, id);
        return clients.isEmpty() ? Optional.empty() : Optional.of(clients.get(0));
    }

    public List<Client> findAll() {
        String sql = "SELECT * FROM clients WHERE active = true";
        return jdbcTemplate.query(sql, clientMapper);
    }

    public Client save(Client client) {
        if (client.getId() == null) {
            return insert(client);
        }
        return update(client);
    }

    private Client insert(Client client) {
        String sql = "INSERT INTO clients (name, last_name, email, phone_number, address, active) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        jdbcTemplate.update(sql,
            client.getName(),
            client.getLastName(),
            client.getEmail(),
            client.getPhoneNumber(),
            client.getAddress(),
            true
        );
        
        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        if (id != null) {
            client.setId(id);
        }
        
        return client;
    }

    private Client update(Client client) {
        String sql = "UPDATE clients SET name = ?, last_name = ?, email = ?, " +
                    "phone_number = ?, address = ?, active = ? " +
                    "WHERE id = ?";
        
        jdbcTemplate.update(sql,
            client.getName(),
            client.getLastName(),
            client.getEmail(),
            client.getPhoneNumber(),
            client.getAddress(),
            client.isActive(),
            client.getId()
        );
        
        return client;
    }

    public void delete(Long id) {
        String sql = "UPDATE clients SET active = false WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM clients WHERE email = ? AND active = true";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM clients WHERE active = true";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }
}