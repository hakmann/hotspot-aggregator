package com.sk.hotspot.aggregator.application.service;

import com.sk.hotspot.aggregator.application.dto.*;

import java.util.List;

public interface AggregatorService {
    // Member
    Long findMemberIdByLoginId(String loginId);
    LoginResponseDto login(LoginRequestDto request);
    boolean validateToken(String token);

    // Store
    List<StoreDto> findStoreInfoByCurrentLocation(float x, float y);

    // Review
    List<ReviewResponseDto> findReviewByStoreId(Long storeId);
    ReviewResponseDto postReview(ReviewRequestDto requestDto);
    StoreOutboundPayload findStoreInfoWithReviewByLocation(float x, float y);

    // mbr/login/rest,  mbr/token/chk
}
