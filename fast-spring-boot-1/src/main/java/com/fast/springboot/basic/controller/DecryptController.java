package com.fast.springboot.basic.controller;

import com.fast.springboot.basic.annotation.BizRestController;
import com.fast.springboot.basic.annotation.DecryptPathVariable;
import com.fast.springboot.basic.annotation.DecryptPostForm;
import com.fast.springboot.basic.annotation.DecryptPostJsonForm;
import com.fast.springboot.basic.annotation.DecryptRequestParam;
import com.fast.springboot.basic.model.AddressFormJackson;
import com.fast.springboot.basic.model.AddressJackson;
import com.fast.springboot.basic.model.AddressJsonManualForm;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bowen.yan
 * @date 2018-11-16
 */
@BizRestController
@RequestMapping("/security")
@Slf4j
public class DecryptController {
    /**
     * 功能：1. HTTP-GET 请求参数自动解密; 2. 输出结果自动加密;
     *
     * @see com.fast.springboot.basic.resolver.DecryptRequestParamResolver
     * @see com.fast.springboot.basic.serializer.EncryptOutputSerializer
     * 请求url：http://127.0.0.1:8080/security/decrypt?username=emhhbmdzYW4=&password=MTIzNDU2&mobile=13811112222
     * 运行解析过程：
     * userName=emhhbmdzYW4= -> zhangsan
     * password=MTIzNDU2 -> 123456
     * mobile=13811112222 -> 13811112222
     * 运行结果：
     * "{\"userName\":\"emhhbmdzYW4=\",\"password\":\"123456\",\"mobile\":\"13811112222\",\"address\":{\"addressCode\":\"MTEwMDIy\",\"addressName\":\"北京海淀\"}}"
     */
    @RequestMapping("/decrypt")
    public void decryptName(@DecryptRequestParam String username, @DecryptRequestParam String password, String mobile) {
        // return new User(username, password, mobile, new Address("110022", "北京海淀"));
        log.info("decryptName handle completed!");
    }

    /**
     * 功能：HTTP POST JSON参数的自动解密
     *
     * @see com.fast.springboot.basic.serializer.DecryptInputSerializer
     * 请求url：http://127.0.0.1:8080/security/decrypt/post-json/jackson
     * 入参：
     * {
     * "addressCode":"MTEwMDIy",
     * "addressName":"bj",
     * "subAddressJackson":{
     * "addressCode":"MTEwMDMz",
     * "addressName":"hz"
     * }
     * }
     * 请求类型Content-Type:  application/json
     * 运行结果：
     * "{\"addressCode\":\"110022\",\"addressName\":\"bj\",\"subAddressJackson\":{\"addressCode\":\"110033\",\"addressName\":\"hz\"}}"
     */
    @PostMapping("/decrypt/post-json/jackson")
    public AddressJackson decryptAddressJson(@RequestBody AddressJackson address) {
        return address;
    }

    /**
     * 功能：HTTP POST JSON参数的自动解密
     *
     * @see com.fast.springboot.basic.serializer.DecryptInputSerializer
     * 请求url：http://127.0.0.1:8080/security/decrypt/post-json/manual
     * 入参：
     * {
     * "addressCode":"MTEwMDIy",
     * "addressName":"bj",
     * "subAddressJackson":{
     * "addressCode":"MTEwMDMz",
     * "addressName":"hz"
     * }
     * }
     * 请求类型Content-Type:  application/json
     * 运行结果：
     * "{\"addressCode\":\"110022\",\"addressName\":\"bj\",\"subAddressJackson\":{\"addressCode\":\"110033\",\"addressName\":\"hz\"}}"
     */
    @PostMapping("/decrypt/post-json/manual")
    public AddressJsonManualForm decryptAddressJson2(@DecryptPostJsonForm AddressJsonManualForm address) {
        return address;
    }

    /**
     * 功能：HTTP POST FORM参数的自动解密
     *
     * @see com.fast.springboot.basic.resolver.DecryptPostFormResolver
     * 请求url： http://127.0.0.1:8080/security/decrypt/post-form
     * 参数：
     * addressCode MTEwMDIy
     * addressName bj
     * 请求类型Content-Type:  multipart/form-data, application/x-www-form-urlencoded
     * 运行结果：
     * "{\"addressCode\":\"110022\",\"addressName\":\"bj\"}"
     */
    @PostMapping("/decrypt/post-form")
    public AddressFormJackson decryptAddressForm(@DecryptPostForm AddressFormJackson address) {
        return address;
    }

    /**
     * 功能：HTTP GET PathVariable参数的自动解密
     *
     * @see com.fast.springboot.basic.resolver.DecryptPathVariableResolver
     * 请求url：http://localhost:8080/security/decrypt/param/emhhbmdzYW4=/MTIzNDU2?address=bj
     * 请求参数：无
     * 返回结果：userName:zhangsan, password:123456, address:bj
     */
    @RequestMapping("/decrypt/param/{username}/{password}")
    public String decryptPathVariable(@DecryptPathVariable String username,
                                      @PathVariable String password,
                                      @RequestParam String address) {
        return String.format("userName:%s, password:%s, address:%s", username, password, address);
    }
}
