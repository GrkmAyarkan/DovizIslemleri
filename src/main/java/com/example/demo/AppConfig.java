package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//WebClient
import org.springframework.web.reactive.function.client.WebClient;
//RestTemlate
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

//    @Bean
//    public WebClient.Builder webClientBuilder() {
//        return WebClient.builder();
//    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
