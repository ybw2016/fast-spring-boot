package com.fast.springcloud.provider.web;

import com.fast.springcloud.provider.common.BaseRsp;
import com.fast.springcloud.provider.exception.ApiBizException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ApiBizErrorHandlerAdvice {
    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object validationRequestParameterException() {
        return BaseRsp.fail("-999999", "参数错误");
    }

    @ExceptionHandler(ApiBizException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handleException(ApiBizException ex, HttpServletRequest request) {
        return BaseRsp.fail(ex.getCode(), ex.getMessage());
    }
}
