package com.fast.redis.task;

import com.fast.redis.config.SpringContextHolder;
import com.fast.redis.service.CalculateService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class CalculateTaskExecutor implements Runnable {
    private String taskId;// 任务流水号id

    @Override
    public void run() {
        // 手动new出来的对象的方中，使用Spring管理的bean，可以通过两种方式使用Spring管理的bean
        // 1. SpringContextHolder.getBean("beanName");
        // 2. 外面AutoWired再传进来;
        CalculateService calculateService = (CalculateService) SpringContextHolder.getBean("calculateService");
        calculateService.calculate(taskId);
    }
}
