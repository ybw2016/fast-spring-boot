package com.fast.redis.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bowen.yan
 * @date 2019-02-14
 */
@Slf4j
@Service
public class CalculateService {
    public void calculate(String taskId) {
        log.info("calculate runs completed! -> taskId:{}", taskId);
    }
}
