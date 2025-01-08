package com.bances.agua_deliciosa.model;

import java.time.LocalDate;

public class Client extends BaseModel{
    private User user;
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    // Getters y setters delegados a User
    public String getFirstName() {
        return user != null ? user.getName() : null;
    }
    
    public void setFirstName(String firstName) {
        if (user == null) {
            user = new User();
        }
        user.setName(firstName);
    }
    
    public String getLastName() {
        return user != null ? user.getLastName() : null;
    }
    
    public void setLastName(String lastName) {
        if (user == null) {
            user = new User();
        }
        user.setLastName(lastName);
    }
    
    public String getDocumentNumber() {
        return user != null ? user.getDocumentNumber() : null;
    }
    
    public void setDocumentNumber(String documentNumber) {
        if (user == null) {
            user = new User();
        }
        user.setDocumentNumber(documentNumber);
    }
    
    public String getPhone() {
        return user != null ? user.getPhoneNumber() : null;
    }
    
    public void setPhone(String phone) {
        if (user == null) {
            user = new User();
        }
        user.setPhoneNumber(phone);
    }
    
    public Gender getGender() {
        return user != null ? user.getGender() : null;
    }
    
    public void setGender(Gender gender) {
        if (user == null) {
            user = new User();
        }
        user.setGender(gender);
    }
    
    public LocalDate getBirthDate() {
        return user != null ? user.getBirthDate() : null;
    }

    public void setBirthDate(LocalDate birthDate) {
        if (user == null) {
            user = new User();
        }
        user.setBirthDate(birthDate);
    }
    
    public Boolean isActive() {
        return user != null ? user.isActive() : null;
    }
    
    public void setActive(Boolean active) {
        if (user == null) {
            user = new User();
        }
        user.setActive(active);
    }
}
