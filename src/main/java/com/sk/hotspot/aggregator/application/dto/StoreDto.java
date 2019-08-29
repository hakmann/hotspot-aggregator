package com.sk.hotspot.aggregator.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreDto {
    // Store
    Long storeId;
    String storeName;
    String storeCategory;
    String storeAddress;

    public StoreDto(){};

    public StoreDto(Long storeId, String storeName, String storeCategory, String storeAddress) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeCategory = storeCategory;
        this.storeAddress = storeAddress;
    }
}
