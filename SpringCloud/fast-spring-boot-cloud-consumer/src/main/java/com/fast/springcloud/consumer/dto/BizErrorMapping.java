package com.fast.springcloud.consumer.dto;

import com.google.common.collect.Maps;

import com.fast.springcloud.consumer.model.BusinessError;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

/**
 * @author bowen.yan
 * @date 2018-12-05
 */
public interface BizErrorMapping {
    @JsonIgnore
    default Map<String, BusinessError> getErrorMap() {
        return Maps.newHashMap();
    }
}
