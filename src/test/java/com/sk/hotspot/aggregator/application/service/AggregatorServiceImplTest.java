package com.sk.hotspot.aggregator.application.service;

import com.sk.hotspot.aggregator.application.dto.ReviewResponseDto;
import com.sk.hotspot.aggregator.application.dto.StoreDto;
import com.sk.hotspot.aggregator.application.dto.StoreOutboundPayload;
import com.sk.hotspot.aggregator.application.dto.StoreWithReviewDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class AggregatorServiceImplTest {

    @Spy
    @InjectMocks
    private AggregatorServiceImpl aggregatorService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findStoreInfoInNormalCase() {
        final int STORE_SIZE = 5;

        Mockito.doReturn(getStoreList(STORE_SIZE)).when(aggregatorService).findStoreInfoByCurrentLocation(Mockito.anyFloat(), Mockito.anyFloat());
        List<StoreDto> storeInfoByCurrentLocation = aggregatorService.findStoreInfoByCurrentLocation(Mockito.anyFloat(), Mockito.anyFloat());

        assertThat(storeInfoByCurrentLocation.size(), equalTo(STORE_SIZE));
    }

    @Test
    public void findReviewInfoInNormalCase() {
        final int REVIEW_SIZE = 10;

        Mockito.doReturn(getReviewList(REVIEW_SIZE)).when(aggregatorService).findReviewByStoreId(Mockito.any());
        List<ReviewResponseDto> reviewInfo = aggregatorService.findReviewByStoreId(Mockito.any());

        assertThat(reviewInfo.size(), equalTo(REVIEW_SIZE));
    }

    @Test
    public void findStoreWithReviewInNormalCase() {
        // Store Size = 2
        // Review size of Store1 = 5
        // Review size of Store2 = 3

        Mockito.doReturn(getStoreWithReview()).when(aggregatorService).findStoreInfoWithReviewByLocation(Mockito.anyFloat(), Mockito.anyFloat());
        StoreOutboundPayload storeInfoWithReviewByLocation = aggregatorService.findStoreInfoWithReviewByLocation(Mockito.anyFloat(), Mockito.anyFloat());

        assertThat(storeInfoWithReviewByLocation.getOutboundPayloads().size(), equalTo(2)); // store size
        assertThat(storeInfoWithReviewByLocation.getOutboundPayloads().get(0).getReviews().size(), equalTo(5));
        assertThat(storeInfoWithReviewByLocation.getOutboundPayloads().get(1).getReviews().size(), equalTo(3));
    }

    private List<StoreDto> getStoreList(int size) {
        List<StoreDto> storeList = new ArrayList<>();
        for(int i = 1; i <= size ; i++){
            storeList.add(StoreDto.builder().storeName("Test" + i).build());
        }
        return storeList;
    }

    private List<ReviewResponseDto> getReviewList(int size) {
        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();
        for(int i = 1; i <= size; i++){
            reviewResponseDtoList.add(ReviewResponseDto.builder().id((long)i).content("TestContenst" + i).build());
        }
        return reviewResponseDtoList;
    }

    private StoreOutboundPayload getStoreWithReview() {
        List<StoreWithReviewDto> storeWithReviewDtos = new ArrayList<>();
        StoreWithReviewDto store1 = new StoreWithReviewDto();
        store1.setStoreId(1L);
        store1.setStoreName("Test1");
        store1.setReviews(getReviewList(5));
        storeWithReviewDtos.add(store1);

        StoreWithReviewDto store2 = new StoreWithReviewDto();
        store2.setStoreId(2L);
        store2.setStoreName("Test2");
        store2.setReviews(getReviewList(3));
        storeWithReviewDtos.add(store2);

        return StoreOutboundPayload.builder().outboundPayloads(storeWithReviewDtos).build();

    }
}