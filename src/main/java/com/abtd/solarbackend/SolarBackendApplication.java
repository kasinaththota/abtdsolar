package com.abtd.solarbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SolarBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SolarBackendApplication.class, args);
    }
}