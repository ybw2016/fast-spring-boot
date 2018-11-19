package com.fast.springboot.basic.controller;

import com.fast.springboot.basic.annotation.BizRestController;
import com.fast.springboot.basic.annotation.DecryptRequestParam;
import com.fast.springboot.basic.model.Address;
import com.fast.springboot.basic.model.AddressFormJackson;
import com.fast.springboot.basic.model.AddressJackson;
import com.fast.springboot.basic.model.User;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

/**
 * 说明：
 * 一。加密：（将数据吐出到端）
 * 1. 通过EncryptOutputSerializer类实现；
 * 二。解密：（解析入参）
 * 1. GET请求过来的入参，通过DecryptParamResolver实现数据解密；
 * 2. POST RequestBody方式，通过DecryptInputSerializer类实现；
 *
 * @author bowen.yan
 * @date 2018-11-16
 */
@BizRestController
@RequestMapping("/security")
@Slf4j
public class DecryptController {
    /**
     * 请求url：http://127.0.0.1:8080/security/decrypt?username=emhhbmdzYW4=&password=MTIzNDU2&mobile=13811112222
     * 运行解析过程：
     * username=emhhbmdzYW4= -> zhangsan
     * password=MTIzNDU2 -> 123456
     * mobile=13811112222 -> 13811112222
     * 运行结果：
     * "{\"userName\":\"emhhbmdzYW4=\",\"password\":\"MTIzNDU2\",\"mobile\":\"13811112222\",\"address\":{\"addressCode\":\"MTEwMDIy\",\"addressName\":\"北京海淀\"}}"
     */
    @RequestMapping("/decrypt")
    public User decryptName(@DecryptRequestParam String username, @DecryptRequestParam String password, String mobile) {
        return new User(username, password, mobile, new Address("110022", "北京海淀"));
    }

    /**
     * 请求url：http://127.0.0.1:8080/security/decrypt/post-json
     * 入参：
     * {
     * "addressCode":"MTEwMDIy",
     * "addressName":"bj",
     * "subAddressJackson":{
     * "addressCode":"MTEwMDMz",
     * "addressName":"hz"
     * }
     * }
     * 运行结果：
     * "{\"addressCode\":\"110022\",\"addressName\":\"bj\",\"subAddressJackson\":{\"addressCode\":\"110033\",\"addressName\":\"hz\"}}"
     */
    @PostMapping("/decrypt/post-json")
    public AddressJackson decryptAddressJson(@RequestBody AddressJackson address) {
        log.info("decryptAddressJson -> {}", address);
        return address;
    }

    /**
     * 无法自动解密
     * 请求url： http://127.0.0.1:8080/security/decrypt/post-form
     * 参数：
     * addressCode MTEwMDIy
     * addressName bj
     * 运行结果：
     * "{\"addressCode\":\"MTEwMDIy\",\"addressName\":\"bj\"}"
     */
    @PostMapping("/decrypt/post-form")
    public AddressFormJackson decryptAddressForm(AddressFormJackson address) {
        log.info("decryptAddressForm -> {}", address);
        return address;
    }

    /**
     * 无法自动解密
     * 请求url：http://localhost:8080/security/decrypt/param/emhhbmdzYW4=
     * 请求参数：无
     * 返回结果：emhhbmdzYW4=
     */
    @RequestMapping("/decrypt/param/{username}")
    @ResponseBody
    public String testPathVariable(@PathVariable String username) {
        log.info("testPathVariable  -> {}", username);
        return username;
    }
}
