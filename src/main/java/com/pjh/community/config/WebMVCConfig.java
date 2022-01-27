package com.pjh.community.config;

import com.pjh.community.interceptor.LoginTicketInterceptor;
import com.pjh.community.interceptor.TestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private TestInterceptor testInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(testInterceptor)
        .excludePathPatterns("/**/*.css","/**/*.js","/**/*.jpg","/**/*.png","/**/*.jpeg")
        .addPathPatterns("/login","/register");

        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.jpg","/**/*.png","/**/*.jpeg");

    }
}
