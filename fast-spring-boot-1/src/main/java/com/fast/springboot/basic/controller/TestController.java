package com.fast.springboot.basic.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yanbowen
 * @date 2023-07-26
 */
@RequestMapping("/test")
@RestController
public class TestController {
    @GetMapping("/index")
    public void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("aaaaaa");
        // 构造重定向的路径:
        String payKey = req.getParameter("payKey");
        // 发送重定向响应:
        resp.sendRedirect("http://www.baidu.com?newAlipayUrl=abc&payKey=" + payKey);
    }
}
