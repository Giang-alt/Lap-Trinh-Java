package com.evmarketplace.dto;

public class LoginResponse {
    
    private String token;
    private String userType;
    private Long userId;
    private String username;
    
    // Constructors
    public LoginResponse() {}
    
    public LoginResponse(String token, String userType, Long userId, String username) {
        this.token = token;
        this.userType = userType;
        this.userId = userId;
        this.username = username;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public void setUserType(String userType) {
        this.userType = userType;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
}
