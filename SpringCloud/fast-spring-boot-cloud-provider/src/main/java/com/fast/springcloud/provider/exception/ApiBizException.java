package com.fast.springcloud.provider.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bowen.yan
 * @date 2018-12-05
 */
@Slf4j
@Data
public class ApiBizException extends RuntimeException {
    private String code;

    public ApiBizException() {
        super();
    }

    public ApiBizException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ApiBizException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ApiBizException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    protected ApiBizException(String code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
}
