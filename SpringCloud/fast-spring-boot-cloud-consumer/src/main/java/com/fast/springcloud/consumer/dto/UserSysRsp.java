package com.fast.springcloud.consumer.dto;

import lombok.Data;

/**
 * 公共返回报文头
 *
 * @author bowen.yan
 * @date 2018-12-03
 */
@Data
public class UserSysRsp {
    private String code;
    private String msg;
    private Object data;
}
