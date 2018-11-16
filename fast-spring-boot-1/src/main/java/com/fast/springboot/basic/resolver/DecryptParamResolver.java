package com.fast.springboot.basic.resolver;

import com.fast.springboot.basic.annotation.DecryptRequestParam;
import com.fast.springboot.basic.util.Base64Util;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bowen.yan
 * @date 2018-11-16
 */
@Slf4j
public class DecryptParamResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //如果函数包含我们的自定义注解，那就走resolveArgument()函数
        return methodParameter.hasParameterAnnotation(DecryptRequestParam.class)
                || methodParameter.hasParameterAnnotation(PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest servletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        for (Map.Entry<String, String[]> entry : servletRequest.getParameterMap().entrySet()) {
            if (methodParameter.getParameterName().equals(entry.getKey())) {
                String newValue = Base64Util.decrypt(entry.getValue()[0]);
                log.info("DecryptParamResolver -> key:{}, rawValue:{}, newValue:{}",
                        entry.getKey(), ArrayUtils.toString(entry.getValue()), newValue);
                return newValue;
            }
        }

        return null;
    }
}
