package com.bances.agua_deliciosa.service.core;

import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<User> findAllEmployees() {
        return userRepository.findByUserableType("Employee");
    }

    @Transactional(readOnly = true)
    public List<User> findAllClients() {
        return userRepository.findByUserableType("Client");
    }

    @Transactional
    public User save(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User createUser(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Transactional
    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public long countEmployees() {
        return userRepository.countByUserableType("Employee");
    }

    @Transactional(readOnly = true)
    public long countClients() {
        return userRepository.countByUserableType("Client");
    }

    @Transactional(readOnly = true)
    public List<User> findTopCustomers(int limit) {
        return userRepository.findTopByOrders(limit);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
