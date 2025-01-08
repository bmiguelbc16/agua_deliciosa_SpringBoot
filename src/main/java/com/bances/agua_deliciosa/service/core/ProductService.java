package com.bances.agua_deliciosa.service.core;

import com.bances.agua_deliciosa.model.Product;
import com.bances.agua_deliciosa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long count() {
        return productRepository.count();
    }

    @Transactional(readOnly = true)
    public List<Product> findTopSelling(int limit) {
        return productRepository.findTopSelling(limit);
    }

    @Transactional(readOnly = true)
    public List<Product> findByForSale(boolean forSale) {
        return productRepository.findByForSale(forSale);
    }

    @Transactional(readOnly = true)
    public List<Product> findByStockLessThan(int minStock) {
        return productRepository.findByStockLessThan(minStock);
    }

    @Transactional
    public void updateStock(Long productId, int quantity) {
        productRepository.updateStock(productId, quantity);
    }
}