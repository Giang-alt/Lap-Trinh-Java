package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DataPackageResponse {
    private Long id;
    private String name;
    private String description;
    private DataType dataType;
    private DataFormat format;
    private BigDecimal price;
    private PricingModel pricingModel;
    private PackageStatus status;
    private Long dataSourceId;
    private Long sizeInMb;
    private LocalDateTime createdDate;

    // Constructors
    public DataPackageResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public DataType getDataType() { return dataType; }
    public void setDataType(DataType dataType) { this.dataType = dataType; }

    public DataFormat getFormat() { return format; }
    public void setFormat(DataFormat format) { this.format = format; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public PricingModel getPricingModel() { return pricingModel; }
    public void setPricingModel(PricingModel pricingModel) { this.pricingModel = pricingModel; }

    public PackageStatus getStatus() { return status; }
    public void setStatus(PackageStatus status) { this.status = status; }

    public Long getDataSourceId() { return dataSourceId; }
    public void setDataSourceId(Long dataSourceId) { this.dataSourceId = dataSourceId; }

    public Long getSizeInMb() { return sizeInMb; }
    public void setSizeInMb(Long sizeInMb) { this.sizeInMb = sizeInMb; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}
public static DataPackageResponse fromEntity(DataPackage entity) {
    if (entity == null) {
        return null;
    }

    DataPackageResponse dto = new DataPackageResponse();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());
    dto.setDataType(entity.getDataType());
    dto.setFormat(entity.getFormat());
    dto.setPrice(entity.getPrice());
    dto.setPricingModel(entity.getPricingModel());
    dto.setStatus(entity.getStatus());
    dto.setDataSourceId(entity.getDataSourceId());
    dto.setSizeInMb(entity.getSizeInMb());
    dto.setCreatedDate(entity.getCreatedDate());

    return dto;
}

// --- Getters & Setters ---
// (Phần này bị bỏ qua để code ngắn gọn, nhưng bạn cần thêm chúng)
// Ví dụ:
public Long getId() { return id; }
public void setId(Long id) { this.id = id; }
// ... và các getter/setter khác cho các trường còn lại ...
}