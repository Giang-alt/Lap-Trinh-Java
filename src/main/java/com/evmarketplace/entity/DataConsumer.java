package com.evmarketplace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "data_consumers")
@PrimaryKeyJoinColumn(name = "user_id")
public class DataConsumer extends User {
    
    @NotBlank
    @Size(max = 100)
    @Column(name = "company_name")
    private String companyName;
    
    @Size(max = 100)
    private String industry;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_type")
    private SubscriptionType subscriptionType = SubscriptionType.BASIC;
    
    @Column(columnDefinition = "TEXT")
    private String preferences;
    
    // Constructors
    public DataConsumer() {
        super();
        this.setRole(UserRole.DATA_CONSUMER);
    }
    
    public DataConsumer(String username, String email, String password, String companyName, String industry) {
        super(username, email, password, UserRole.DATA_CONSUMER);
        this.companyName = companyName;
        this.industry = industry;
    }
    
    // Getters and Setters
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getIndustry() {
        return industry;
    }
    
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    
    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }
    
    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
    
    public String getPreferences() {
        return preferences;
    }
    
    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
    
    // Enums
    public enum SubscriptionType {
        BASIC, PREMIUM, ENTERPRISE
    }
}
