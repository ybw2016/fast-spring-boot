package com.fast.springcloud.consumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 公共请求报文头
 *
 * @author bowen.yan
 * @date 2018-12-03
 */
@Data
public class UserSysReq {
    private String serialId;
    private String fromSystem;
    private Long timestamp;
    private Integer pageNo;
    private Integer pageSize;

    @JsonProperty("trans_id")
    public String getTransId() {
        return serialId;
    }
}
