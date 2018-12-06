package com.fast.springcloud.consumer.dto.response;

import com.fast.springcloud.consumer.constant.UserSysErrorConstants;
import com.fast.springcloud.consumer.dto.ResponseBase;
import com.fast.springcloud.consumer.model.BusinessError;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * @author bowen.yan
 * @date 2018-12-03
 */
@Data
public class UserInfoRsp extends ResponseBase {
    private String username;
    private boolean gender;
    private int age;
    private String address;

    @Override
    public Map<String, BusinessError> getErrorMap() {
        return new HashMap<String, BusinessError>() {
            {
                put("5293", UserSysErrorConstants.PAGE_NO_SIZE_INVALID_ERROR);
                put("6288", UserSysErrorConstants.PAGE_EXCEED_MAX_ERROR);
                put("6299", UserSysErrorConstants.USER_NOT_EXIST_ERROR);
            }
        };
    }
}