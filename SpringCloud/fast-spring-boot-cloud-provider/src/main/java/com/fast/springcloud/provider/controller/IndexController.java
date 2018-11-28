package com.fast.springcloud.provider.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bowen.yan
 * @date 2018-11-14
 */
@RestController
@Slf4j
public class IndexController {
    @RequestMapping("hello/{name}")
    public String hello(@PathVariable(value = "name") String name) {
        log.info("Spring Cloud Provider receives -> name:{}", name);
        return name + " say hello";
    }

    @RequestMapping("address")
    public String address(@RequestParam(value = "addressCode") String addressCode) {
        log.info("Spring Cloud Provider receives -> addressCode:{}", addressCode);
        return "come from " + addressCode;
    }
}
