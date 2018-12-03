package com.fast.springcloud.consumer.dto.request;

import com.fast.springcloud.consumer.dto.ReqBase;

import lombok.Data;

/**
 * @author bowen.yan
 * @date 2018-12-03
 */
@Data
public class UserInfoReq extends ReqBase {
    private String username;
    private String mobile;
}
