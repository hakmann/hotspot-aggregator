package com.sk.hotspot.aggregator.application;

import com.sk.hotspot.aggregator.application.service.AggregatorServiceRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class MemberSessionController {
    @Autowired
    private AggregatorServiceRedis redisService;

//    @PostMapping("/session/{memberId}")
//    public HttpEntity saveMemberSession(@PathVariable("memberId") String memberId) {
//        redisService.saveSessionForMember(memberId);
//        return ResponseEntity.status(201).body(null);
//    }
//
//    @GetMapping("/session/{memberId}")
//    public HttpEntity<String> getMemberSession(@PathVariable("memberId") String memberId) {
//        return ResponseEntity.status(200).body(redisService.getSessionForMember(memberId) ? "TRUE" : "FALSE");
//    }
}
