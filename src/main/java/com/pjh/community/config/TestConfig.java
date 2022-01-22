package com.pjh.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.logging.SimpleFormatter;

@Configuration
public class TestConfig {

    @Bean
    public SimpleDateFormat SimpleDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
