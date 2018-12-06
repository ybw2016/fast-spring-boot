package com.fast.springcloud.consumer.dto.response;

import com.fast.springcloud.consumer.constant.UserSysErrorConstants;
import com.fast.springcloud.consumer.dto.UserSysRspBizBase;
import com.fast.springcloud.consumer.model.BusinessError;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Data;

/**
 * @author bowen.yan
 * @date 2018-12-03
 */
@Data
public class UserInfoListRsp extends UserSysRspBizBase {
    private List<UserInfoRsp> list;
    private static final Map<String, BusinessError> BIZ_ERROR_MAP = new ConcurrentHashMap<String, BusinessError>() {
        {
            put("5293", UserSysErrorConstants.PAGE_NO_SIZE_INVALID_ERROR);
            put("6288", UserSysErrorConstants.PAGE_EXCEED_MAX_ERROR);
            put("6299", UserSysErrorConstants.USER_NOT_EXIST_ERROR);
        }
    };

    @Override
    public Map<String, BusinessError> getErrorMap() {
        return BIZ_ERROR_MAP;
    }
}
