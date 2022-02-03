package com.pjh.community.config;

import com.pjh.community.annotation.LoginRequired;
import com.pjh.community.interceptor.LoginRequiredInterceptor;
import com.pjh.community.interceptor.LoginTicketInterceptor;
import com.pjh.community.interceptor.MessageInterceptor;
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

    /*@Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;*/

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(testInterceptor)
        .excludePathPatterns("/**/*.css","/**/*.js","/**/*.jpg","/**/*.png","/**/*.jpeg")
        .addPathPatterns("/login","/register");

        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.jpg","/**/*.png","/**/*.jpeg");

        //registry.addInterceptor(loginRequiredInterceptor)
           //     .excludePathPatterns("/**/*.css","/**/*.js","/**/*.jpg","/**/*.png","/**/*.jpeg");

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.jpg","/**/*.png","/**/*.jpeg");

    }
}
