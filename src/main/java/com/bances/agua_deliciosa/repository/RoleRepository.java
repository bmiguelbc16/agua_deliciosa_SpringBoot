package com.bances.agua_deliciosa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.bances.agua_deliciosa.model.Role;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    Optional<Role> findByName(String name);
    
    @Query("SELECT r FROM Role r WHERE r.name IN :names")
    Set<Role> findByNames(@Param("names") Set<String> names);
    
    @Query("SELECT r FROM Role r WHERE r.name != 'ROLE_CLIENTE'")
    Set<Role> findAllExceptClient();
    
    boolean existsByName(String name);
} 