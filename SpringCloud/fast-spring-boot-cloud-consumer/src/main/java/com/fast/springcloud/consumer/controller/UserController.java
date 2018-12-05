package com.fast.springcloud.consumer.controller;

import com.fast.springcloud.consumer.api.UserServiceClient;
import com.fast.springcloud.consumer.dto.request.UserInfoReq;
import com.fast.springcloud.consumer.dto.response.UserInfoListRsp;
import com.fast.springcloud.consumer.dto.response.UserInfoRsp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bowen.yan
 * @date 2018-11-14
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserServiceClient userServiceClient;

    @GetMapping("/user-info")
    public UserInfoRsp getUserInfoData(@RequestParam("username") String username, @RequestParam("mobile") String mobile) {
        return userServiceClient.queryUserInfo(username, mobile);
    }

    @PostMapping("/user-info-list")
    public UserInfoListRsp getUserInfoListData(@RequestBody UserInfoReq userInfoReq) {
        return userServiceClient.queryUserListInfo(userInfoReq);
    }

    @PostMapping("/user-info-list2")
    public List<UserInfoRsp> getUserInfoListData2(@RequestBody UserInfoReq userInfoReq) {
        return userServiceClient.queryUserListInfo2(userInfoReq);
    }
}
