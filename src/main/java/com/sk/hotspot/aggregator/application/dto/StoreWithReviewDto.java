package com.sk.hotspot.aggregator.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class StoreWithReviewDto extends StoreDto{

    // Review
    private List<ReviewDto> reviews;

    public StoreWithReviewDto(){};
}
