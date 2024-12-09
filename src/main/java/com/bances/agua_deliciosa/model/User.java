package com.bances.agua_deliciosa.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "document_number", length = 11, unique = true)
    private String documentNumber;
    
    private String name;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    private String gender;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(unique = true)
    private String email;
    
    @Column(name = "userable_type")
    private String userableType;
    
    @Column(name = "userable_id")
    private Long userableId;
    
    private String password;
    
    @Column(name = "remember_token")
    private String rememberToken;
    
    private boolean active = true;
    
    @Column(name = "email_verified_at")
    private LocalDateTime emailVerifiedAt;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "model_has_roles",
        joinColumns = @JoinColumn(name = "model_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    
    public String getFullName() {
        return String.format("%s %s", name, lastName);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
} 