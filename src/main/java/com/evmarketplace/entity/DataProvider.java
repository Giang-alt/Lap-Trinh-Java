package com.evmarketplace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "data_providers")
@PrimaryKeyJoinColumn(name = "user_id")
public class DataProvider extends User {
    
    @NotBlank
    @Size(max = 100)
    @Column(name = "company_name")
    private String companyName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "data_source_type")
    private DataSourceType dataSourceType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status")
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal revenue = BigDecimal.ZERO;
    
    // Constructors
    public DataProvider() {
        super();
        this.setRole(UserRole.DATA_PROVIDER);
    }
    
    public DataProvider(String username, String email, String password, String companyName, DataSourceType dataSourceType) {
        super(username, email, password, UserRole.DATA_PROVIDER);
        this.companyName = companyName;
        this.dataSourceType = dataSourceType;
    }
    
    // Getters and Setters
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public DataSourceType getDataSourceType() {
        return dataSourceType;
    }
    
    public void setDataSourceType(DataSourceType dataSourceType) {
        this.dataSourceType = dataSourceType;
    }
    
    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }
    
    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }
    
    public BigDecimal getRevenue() {
        return revenue;
    }
    
    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }
    
    // Enums
    public enum DataSourceType {
        OEM, CHARGING_STATION, FLEET_OPERATOR, RESEARCH_INSTITUTE, OTHER
    }
    
    public enum VerificationStatus {
        PENDING, VERIFIED, REJECTED
    }
}
