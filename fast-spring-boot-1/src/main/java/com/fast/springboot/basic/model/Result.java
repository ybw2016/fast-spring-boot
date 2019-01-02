package com.fast.springboot.basic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bowen.yan
 * @date 2018-11-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private static final int SUCCESS_CODE = 0;
    private int code;
    private String msg;
    private Object data;

    public static Result buildSuccess(Object data) {
        return new Result(SUCCESS_CODE, "", data);
    }

    public static Result buildFailed(int code, String msg) {
        return new Result(code, "", null);
    }
}
