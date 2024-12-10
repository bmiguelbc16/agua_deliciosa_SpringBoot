package com.bances.agua_deliciosa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.bances.agua_deliciosa.model.Product;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @Query("SELECT COUNT(p) FROM Product p WHERE p.stock <= 5")
    Long countLowStock();
    
    @Query("SELECT SUM(p.stock) FROM Product p")
    Long getTotalStock();
    
    @Query("SELECT p FROM Product p WHERE p.stock <= 5")
    List<Product> findLowStockProducts();
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:search%")
    Page<Product> findByNameContaining(String search, Pageable pageable);
}