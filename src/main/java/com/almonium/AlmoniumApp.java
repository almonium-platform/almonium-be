package com.almonium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class AlmoniumApp extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(AlmoniumApp.class, args);
    }
}
