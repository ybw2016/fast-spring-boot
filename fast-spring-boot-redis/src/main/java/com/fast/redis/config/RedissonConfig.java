package com.fast.redis.config;

import com.google.common.collect.Maps;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.RedissonNode;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.RedissonNodeConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Redisson配置类： 默认开启redissonNode节点
 */
@Configuration
public class RedissonConfig {
    public static final String REDIS_QUEUE_KEY = "fast_spring_boot_queue_jobs";

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Integer port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database}")
    private Integer database;

    @Bean
    public Config config() {
        Config config = new Config();
        String address = "redis://" + redisHost + ":" + port;
        if (StringUtils.isNotBlank(password) && StringUtils.isNotEmpty(password)) {
            config.useSingleServer().setDatabase(database).setAddress(address).setPassword(password);
        } else {
            config.useSingleServer().setDatabase(database).setAddress(address);
        }
        return config;
    }

    @Bean
    public RedissonClient redissonClient(Config config) {
        return Redisson.create(config);
    }

    @Bean
    public RedissonNode redissonNode(Config config) {
        RedissonNodeConfig nodeConfig = new RedissonNodeConfig(config);
        Map<String, Integer> workers = Maps.newHashMap();

        workers.put(REDIS_QUEUE_KEY, 1);
        nodeConfig.setExecutorServiceWorkers(workers);
        RedissonNode node = RedissonNode.create(nodeConfig);
        node.start();
        return node;
    }

    @Bean
    public RScheduledExecutorService rScheduledExecutorService(RedissonClient client) {
        return client.getExecutorService(REDIS_QUEUE_KEY);
    }
}
