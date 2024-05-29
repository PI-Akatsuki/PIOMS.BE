package com.akatsuki.pioms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PiomsBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PiomsBeApplication.class, args);
    }

}
