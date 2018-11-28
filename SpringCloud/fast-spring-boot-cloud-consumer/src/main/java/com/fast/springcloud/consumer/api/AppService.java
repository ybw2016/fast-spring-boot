package com.fast.springcloud.consumer.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author bowen.yan
 * @date 2018-11-28
 */
@FeignClient("${app.service-name}")
//@FeignClient(url = "http://localhost:8091", name = "appService")
public interface AppService {
    @RequestMapping(method = RequestMethod.GET, value = "hello/{name}")
    String getHelloWorld(@RequestParam(value = "name") String name);

    @RequestMapping(method = RequestMethod.GET, value = "address")
    String getAddress(@RequestParam(value = "addressCode") String addressCode);
}
