package com.fast.springcloud.consumer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bowen.yan
 * @date 2018-12-05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessError {
    private String bizErrorCode;
    private String bizErrorMsg;
}
