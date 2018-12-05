package com.fast.springcloud.provider.dto.request;

import lombok.Data;

/**
 * @author bowen.yan
 * @date 2018-12-03
 */
@Data
public class UserQueryReq extends QueryReqBase {
    private String username;
    private String mobile;

    @Override
    public String toString() {
        return "UserQueryReq{" + super.toString() +
                ", userName='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
