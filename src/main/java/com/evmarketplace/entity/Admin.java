package com.evmarketplace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User {
    
    @NotBlank
    @Size(max = 100)
    private String permissions;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "access_level")
    private AccessLevel accessLevel = AccessLevel.SUPER_ADMIN;
    
    // Constructors
    public Admin() {
        super();
        this.setRole(UserRole.ADMIN);
    }
    
    public Admin(String username, String email, String password, String permissions, AccessLevel accessLevel) {
        super(username, email, password, UserRole.ADMIN);
        this.permissions = permissions;
        this.accessLevel = accessLevel;
    }
    
    // Getters and Setters
    public String getPermissions() {
        return permissions;
    }
    
    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
    
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }
    
    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }
    
    // Enums
    public enum AccessLevel {
        SUPER_ADMIN, ADMIN, MODERATOR
    }
}
