package com.fast.springcloud.consumer.constant;

import com.fast.springcloud.consumer.model.BusinessError;

/**
 * @author bowen.yan
 * @date 2018-12-05
 */
public class UserSysErrorConstants {
    public static final BusinessError BIZ_SUCCESS = new BusinessError("0", "成功");
    public static final BusinessError PAGE_NO_SIZE_INVALID_ERROR = new BusinessError("100001", "页码、每页条数有误");
    public static final BusinessError PAGE_EXCEED_MAX_ERROR = new BusinessError("100002", "最多只能获取1页的数据");
    public static final BusinessError USER_NOT_EXIST_ERROR = new BusinessError("100003", "当前输入的用户不存在");
}
