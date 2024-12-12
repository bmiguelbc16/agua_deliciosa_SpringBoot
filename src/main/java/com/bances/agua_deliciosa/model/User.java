package com.bances.agua_deliciosa.model;

import com.bances.agua_deliciosa.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity implements UserDetails {
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(name = "document_number", unique = true)
    private String documentNumber;
    
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "email_verified_at")
    private LocalDateTime emailVerifiedAt;
    
    @Column(name = "remember_token")
    private String rememberToken;
    
    private boolean active = true;
    
    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    
    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    
    @Column(name = "verification_token")
    private String verificationToken;
    
    @Column(name = "verification_token_created_at")
    private LocalDateTime verificationTokenCreatedAt;
    
    @Column(name = "reset_token")
    private String resetToken;
    
    @Column(name = "reset_token_created_at")
    private LocalDateTime resetTokenCreatedAt;
    
    @Column(name = "userable_type")
    private String userableType;
    
    @Column(name = "userable_id")
    private Long userableId;
    
    @Transient
    private Set<SimpleGrantedAuthority> authorities = new HashSet<>();
    
    public String getFullName() {
        return name + " " + lastName;
    }
    
    public boolean isEmailVerified() {
        return emailVerifiedAt != null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null || authorities.isEmpty()) {
            authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
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
        return active;
    }

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
    }

    public void setAuthorities(Set<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }
} 