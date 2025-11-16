package com.evmarketplace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "data_sources")
public class DataSource {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 100)
    private String name;
    
    @Enumerated(EnumType.STRING)
    private DataSourceType type;
    
    @Size(max = 500)
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_provider_id", nullable = false)
    private DataProvider dataProvider;
    
    @Enumerated(EnumType.STRING)
    private DataSourceStatus status = DataSourceStatus.ACTIVE;
    
    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    // Constructors
    public DataSource() {}
    
    public DataSource(String name, DataSourceType type, String description, DataProvider dataProvider) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.dataProvider = dataProvider;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public DataSourceType getType() {
        return type;
    }
    
    public void setType(DataSourceType type) {
        this.type = type;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public DataProvider getDataProvider() {
        return dataProvider;
    }
    
    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }
    
    public DataSourceStatus getStatus() {
        return status;
    }
    
    public void setStatus(DataSourceStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    // Enums
    public enum DataSourceType {
        BATTERY_DATA, CHARGING_DATA, DRIVING_BEHAVIOR, V2G_TRANSACTIONS, FLEET_DATA, RESEARCH_DATA
    }
    
    public enum DataSourceStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }
}
