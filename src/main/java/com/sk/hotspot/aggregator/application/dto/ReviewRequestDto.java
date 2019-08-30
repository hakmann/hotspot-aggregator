package com.sk.hotspot.aggregator.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
    String content;
    // 현재 로그인 한 사용자의 memberId를 넣음
    Long customerId;
    Long storeId;
}