package com.fast.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class FastSpringBootRedisApplication {
    public static void main(String[] args) {
        SpringApplication.run(FastSpringBootRedisApplication.class, args);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
