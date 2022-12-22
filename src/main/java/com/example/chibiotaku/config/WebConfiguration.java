package com.example.chibiotaku.config;

import com.example.chibiotaku.interceptor.AuthenticatorInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new AuthenticatorInterceptor()).addPathPatterns(
                "/manga/add",
                "/animes/add",
                "/animes/{id}/add",
                "/manga/{id}/add");

    }
}
