package com.sk.hotspot.aggregator.application.dto;

import lombok.Data;

@Data
public class StoreDto {
    // Store
    Long storeId;
    String storeName;
    String storeCategory;
    String storeAddress;

    public StoreDto(){};
}
