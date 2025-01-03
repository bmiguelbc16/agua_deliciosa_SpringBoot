package com.bances.agua_deliciosa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bances.agua_deliciosa.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
} 