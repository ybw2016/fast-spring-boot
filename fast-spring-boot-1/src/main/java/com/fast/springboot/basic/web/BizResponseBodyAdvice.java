package com.fast.springboot.basic.web;

import com.fast.springboot.basic.annotation.BizRestController;
import com.fast.springboot.basic.model.EmptyObject;
import com.fast.springboot.basic.model.Result;
import com.fast.springboot.basic.util.JsonUtil;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bowen.yan
 * @date 2018-11-16
 */
@Slf4j
@RestControllerAdvice(annotations = {BizRestController.class})
public class BizResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
//        return Object.class.isAssignableFrom(returnType.getMethod().getReturnType())
//                || Void.class.isAssignableFrom(returnType.getMethod().getReturnType());
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        log.info("beforeBodyWrite enters!");
        Object retData;
        if (body != null) {
            retData = Result.buildSuccess(JsonUtil.toJsonString(body));
            log.info("BizResponseBodyAdvice beforeBodyWrite -> retData：{}", retData);
        } else {
            retData = Result.buildSuccess(JsonUtil.toJsonString(new EmptyObject()));
        }
        return retData;
    }
}
