package com.sk.hotspot.aggregator.application.service;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.sk.hotspot.aggregator.application.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AggregatorServiceImpl implements AggregatorService {

    @Value("${store.host}")
    private String storeHost;
    @Value("${store.findByLocation}")
    private String findByLocation;

    @Value("${review.host}")
    private String reviewHost;
    @Value("${review.findReviewByStoreId}")
    private String findReviewByStoreId;

    @Override
    public MemberDto findMemberInfoByLoginId(String loginId) {
        return null;
    }

    @Override
    public List<StoreDto> findStoreInfoByCurrentLocation(float x, float y) {
        ClientHttpRequestFactory requestFactory = getClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        String response = restTemplate.getForObject("http://" + storeHost + findByLocation.replace("{x}", String.valueOf(x)).replace("{y}", String.valueOf(y)), String.class);
        log.debug(response);
        return parseResponseFromFindStore(response);
    }

    @Override
    public List<ReviewDto> findReviewByStoreId(Long storeId) {
        ClientHttpRequestFactory requestFactory = getClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        String response = restTemplate.getForObject("http://" + reviewHost + findReviewByStoreId.replace("{storeId}", String.valueOf(storeId)), String.class);
        log.debug(response);
        return parseResponseFromFindReview(response);
    }

    @Override
    public StoreOutboundPayload findStoreInfoWithReviewByLocation(float x, float y) {
        // store
        List<StoreDto> storeInfoByCurrentLocation = findStoreInfoByCurrentLocation(x, y);

        StoreOutboundPayload storeOutboundPayload = new StoreOutboundPayload();
        List<StoreWithReviewDto> storeWithReviewDtos = new ArrayList<>();

        storeInfoByCurrentLocation.forEach(storeDto -> {
            // TODO: find review
            StoreWithReviewDto storeWithReviewDto = new StoreWithReviewDto();
            storeWithReviewDto.setStoreName(storeDto.getStoreName());
            storeWithReviewDto.setStoreAddress(storeDto.getStoreAddress());
            storeWithReviewDto.setStoreCategory(storeDto.getStoreCategory());
            storeWithReviewDto.setStoreId(storeDto.getStoreId());
            storeWithReviewDto.setReviews(findReviewByStoreId(storeDto.getStoreId()));

            storeWithReviewDtos.add(storeWithReviewDto);
        });

        storeOutboundPayload.setOutboundPayloads(storeWithReviewDtos);

        return storeOutboundPayload;
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        int connectionTimeout = 5000;
        int readTimeout = 30000;
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
                = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(connectionTimeout);
        clientHttpRequestFactory.setReadTimeout(readTimeout);
        return clientHttpRequestFactory;
    }

    private List<StoreDto> parseResponseFromFindStore(String response) {
        Gson gson = new Gson();
        ArrayList<LinkedTreeMap> resultMap = gson.fromJson(response, ArrayList.class);
        if (CollectionUtils.isEmpty(resultMap)) {
            return Collections.emptyList();
        }else{
            return resultMap.stream()
                    .map(result -> {
                        LinkedTreeMap resMap = (LinkedTreeMap) result;
                        StoreDto dto = new StoreDto();
                        dto.setStoreName(resMap.get("name").toString());
                        dto.setStoreAddress(resMap.get("rdnmAdr").toString());
                        dto.setStoreCategory(resMap.get("category").toString());
                        Double eid = (Double) resMap.get("eid");
                        dto.setStoreId(eid.longValue());
                        return dto;
                    }).collect(Collectors.toList());
        }
    }

    private List<ReviewDto> parseResponseFromFindReview(String response) {
        Gson gson = new Gson();
        ArrayList<LinkedTreeMap> resultMap = gson.fromJson(response, ArrayList.class);
        if (CollectionUtils.isEmpty(resultMap)) {
            return Collections.emptyList();
        }else{
            return resultMap.stream()
                    .map(result -> {
                        LinkedTreeMap resMap = (LinkedTreeMap) result;
                        return ReviewDto.builder()
                                .content(resMap.get("content").toString())
                                .registDate(Date.valueOf(resMap.get("registDate").toString()))
                                .build();
                    }).collect(Collectors.toList());
        }
    }
}
