package com.sk.hotspot.aggregator.application.service;

public interface AggregatorServiceRedis {
    void saveSessionForMember(String memberId);
    void saveFavoriteStoreList();
    boolean getSessionForMember(String memberId);
}
