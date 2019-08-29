package com.sk.hotspot.aggregator.application;

import com.sk.hotspot.aggregator.application.dto.*;
import com.sk.hotspot.aggregator.application.service.AggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class AggregatorController {

    @Autowired
    private AggregatorService aggregatorService;

    @GetMapping("/store/location/{x}/{y}")
    public StoreOutboundPayload findStoreInfoWithReviewByLocation(@PathVariable("x") float x, @PathVariable("y") float y) {
        return aggregatorService.findStoreInfoWithReviewByLocation(x, y);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return aggregatorService.login(loginRequestDto);
    }

    @PostMapping("/review")
    public ReviewResponseDto postReview(@RequestBody ReviewRequestDto reviewRequestDto, HttpServletRequest request) {
        ServletContext servletContext = RequestContextUtils.findWebApplicationContext(request).getServletContext();
        Object memberId = servletContext.getAttribute("memberId");
        reviewRequestDto.setCustomerId(Long.valueOf(memberId.toString()));
        return aggregatorService.postReview(reviewRequestDto);
    }
}
