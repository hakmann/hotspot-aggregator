package com.sk.hotspot.aggregator.application.service;

import com.sk.hotspot.aggregator.application.dto.ReviewDto;
import com.sk.hotspot.aggregator.application.dto.StoreDto;
import com.sk.hotspot.aggregator.application.dto.StoreOutboundPayload;

import java.util.List;

public interface AggregatorService {
    // Store
    List<StoreDto> findStoreInfoByCurrentLocation(float x, float y);

    // Review
    List<ReviewDto> findReviewByStoreId(Long storeId);

    StoreOutboundPayload findStoreInfoWithReviewByLocation(float x, float y);
}
