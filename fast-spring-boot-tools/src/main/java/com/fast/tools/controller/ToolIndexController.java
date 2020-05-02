package com.fast.tools.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bowen.yan
 * @date 2019-01-05
 */
@RestController
public class ToolIndexController {
    @RequestMapping(value = {"/"})
    public String index() {
        return "hello, tool index controller!";
    }
}
