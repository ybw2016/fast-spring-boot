package com.fast.redis.util;

import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class RScheduledExecutorServiceHelper {
    public static final String REDIS_QUEUE_KEY = "fast_spring_boot_queue_jobs";
    private static RScheduledExecutorService service = null;

    public static RScheduledExecutorService getService() {
        return service;
    }

    @Autowired
    public void setRedissonClient(RedissonClient client) {
        if (service == null) {
            service = client.getExecutorService(REDIS_QUEUE_KEY);
        }
    }
}
