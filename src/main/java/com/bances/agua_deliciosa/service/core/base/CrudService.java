package com.bances.agua_deliciosa.service.core.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.List;

public interface CrudService<T, D> {
    Optional<T> findById(Long id);
    T getById(Long id);
    List<T> findAll();
    Page<T> findAll(Pageable pageable);
    T create(D dto);
    T update(Long id, D dto);
    void delete(Long id);
} 