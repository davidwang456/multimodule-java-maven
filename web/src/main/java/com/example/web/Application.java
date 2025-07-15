package com.example.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 启动类
 */
@SpringBootApplication(scanBasePackages = "com.example")
@MapperScan("com.example.service.repository")
public class Application {
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
} 