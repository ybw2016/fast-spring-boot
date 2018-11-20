package com.fast.springboot.basic.resolver;

import com.fast.springboot.basic.annotation.DecryptPostForm;
import com.fast.springboot.basic.annotation.DecryptPostFormField;
import com.fast.springboot.basic.util.LogExtUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

/**
 * HTTP POST FORM解密器
 *
 * @author bowen.yan
 * @date 2018-11-16
 */
@Slf4j
public class DecryptPostFormResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(DecryptPostForm.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        Object obj = BeanUtils.instantiate(methodParameter.getParameterType());
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(obj);
        Iterator<String> paramNames = nativeWebRequest.getParameterNames();
        Map<String, Field> fieldMap = Stream.of(methodParameter.getParameterType().getDeclaredFields())
                .collect(Collectors.toMap(f -> f.getName(), f -> f));

        while (paramNames.hasNext()) {
            String paramName = paramNames.next();
            Object rawValue = nativeWebRequest.getParameter(paramName);
            String newValue = Optional.ofNullable(rawValue).isPresent() ? rawValue.toString() : null;
            if (fieldMap.get(paramName).getAnnotation(DecryptPostFormField.class) != null) {
                newValue = LogExtUtil.decryptAndLog("DecryptPostFormResolver", paramName, newValue);
            }
            wrapper.setPropertyValue(paramName, newValue);
        }
        return obj;
    }
}
