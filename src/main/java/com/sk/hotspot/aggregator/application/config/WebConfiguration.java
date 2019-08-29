package com.sk.hotspot.aggregator.application.config;

import com.sk.hotspot.aggregator.application.web.WebRequestContextInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Autowired
    private WebRequestContextInterceptor webRequestContextInterceptor;

    public WebConfiguration(){
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webRequestContextInterceptor).addPathPatterns(new String[]{"/api/v1/**"});
    }
}
