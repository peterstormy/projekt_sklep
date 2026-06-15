package com.example.projekt_sklep.config;

import jakarta.servlet.http.HttpSessionListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public ServletListenerRegistrationBean<HttpSessionListener> sessionListenerBean(
            SessionListener sessionListener) {
        return new ServletListenerRegistrationBean<>(sessionListener);
    }
}
