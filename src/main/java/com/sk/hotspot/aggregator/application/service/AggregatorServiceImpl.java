package com.sk.hotspot.aggregator.application.service;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.sk.hotspot.aggregator.application.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.*;
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
    @Value("${review.postReview}")
    private String postReview;

    @Value("${member.host}")
    private String memberHost;
    @Value("${member.login}")
    private String login;
    @Value("${member.validateToken}")
    private String validateToken;

    @Autowired
    private AggregatorServiceRedis aggregatorServiceRedis;

    @Override
    public MemberDto findMemberInfoByLoginId(String loginId) {
        return null;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        // Member Domain의 로그인 수행
        // /mbr/login/rest
        ClientHttpRequestFactory requestFactory = getClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        String accessToken = restTemplate.postForObject("http://" + memberHost + login, request, String.class);

        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .accessToken(parseResponseFromLogin(accessToken))
                .loginId(request.getUsername())
                .build();

        // save to redis
        aggregatorServiceRedis.saveSessionForMember(loginResponseDto);

        return loginResponseDto;
    }

    @Override
    public boolean validateToken(String token) {
        // Member Domain의 token 유효성 검증
        // /mbr/token/chk
        ClientHttpRequestFactory requestFactory = getClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("token", token);
        // false가 valid!
        return Optional.ofNullable(restTemplate.postForObject("http://" + memberHost + validateToken,requestMap, Boolean.class)).orElse(false);
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
    public List<ReviewResponseDto> findReviewByStoreId(Long storeId) {
        ClientHttpRequestFactory requestFactory = getClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        String response = restTemplate.getForObject("http://" + reviewHost + findReviewByStoreId.replace("{storeId}", String.valueOf(storeId)), String.class);
        log.debug(response);
        return parseResponseFromFindReview(response);
    }

    @Override
    public ReviewResponseDto postReview(ReviewRequestDto requestDto) {
        ClientHttpRequestFactory requestFactory = getClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        ReviewResponseDto response = restTemplate.postForObject("http://" + reviewHost + postReview, requestDto, ReviewResponseDto.class);

        return response;
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

    private List<ReviewResponseDto> parseResponseFromFindReview(String response) {
        Gson gson = new Gson();
        ArrayList<LinkedTreeMap> resultMap = gson.fromJson(response, ArrayList.class);
        if (CollectionUtils.isEmpty(resultMap)) {
            return Collections.emptyList();
        }else{
            return resultMap.stream()
                    .map(result -> {
                        LinkedTreeMap resMap = (LinkedTreeMap) result;
                        return ReviewResponseDto.builder()
                                .content(resMap.get("content").toString())
                                .registDate(Date.valueOf(resMap.get("registDate").toString()))
                                .build();
                    }).collect(Collectors.toList());
        }
    }

    private String parseResponseFromLogin(String response) {
        Gson gson = new Gson();
        LinkedTreeMap resultMap = gson.fromJson(response, LinkedTreeMap.class);
        return String.valueOf(resultMap.get("token"));
    }
}
