package com.fast.springcloud.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * https://blog.csdn.net/ycxzz/article/details/78324587
 */
@EnableEurekaServer
@SpringBootApplication
public class SpringCloudServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudServerApplication.class, args);
    }
}
