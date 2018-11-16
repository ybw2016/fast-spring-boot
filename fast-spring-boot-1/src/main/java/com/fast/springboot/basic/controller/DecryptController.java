package com.fast.springboot.basic.controller;

import com.fast.springboot.basic.annotation.BizRestController;
import com.fast.springboot.basic.annotation.DecryptParam;
import com.fast.springboot.basic.model.Address;
import com.fast.springboot.basic.model.User;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author bowen.yan
 * @date 2018-11-16
 */
@BizRestController
@RequestMapping("/security")
public class DecryptController {
    /**
     * 请求url：http://127.0.0.1:8080/security/decrypt?username=emhhbmdzYW4=&password=bGlzaQ==&mobile=13811112222
     * 运行结果：
     * zhangsan
     * lisi
     * 13811112222
     */
    @RequestMapping("/decrypt")
    public User decryptName(@DecryptParam String username, @DecryptParam String password, String mobile) {
        return new User(username, password, mobile, new Address("110022", "北京海淀"));
    }

    @PostMapping("/decrypt/post")
    public User decryptName(@RequestBody Address address) {
        System.out.println("address -> " + address);
        return null;
    }
}
