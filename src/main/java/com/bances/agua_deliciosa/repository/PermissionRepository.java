package com.bances.agua_deliciosa.repository;

import com.bances.agua_deliciosa.model.Permission;
import com.bances.agua_deliciosa.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
    boolean existsByName(String name);
    
    @Query("SELECT r FROM Role r JOIN r.permissions p WHERE p.id = :permissionId")
    List<Role> findRolesUsingPermission(@Param("permissionId") Long permissionId);
}
