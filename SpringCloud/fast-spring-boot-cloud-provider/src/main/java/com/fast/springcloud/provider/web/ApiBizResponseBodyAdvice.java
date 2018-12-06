package com.fast.springcloud.provider.web;

import com.fast.springcloud.provider.annotation.ApiBizController;
import com.fast.springcloud.provider.common.BaseRsp;
import com.fast.springcloud.provider.dto.response.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(annotations = {ApiBizController.class})
public class ApiBizResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        return Response.class.isAssignableFrom(methodParameter.getMethod().getReturnType());
    }

    @Override
    public Object beforeBodyWrite(
            Object returnValue, MethodParameter methodParameter, MediaType mediaType,
            Class<? extends HttpMessageConverter<?>> converterType,
            ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        printLogData(methodParameter, serverHttpRequest, returnValue);
        return BaseRsp.success(returnValue);
    }

    private void printLogData(MethodParameter methodParameter, ServerHttpRequest serverHttpRequest, Object result) {
        StringBuilder builder = new StringBuilder("\n=======================请求上下文=======================\n");
        Method method = methodParameter.getMethod();
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String methodPath = className + "." + methodName + "()";
        String url = Optional.ofNullable(serverHttpRequest.getURI())
                .map(uri -> {
                    String path = StringUtils.defaultIfBlank(uri.getPath(), StringUtils.EMPTY);
                    String query = StringUtils.isNotBlank(uri.getQuery()) ? "?" + uri.getQuery() : StringUtils.EMPTY;
                    return path + query;
                }).orElse(StringUtils.EMPTY);
        builder.append("\n调用地址: ").append(url);
        builder.append("\n调用方法: ").append(methodPath);
        builder.append("\nheaders: ");

        HttpHeaders headers = serverHttpRequest.getHeaders();
        for (String headerKey : headers.keySet()) {
            builder.append(headerKey).append(":").append(headers.get(headerKey)).append(" ; ");
        }

        if (result != null) {
            try {
                String jsonString = mapper.writeValueAsString(result);
                builder.append("\n返回数据:\n").append(jsonString);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        log.info(builder.toString());
    }
}
