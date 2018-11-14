package com.fast.springcloud.provider.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bowen.yan
 * @date 2018-11-14
 */
@RestController
public class IndexController {
    @RequestMapping("hello/{name}")
    public String hello(@PathVariable String name) {
        return name + " say hello";
    }
}
