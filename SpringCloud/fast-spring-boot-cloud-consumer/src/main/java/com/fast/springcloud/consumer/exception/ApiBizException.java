package com.fast.springcloud.consumer.exception;

/**
 * @author bowen.yan
 * @date 2018-12-03
 */
public class ApiBizException extends RuntimeException {
    public ApiBizException() {
        super();
    }

    public ApiBizException(String message) {
        super(message);
    }

    public ApiBizException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiBizException(Throwable cause) {
        super(cause);
    }

    protected ApiBizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
