package com.fast.springcloud.consumer.dto;

import com.fast.springcloud.consumer.constant.UserSysErrorConstants;
import com.fast.springcloud.consumer.model.BusinessError;

import lombok.Data;

/**
 * 公共返回报文头
 *
 * @author bowen.yan
 * @date 2018-12-03
 */
@Data
public class ResponseBase implements BizErrorMapping {
    private BusinessError businessError;

    public boolean success() {
        return UserSysErrorConstants.BIZ_SUCCESS.equals(businessError);
    }
}