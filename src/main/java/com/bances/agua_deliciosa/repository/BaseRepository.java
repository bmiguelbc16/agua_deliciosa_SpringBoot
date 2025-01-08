package com.bances.agua_deliciosa.repository;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T> {
    T save(T entity);
    Optional<T> findById(Long id);
    List<T> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    long count();
}
