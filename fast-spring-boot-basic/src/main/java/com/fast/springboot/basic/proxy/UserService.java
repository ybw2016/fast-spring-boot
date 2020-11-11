package com.fast.springboot.basic.proxy;

/**
 * @author bw
 * @since 2020-11-11
 */
public interface UserService {
    void getUerName(String userId);

    void getUerName(String userId, Integer age);
}
