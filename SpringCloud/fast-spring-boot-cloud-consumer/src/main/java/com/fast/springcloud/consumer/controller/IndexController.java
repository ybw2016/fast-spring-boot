package com.fast.springcloud.consumer.controller;

import com.fast.springcloud.consumer.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bowen.yan
 * @date 2018-11-14
 */
@RestController
@Slf4j
public class IndexController {
    @Autowired
    private UserService userService;

    @GetMapping("hello")
    public String hello(String name) {
        return userService.callHello(name);
    }

    @GetMapping("hello2")
    public String hello2(String name) {
        return userService.callHello2(name);
    }

    @GetMapping("address")
    public String address(String addressCode) {
        String result = userService.getAddress(addressCode);
        log.info("Eureka Result ------> {}", result);
        return result;
    }
}
