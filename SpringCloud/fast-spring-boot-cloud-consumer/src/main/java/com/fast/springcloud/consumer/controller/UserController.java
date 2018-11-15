package com.fast.springcloud.consumer.controller;

import com.fast.springcloud.consumer.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bowen.yan
 * @date 2018-11-14
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("hello")
    public String hello(String name) {
        String result = userService.callHello(name);
        return result;
    }
}
