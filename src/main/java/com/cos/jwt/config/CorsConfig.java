package com.cos.jwt.config;

// 주의 반드시 : org.springframework.web.filter.CorsFilter 여야 함
import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    //
    @Bean
    public CorsFilter corsFilter() { //Cross Origin Filter
        //
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 내서버가 응답 할때 json을 자바스크립트에서 받을수(처리) 할 수 있게 할지를 설정하는 것
        config.addAllowedOrigin("*"); // 모든 ip에 응답을  모두 허용
        config.addAllowedHeader("*"); // 어떤 Header에 응답을 모두 허용
        config.addAllowedMethod("*"); // get, post, put, delete, patch 무슨 요청이든 허용

        source.registerCorsConfiguration("/api/**", config); // "/api/로 들어오는 모든 요청은 config 설정을 따른다
        return new CorsFilter(source);

    }
}
