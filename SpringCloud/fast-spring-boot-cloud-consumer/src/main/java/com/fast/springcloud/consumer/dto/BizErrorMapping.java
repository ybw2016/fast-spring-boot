package com.fast.springcloud.consumer.dto;

import com.google.common.collect.Maps;

import com.fast.springcloud.consumer.model.BusinessError;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * @author bowen.yan
 * @date 2018-12-05
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface BizErrorMapping {
    Map<String, BusinessError> ERROR_MAP = Maps.newHashMap();

    String getRawErrorCode();

    String getRawErrorMsg();

    @JsonIgnore
    default Map<String, BusinessError> getErrorMap() {
        return ERROR_MAP;
    }

    boolean success();
}
