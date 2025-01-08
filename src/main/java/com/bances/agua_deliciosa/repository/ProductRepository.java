package com.bances.agua_deliciosa.repository;

import com.bances.agua_deliciosa.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Product> productMapper = (rs, rowNum) -> {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setSalePrice(rs.getBigDecimal("sale_price"));
        product.setForSale(rs.getBoolean("for_sale"));
        product.setStock(rs.getInt("stock"));
        product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        product.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return product;
    };

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> findAll() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, productMapper);
    }

    public Optional<Product> findById(Long id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        List<Product> products = jdbcTemplate.query(sql, productMapper, id);
        return products.isEmpty() ? Optional.empty() : Optional.of(products.get(0));
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            String sql = """
                INSERT INTO products (name, description, sale_price, for_sale, stock, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
            jdbcTemplate.update(sql,
                product.getName(),
                product.getDescription(),
                product.getSalePrice(),
                product.isForSale(),
                product.getStock(),
                product.getCreatedAt(),
                product.getUpdatedAt()
            );
            
            String idSql = "SELECT LAST_INSERT_ID()";
            Long id = jdbcTemplate.queryForObject(idSql, Long.class);
            product.setId(id);
        } else {
            String sql = """
                UPDATE products
                SET name = ?, description = ?, sale_price = ?, for_sale = ?, stock = ?, updated_at = ?
                WHERE id = ?
                """;
            jdbcTemplate.update(sql,
                product.getName(),
                product.getDescription(),
                product.getSalePrice(),
                product.isForSale(),
                product.getStock(),
                product.getUpdatedAt(),
                product.getId()
            );
        }
        return product;
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Product> findByForSale(boolean forSale) {
        String sql = "SELECT * FROM products WHERE for_sale = ?";
        return jdbcTemplate.query(sql, productMapper, forSale);
    }

    public List<Product> findByStockLessThan(int minStock) {
        String sql = "SELECT * FROM products WHERE stock < ?";
        return jdbcTemplate.query(sql, productMapper, minStock);
    }

    public void updateStock(Long productId, int quantity) {
        String sql = "UPDATE products SET stock = stock + ? WHERE id = ?";
        jdbcTemplate.update(sql, quantity, productId);
    }

    public List<Product> findTopSelling(int limit) {
        String sql = """
            SELECT p.* FROM products p
            LEFT JOIN order_items oi ON p.id = oi.product_id
            WHERE p.for_sale = true
            GROUP BY p.id
            ORDER BY COUNT(oi.id) DESC
            LIMIT ?
        """;
        return jdbcTemplate.query(sql, productMapper, limit);
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM products";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }
}
