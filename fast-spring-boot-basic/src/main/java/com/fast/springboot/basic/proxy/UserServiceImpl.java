package com.fast.springboot.basic.proxy;

/**
 * @author bw
 * @since 2020-11-11
 */
public class UserServiceImpl implements UserService {

    @Override
    public void getUerName(String userId) {
        System.out.println(String.format("userId:%s -> userName:%s", userId, userId.toUpperCase()));
    }

    @Override
    public void getUerName(String userId, Integer age) {
        System.out.println(String.format("userId:%s -> userName:%s, age:%s",
            userId, userId.toUpperCase(), age));
    }
}
