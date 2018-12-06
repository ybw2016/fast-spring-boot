package com.fast.springcloud.consumer.dto.response;

import com.fast.springcloud.consumer.constant.UserSysErrorConstants;
import com.fast.springcloud.consumer.dto.ResponseBase;
import com.fast.springcloud.consumer.model.BusinessError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @author bowen.yan
 * @date 2018-12-03
 */
@Data
public class UserInfoListRsp extends ResponseBase {
    private List<UserInfoRsp> list;
    private static final Map<String, BusinessError> ERROR_MAP = new HashMap<String, BusinessError>() {
        {
            put("5293", UserSysErrorConstants.PAGE_NO_SIZE_INVALID_ERROR);
            put("6288", UserSysErrorConstants.PAGE_EXCEED_MAX_ERROR);
            put("6299", UserSysErrorConstants.USER_NOT_EXIST_ERROR);
        }
    };

    @Override
    public Map<String, BusinessError> getErrorMap() {
        return ERROR_MAP;
    }
}
