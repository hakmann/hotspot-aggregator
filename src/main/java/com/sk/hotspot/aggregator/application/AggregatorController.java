package com.sk.hotspot.aggregator.application;

import com.sk.hotspot.aggregator.application.dto.LoginRequestDto;
import com.sk.hotspot.aggregator.application.dto.LoginResponseDto;
import com.sk.hotspot.aggregator.application.dto.StoreOutboundPayload;
import com.sk.hotspot.aggregator.application.service.AggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
