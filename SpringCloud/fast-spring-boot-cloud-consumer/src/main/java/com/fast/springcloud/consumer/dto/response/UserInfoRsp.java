package com.fast.springcloud.consumer.dto.response;

import lombok.Data;

/**
 * @author bowen.yan
 * @date 2018-12-03
 */
@Data
public class UserInfoRsp {
    private String username;
    private boolean gender;
    private int age;
    private String address;
}
