package com.bances.agua_deliciosa.service.core;

import com.bances.agua_deliciosa.model.Order;
import com.bances.agua_deliciosa.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Transactional
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long count() {
        return orderRepository.count();
    }

    @Transactional(readOnly = true)
    public List<Order> findRecent(int limit) {
        return orderRepository.findTopByOrderByCreatedAtDesc(limit);
    }
}
