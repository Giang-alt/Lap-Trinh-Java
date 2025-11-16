package com.evmarketplace.dto;

import com.evmarketplace.enums.DataFormat;
import com.evmarketplace.enums.DataType;
import com.evmarketplace.enums.PackageStatus;
import com.evmarketplace.enums.PricingModel;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class UpdateDataPackageRequest {

    @NotBlank(message = "Tên gói dữ liệu không được bỏ trống")
    private String name;

    @Size(max = 500, message = "Mô tả không được quá 500 ký tự")
    private String description;

    @NotNull(message = "Loại dữ liệu không được bỏ trống")
    private DataType dataType;

    @NotNull(message = "Định dạng không được bỏ trống")
    private DataFormat format;

    @NotNull(message = "Giá không được bỏ trống")
    @DecimalMin(value = "0.0", message = "Giá phải là số dương")
    private BigDecimal price;

    @NotNull(message = "Mô hình giá không được bỏ trống")
    private PricingModel pricingModel;

    @NotNull(message = "Trạng thái không được bỏ trống")
    private PackageStatus status;

    @Positive(message = "Kích thước file phải là số dương")
    private Long size;

    // Thêm getters và setters cho tất cả các trường
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

    public PackageStatus getStatus() {
        return status;
    }

    public void setStatus(PackageStatus status) {
        this.status = status;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
