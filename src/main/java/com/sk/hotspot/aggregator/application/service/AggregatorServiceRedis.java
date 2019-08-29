package com.sk.hotspot.aggregator.application.service;

import com.sk.hotspot.aggregator.application.dto.LoginResponseDto;

public interface AggregatorServiceRedis {
    void saveSessionForMember(LoginResponseDto loginResponseDto);
    void saveFavoriteStoreList();
    String getSessionForMember(String accessToken);
}
