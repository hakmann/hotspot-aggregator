package com.sk.hotspot.aggregator.application.service;

import com.sk.hotspot.aggregator.application.dto.LoginResponseDto;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class AggregatorServiceRedisImpl implements AggregatorServiceRedis {

    @Value("${redis.uri}")
    private String REDIS_URI;

    @Autowired
    private AggregatorService aggregatorService;

    @Override
    public void saveSessionForMember(LoginResponseDto responseDto) {
        // User Session
        RedisAsyncCommands<String, String> asyncCommands = null;
        try {
            RedisClient redisClient = RedisClient.create(REDIS_URI);
            StatefulRedisConnection<String, String> connection = redisClient.connect();
            asyncCommands = connection.async();
            // 1. Simple String(key:value)
            RedisFuture<String> redisResult = asyncCommands.get(responseDto.getAccessToken());
            String cached = redisResult.get(3, TimeUnit.SECONDS);

            // cache된 세션이 있다면 삭제 후 다시 넣음.
            if(!StringUtils.isEmpty(cached)) {
                asyncCommands.del(responseDto.getAccessToken());
                asyncCommands.del(responseDto.getLoginId());
            }

            asyncCommands.set(responseDto.getAccessToken(), responseDto.getLoginId());
            asyncCommands.expire(responseDto.getAccessToken(), 30000L);
            //asyncCommands.set(responseDto.getLoginId(), String.valueOf(aggregatorService.findMemberIdByLoginId(responseDto.getLoginId())));
            asyncCommands.set(responseDto.getLoginId(), String.valueOf(1123));
            asyncCommands.expire(responseDto.getLoginId(), 30000L);

            connection.close();
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

    @Override
    public void saveFavoriteStoreList() {
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

            connection.close();
        } catch(RedisConnectionException ex) {
            asyncCommands = null;
        } catch(ExecutionException ex) {
            asyncCommands = null;
        } catch(InterruptedException ex) {
            asyncCommands = null;
        } catch(TimeoutException ex) {
            asyncCommands = null;
        }
    }

    @Override
    public String getLoginIdByAccessToken(String accessToken) {
        RedisAsyncCommands<String, String> asyncCommands = null;
        try {
            RedisClient redisClient = RedisClient.create(REDIS_URI);
            StatefulRedisConnection<String, String> connection = redisClient.connect();
            asyncCommands = connection.async();

            RedisFuture<String> redisResult = asyncCommands.get(accessToken);
            String token = redisResult.get(3, TimeUnit.SECONDS);
            connection.close();

            return token;

        } catch(RedisConnectionException ex) {
            asyncCommands = null;
            return "";
        } catch(ExecutionException ex) {
            asyncCommands = null;
            return "";
        } catch(InterruptedException ex) {
            asyncCommands = null;
            return "";
        } catch(TimeoutException ex) {
            asyncCommands = null;
            return "";
        }
    }

    @Override
    public Long getMemberIdByLoginId(String loginId) {
        // User Session
        RedisAsyncCommands<String, String> asyncCommands = null;
        try {
            RedisClient redisClient = RedisClient.create(REDIS_URI);
            StatefulRedisConnection<String, String> connection = redisClient.connect();
            asyncCommands = connection.async();
            // 1. Simple String(key:value)
            RedisFuture<String> redisResult = asyncCommands.get(loginId);
            String cached = redisResult.get(3, TimeUnit.SECONDS);

            connection.close();
            return Long.valueOf(cached);
        } catch(RedisConnectionException ex) {
            // redis 접속 장애시, redis 인증은 안된 걸로 생각하고 다음으로 넘어감
            asyncCommands = null;
            return 0L;
        } catch(ExecutionException ex) {
            // redis 접속 장애시, redis 인증은 안된 걸로 생각하고 다음으로 넘어감
            asyncCommands = null;
            return 0L;
        } catch(InterruptedException ex) {
            // redis 접속 장애시, redis 인증은 안된 걸로 생각하고 다음으로 넘어감
            asyncCommands = null;
            return 0L;
        } catch(TimeoutException ex) {
            // redis 접속 장애시, redis 인증은 안된 걸로 생각하고 다음으로 넘어감
            asyncCommands = null;
            return 0L;
        }
    }
}
