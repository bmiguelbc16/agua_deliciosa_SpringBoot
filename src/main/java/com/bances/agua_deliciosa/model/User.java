package com.bances.agua_deliciosa.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User extends BaseModel {
    private String name;
    private String lastName;
    private String email;
    private String documentNumber;
    private String password;
    private LocalDate birthDate;
    private String gender;
    private String phoneNumber;
    private Role role;
    private String userableType;
    private Long userableId;
    private LocalDateTime emailVerifiedAt;
    private String rememberToken;
    private boolean active = true;
    private List<UserToken> tokens = new ArrayList<>();
    private List<UserVerification> verifications = new ArrayList<>();
    private List<PasswordReset> passwordResets = new ArrayList<>();

    // Getters
    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public String getUserableType() {
        return userableType;
    }

    public Long getUserableId() {
        return userableId;
    }

    public LocalDateTime getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public String getRememberToken() {
        return rememberToken;
    }

    public boolean isActive() {
        return active;
    }

    public List<UserToken> getTokens() {
        return new ArrayList<>(tokens);
    }

    public List<UserVerification> getVerifications() {
        return new ArrayList<>(verifications);
    }

    public List<PasswordReset> getPasswordResets() {
        return new ArrayList<>(passwordResets);
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setUserableType(String userableType) {
        this.userableType = userableType;
    }

    public void setUserableId(Long userableId) {
        this.userableId = userableId;
    }

    public void setEmailVerifiedAt(LocalDateTime emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // Utility methods
    public String getFullName() {
        return name + " " + lastName;
    }

    public boolean isVerified() {
        return emailVerifiedAt != null;
    }

    public boolean hasPermission(String permissionName) {
        return role != null && role.hasPermission(permissionName);
    }

    public void addToken(UserToken token) {
        if (token != null) {
            tokens.add(token);
        }
    }

    public void removeToken(UserToken token) {
        if (token != null) {
            tokens.remove(token);
        }
    }

    public void addVerification(UserVerification verification) {
        if (verification != null) {
            verifications.add(verification);
        }
    }

    public void removeVerification(UserVerification verification) {
        if (verification != null) {
            verifications.remove(verification);
        }
    }

    public void addPasswordReset(PasswordReset reset) {
        if (reset != null) {
            passwordResets.add(reset);
        }
    }

    public void removePasswordReset(PasswordReset reset) {
        if (reset != null) {
            passwordResets.remove(reset);
        }
    }
}
