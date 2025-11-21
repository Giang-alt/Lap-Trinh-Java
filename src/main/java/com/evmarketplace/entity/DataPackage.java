package com.evmarketplace.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "data_packages")
public class DataPackage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 100)
    private String name;
    
    @Size(max = 500)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "data_type")
    private DataType dataType;
    
    @Enumerated(EnumType.STRING)
    private DataFormat format;
    
    @Column(name = "file_size")
    private Long size;
    
    @NotNull
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "pricing_model")
    private PricingModel pricingModel = PricingModel.ONE_TIME;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_source_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private DataSource dataSource;
    
    @Enumerated(EnumType.STRING)
    private PackageStatus status = PackageStatus.ACTIVE;
    
    @Column(name = "file_path")
    private String filePath;
    
    // Constructors
    public DataPackage() {}
    
    public DataPackage(String name, String description, DataType dataType, DataFormat format, 
                      Long size, BigDecimal price, DataSource dataSource) {
        this.name = name;
        this.description = description;
        this.dataType = dataType;
        this.format = format;
        this.size = size;
        this.price = price;
        this.dataSource = dataSource;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public DataType getDataType() {
        return dataType;
    }
    
    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
    
    public DataFormat getFormat() {
        return format;
    }
    
    public void setFormat(DataFormat format) {
        this.format = format;
    }
    
    public Long getSize() {
        return size;
    }
    
    public void setSize(Long size) {
        this.size = size;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public PricingModel getPricingModel() {
        return pricingModel;
    }
    
    public void setPricingModel(PricingModel pricingModel) {
        this.pricingModel = pricingModel;
    }
    
    public DataSource getDataSource() {
        return dataSource;
    }
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public PackageStatus getStatus() {
        return status;
    }
    
    public void setStatus(PackageStatus status) {
        this.status = status;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    // Enums
    public enum DataType {
        RAW_DATA, PROCESSED_DATA, ANALYTICS_REPORT, DASHBOARD
    }
    
    public enum DataFormat {
        CSV, JSON, XML, EXCEL, PDF, API
    }
    
    public enum PricingModel {
        ONE_TIME, SUBSCRIPTION, PER_DOWNLOAD
    }
    
    public enum PackageStatus {
        ACTIVE, INACTIVE, SUSPENDED, PENDING_APPROVAL
    }
}
