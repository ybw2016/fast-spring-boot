package com.fast.springcloud.consumer.dto;

import lombok.Data;

/**
 * 调用第三方系统接口，统一返回的报文头
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
