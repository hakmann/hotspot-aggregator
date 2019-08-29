package com.sk.hotspot.aggregator.application.web;

import com.sk.hotspot.aggregator.application.service.AggregatorService;
import com.sk.hotspot.aggregator.application.service.AggregatorServiceRedis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class WebRequestContextInterceptor implements HandlerInterceptor {

    @Autowired
    private AggregatorServiceRedis aggregatorServiceRedis;

    @Autowired
    private AggregatorService aggregatorService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("Authentication....");
        String authorization = request.getHeader("Authorization");
        if ("".equals(authorization) || authorization == null ){
            throw new RuntimeException("Invalid Access token. Access token is needed in request header");
        }

        String[] auth = authorization.split(" ");// Authorization: Bearer {token}
        if (auth.length != 2) {
            throw new RuntimeException("Invalid Access token. example) Authorization: bearer ACCESS_TOKEN");
        }

        // redis check
        String loginId = aggregatorServiceRedis.getLoginIdByAccessToken(auth[1]);
        // token이 없을 경우 "" (empty string)"
        if (!"".equals(loginId) && !aggregatorService.validateToken(auth[1])) {
            log.debug("exists session in redis userId: {}", loginId);
            Long memberId = aggregatorServiceRedis.getMemberIdByLoginId(loginId);
            RequestContextUtils.findWebApplicationContext(request).getServletContext().setAttribute("memberId", memberId);
            return true;
        }else{
            throw new RuntimeException("Invalid Access token");
        }

        //return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
