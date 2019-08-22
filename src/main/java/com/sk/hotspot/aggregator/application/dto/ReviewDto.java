package com.sk.hotspot.aggregator.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    //
    Long reviewId;
    private String content;
    private Date registDate;
    private Date updateDate;
}
