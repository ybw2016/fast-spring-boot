package com.fast.springcloud.consumer.dto.request;

import com.fast.springcloud.consumer.dto.UserSysReq;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author bowen.yan
 * @date 2018-12-03
 */
@Data
public class UserInfoReq extends UserSysReq {
    @JsonProperty("username")
    private String userName;
    private String mobile;
}
