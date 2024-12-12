package com.bances.agua_deliciosa.service.core.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public abstract class BaseService<T, D> {
    
    protected final JpaRepository<T, Long> repository;
    protected final ModelMapper mapper;
    
    protected BaseService(JpaRepository<T, Long> repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    public Optional<T> findById(Long id) {
        return repository.findById(id);
    }
    
    public T getById(Long id) {
        return findById(id)
            .orElseThrow(() -> new RuntimeException(getEntityName() + " no encontrado"));
    }
    
    public List<T> findAll() {
        return repository.findAll();
    }
    
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    public long count() {
        return repository.count();
    }
    
    @Transactional
    public T create(D dto) {
        T entity = createFromDTO(dto);
        beforeSave(entity);
        T savedEntity = repository.save(entity);
        afterSave(savedEntity);
        return savedEntity;
    }
    
    @Transactional
    public T update(Long id, D dto) {
        T entity = getById(id);
        updateFromDTO(entity, dto);
        beforeSave(entity);
        T savedEntity = repository.save(entity);
        afterSave(savedEntity);
        return savedEntity;
    }
    
    @Transactional
    public void delete(Long id) {
        beforeDelete(id);
        repository.deleteById(id);
        afterDelete(id);
    }
    
    protected void beforeSave(T entity) {}
    protected void afterSave(T entity) {}
    protected void beforeDelete(Long id) {}
    protected void afterDelete(Long id) {}
    
    protected abstract T createFromDTO(D dto);
    protected abstract T updateFromDTO(T entity, D dto);
    protected abstract String getEntityName();
} 