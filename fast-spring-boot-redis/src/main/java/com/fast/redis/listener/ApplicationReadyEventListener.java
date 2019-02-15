package com.fast.redis.listener;

import com.fast.redis.service.CalculateService;
import com.fast.redis.task.CalculateTaskExecutor;

import org.redisson.api.RScheduledExecutorService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bowen.yan
 * @date 2019-02-14
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {
    private final RScheduledExecutorService rScheduledExecutorService;// redisson bean managed by spring
    //private final Executor fixedThreadPoolExecutor;// jdk bean by spring
    private final CalculateService calculateService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("fast-spring-boot-redis starts executing...");

        executeByRedisson();
        //executeByThreadPool();
    }

    private void executeByRedisson() {
        rScheduledExecutorService.schedule(
                new CalculateTaskExecutor("T2019000001"),
                1,
                TimeUnit.SECONDS
        );
    }

    private void executeByThreadPool() {
        class Task implements Runnable {
            private String taskId;

            public Task(String taskId) {
                this.taskId = taskId;
            }

            @Override
            public void run() {
                calculateService.calculate(taskId);
            }
        }

        Executor fixedThreadPoolExecutor = Executors.newFixedThreadPool(5);
        fixedThreadPoolExecutor.execute(new Task("T2019000001"));
    }
}

