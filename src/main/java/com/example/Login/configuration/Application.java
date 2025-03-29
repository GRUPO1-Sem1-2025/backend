package com.example.Login.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.Login")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}