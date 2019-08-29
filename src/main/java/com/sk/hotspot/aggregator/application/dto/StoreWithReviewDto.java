package com.sk.hotspot.aggregator.application.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class StoreWithReviewDto extends StoreDto{

    // Review
    private List<ReviewDto> reviews;

    public StoreWithReviewDto(){};

    public StoreWithReviewDto(List<ReviewDto> reviews) {
        this.reviews = reviews;
    }

    public StoreWithReviewDto(Long storeId, String storeName, String storeCategory, String storeAddress, List<ReviewDto> reviews) {
        super(storeId, storeName, storeCategory, storeAddress);
        this.reviews = reviews;
    }
}
