package com.fast.springboot.basic.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author bw
 * @since 2020-11-11
 */
public class UserInvocationHandler implements InvocationHandler {
    private Object targetObject;

    public UserInvocationHandler(Object targetObject) {
        this.targetObject = targetObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("UserInvocationHandler enhance raw UserService!");
        return method.invoke(targetObject, args);
    }
}
