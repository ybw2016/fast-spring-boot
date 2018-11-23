package com.fast.springboot.basic.resolver;

import com.fast.springboot.basic.annotation.DecryptPostJsonForm;
import com.fast.springboot.basic.annotation.DecryptPostJsonFormField;
import com.fast.springboot.basic.util.JsonUtil;
import com.fast.springboot.basic.util.LogExtUtil;
import com.fast.springboot.basic.util.ReflectionUtil;

import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Field;

import javax.servlet.ServletRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * HTTP POST FORM解密器
 *
 * @author bowen.yan
 * @date 2018-11-16
 */
@Slf4j
public class DecryptPostJsonFormResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(DecryptPostJsonForm.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        String jsonBody = IOUtils.toString(((ServletRequest) nativeWebRequest.getNativeRequest()).getInputStream());
        Object newObject = JsonUtil.fromJson(jsonBody, methodParameter.getParameterType());

        for (Field field : newObject.getClass().getDeclaredFields()) {
            if (field.getAnnotation(DecryptPostJsonFormField.class) == null) {
                continue;
            }
            Object rawValue = ReflectionUtil.getFieldValue(newObject, field);
            String newValue = (rawValue != null) ? rawValue.toString() : null;
            newValue = LogExtUtil.decryptAndLog("DecryptPostJsonFormResolver", field.getName(), newValue);
            ReflectionUtil.setFieldValue(newObject, field, newValue);
        }
        return newObject;
    }
}
