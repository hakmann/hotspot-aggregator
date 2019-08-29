package com.sk.hotspot.aggregator.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class StoreWithReviewDto extends StoreDto{

    // Review
    private List<ReviewResponseDto> reviews;

    public StoreWithReviewDto(){};

    public StoreWithReviewDto(List<ReviewResponseDto> reviews) {
        this.reviews = reviews;
    }

    public StoreWithReviewDto(Long storeId, String storeName, String storeCategory, String storeAddress, List<ReviewResponseDto> reviews) {
        super(storeId, storeName, storeCategory, storeAddress);
        this.reviews = reviews;
    }
}
