package com.fast.springcloud.provider.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.fast.springcloud.provider.annotation.ApiBizController;
import com.fast.springcloud.provider.dto.request.UserQueryReq;
import com.fast.springcloud.provider.dto.response.UserListQueryRsp;
import com.fast.springcloud.provider.dto.response.UserQueryRsp;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bowen.yan
 * @date 2018-12-03
 */
@ApiBizController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private static Map<String, UserQueryRsp> USER_MAP = Maps.newHashMap();

    @PostConstruct
    void init() {
        List<UserQueryRsp> users = Lists.newArrayList(
                new UserQueryRsp("zhangsan", true, 30, "beijing"),
                new UserQueryRsp("lisi", false, 26, "shanghai"),
                new UserQueryRsp("wangwu", true, 29, "guangzhou"),
                new UserQueryRsp("zhaoliu", true, 38, "shenzhen"),
                new UserQueryRsp("zhaoliu2", true, 32, "wuhan")
        );
        USER_MAP = users.stream().collect(Collectors.toMap(u -> u.getUsername(), u -> u));
        log.info("init user map -> USER_MAP:{}", USER_MAP);
    }

    @GetMapping("/user-info")
    @ResponseBody
    public UserQueryRsp getUserInfo(@RequestParam String username, @RequestParam String mobile) {
        log.info("Spring Cloud Provider getUserInfo receives -> username:{}, mobile:{}", username, mobile);
        if (StringUtils.isNotEmpty(username)) {
            return USER_MAP.get(username);
        }
        return USER_MAP.get(mobile);
    }

    @PostMapping("/user-info-list")
    @ResponseBody
    public UserListQueryRsp getUserInfoList(@RequestBody UserQueryReq userQueryReq) {
        log.info("Spring Cloud Provider getUserInfoList receives -> userQueryReq:{}", userQueryReq);
        UserListQueryRsp userListQueryRsp = new UserListQueryRsp();
        if (userQueryReq.getPageNo() != null || userQueryReq.getPageSize() != null) {
            List<UserQueryRsp> foundUsers = USER_MAP.values().stream()
                    .skip(userQueryReq.getPageNo() - 1)
                    .limit(userQueryReq.getPageSize())
                    .collect(Collectors.toList());
            userListQueryRsp.setList(foundUsers);
        }
        return userListQueryRsp;
    }
}
