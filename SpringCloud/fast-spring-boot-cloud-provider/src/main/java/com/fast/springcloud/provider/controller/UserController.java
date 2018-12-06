package com.fast.springcloud.provider.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.fast.springcloud.provider.annotation.ApiBizController;
import com.fast.springcloud.provider.dto.request.UserQueryReq;
import com.fast.springcloud.provider.dto.response.UserListQueryRsp;
import com.fast.springcloud.provider.dto.response.UserQueryRsp;
import com.fast.springcloud.provider.exception.ApiBizException;

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
import java.util.stream.Stream;

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
        log.info("Spring Cloud Provider getUserInfo receives -> userName:{}, mobile:{}", username, mobile);
        if (StringUtils.isNotEmpty(username)) {
            return USER_MAP.get(username);
        }
        return USER_MAP.get(mobile);
    }

    @PostMapping("/user-info-list")
    @ResponseBody
    public UserListQueryRsp getUserInfoList(@RequestBody UserQueryReq userQueryReq) {
        log.info("Spring Cloud Provider getUserInfoList receives -> userQueryReq:{}", userQueryReq);
        return new UserListQueryRsp(getUserInfoListData(userQueryReq));
    }

    private List<UserQueryRsp> getUserInfoListData(UserQueryReq userQueryReq) {
        if (userQueryReq.getPageNo() == null || userQueryReq.getPageSize() == null) {
            throw new ApiBizException("5293", "pageNo和pageSize必须都大于0");
        }
        if (userQueryReq.getPageNo() > 1) {
            throw new ApiBizException("6288", "pageNo最多为1页");
        }
        Stream<UserQueryRsp> users = USER_MAP.values().stream();
        if (StringUtils.isNotEmpty(userQueryReq.getUsername())) {
            if (USER_MAP.values().stream().noneMatch(user -> userQueryReq.getUsername().equals(user.getUsername()))) {
                throw new ApiBizException("6299", "用户不存在");
            }
            users = users.filter(user -> userQueryReq.getUsername().equals(user.getUsername()));
        }
        return users.skip(userQueryReq.getPageNo() - 1)
                .limit(userQueryReq.getPageSize())
                .collect(Collectors.toList());
    }
}
