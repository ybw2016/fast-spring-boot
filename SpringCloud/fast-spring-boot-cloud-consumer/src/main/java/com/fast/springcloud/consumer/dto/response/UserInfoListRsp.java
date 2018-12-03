package com.fast.springcloud.consumer.dto.response;

import java.util.List;

import lombok.Data;

/**
 * @author bowen.yan
 * @date 2018-12-03
 */
@Data
public class UserInfoListRsp {
    private List<UserInfoRsp> list;
}
