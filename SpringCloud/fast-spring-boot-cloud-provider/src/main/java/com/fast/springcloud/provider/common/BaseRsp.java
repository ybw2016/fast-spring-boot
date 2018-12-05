package com.fast.springcloud.provider.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import lombok.Data;

@Data
public class BaseRsp<T> implements Serializable {
    private static final long serialVersionUID = -6241701394952098042L;
    private static final String SUCCESS_CODE = "0";

    @JsonProperty("code")
    private String code;
    @JsonProperty("msg")
    private String message;
    private T data;

    public BaseRsp() {
    }

    public BaseRsp(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> BaseRsp<T> success(T data) {
        return new BaseRsp<T>(SUCCESS_CODE, "", data);
    }

    public static BaseRsp fail(String code, String message) {
        return new BaseRsp(code, message, null);
    }
}
