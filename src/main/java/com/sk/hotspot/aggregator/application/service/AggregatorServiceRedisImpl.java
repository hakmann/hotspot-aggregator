package com.sk.hotspot.aggregator.application.service;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class AggregatorServiceRedisImpl implements AggregatorServiceRedis {

    @Value("${redis.uri}")
    private String REDIS_URI;

    @Override
    public void saveFavoriteStoreList() {
        // authorization 을 redis에서 확인
        RedisAsyncCommands<String, String> asyncCommands = null;
        try {
            RedisClient redisClient = RedisClient.create(REDIS_URI);
            StatefulRedisConnection<String, String> connection = redisClient.connect();
            asyncCommands = connection.async();
            // 1. Simple String(key:value)
            //RedisFuture<String> redisResult = asyncCommands.get(authorization);

            // 2. Hash
            RedisFuture<Map<String, String>> redisResult = asyncCommands.hgetall("sample");
            Map<String, String> cached = redisResult.get(5, TimeUnit.SECONDS);   // timeout in 5 seconds

            //if(StringUtils.isEmpty(cached) == false) {
            if(!cached.isEmpty()) {
                // valid 경우 끝
                log.info("Found matching token in redis");
                return;
            }
        } catch(RedisConnectionException ex) {
            // redis 접속 장애시, redis 인증은 안된 걸로 생각하고 다음으로 넘어감
            asyncCommands = null;
        } catch(ExecutionException ex) {
            // redis 접속 장애시, redis 인증은 안된 걸로 생각하고 다음으로 넘어감
            asyncCommands = null;
        } catch(InterruptedException ex) {
            // redis 접속 장애시, redis 인증은 안된 걸로 생각하고 다음으로 넘어감
            asyncCommands = null;
        } catch(TimeoutException ex) {
            // redis 접속 장애시, redis 인증은 안된 걸로 생각하고 다음으로 넘어감
            asyncCommands = null;
        }
    }
}
