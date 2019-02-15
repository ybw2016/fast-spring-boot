package com.fast.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author bowen.yan
 * @date 2019-02-14
 */
//@Configuration
public class ThreadPoolConfig {
    @Bean
    Executor fixedThreadPoolExecutor() {
        return Executors.newFixedThreadPool(5);
    }
}
