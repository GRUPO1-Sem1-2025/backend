package com.example.Login.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplica a todas las rutas
                        .allowedOrigins(
                        		"http://localhost:5173", // Permitir origen de tu app React
                        		"http://localhost:8081" // Permitir origen de tu app ReactNative
                        		) // Permitir origen de tu app React
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD","TRACE") // Métodos permitidos
                        .allowedHeaders("*");
            }
        };
    }
}