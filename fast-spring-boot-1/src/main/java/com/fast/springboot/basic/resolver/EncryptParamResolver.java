package com.fast.springboot.basic.resolver;

import com.fast.springboot.basic.annotation.DecryptParam;
import com.fast.springboot.basic.annotation.EncryptParam;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author bowen.yan
 * @date 2018-11-16
 */
public class EncryptParamResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //如果函数包含我们的自定义注解，那就走resolveArgument()函数
        return methodParameter.hasParameterAnnotation(EncryptParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        System.out.println("EncryptParamResolver:resolveArgument");
        DecryptParam decryptParam = methodParameter.getParameterAnnotation(DecryptParam.class);
        System.out.println("decryptParam -> " + decryptParam);
        return null;
    }
}
