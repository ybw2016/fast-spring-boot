package com.fast.springboot.basic.common;

import com.fast.springboot.basic.model.User;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author bowen.yan
 * @since 2019-12-25
 */
public class MiscClass {
    public static void main(String[] args) {

        ThreadLocal<String> userNameThreadLocal = new ThreadLocal<>();
        userNameThreadLocal.set("zhangsan");

        InheritableThreadLocal<String> userNameThreadLocalInheritable = new InheritableThreadLocal<>();
        userNameThreadLocalInheritable.set("lisi");

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(() -> {
            System.out.println("userNameThreadLocal -> " + userNameThreadLocal.get());
            System.out.println("userNameThreadLocalInheritable -> " + userNameThreadLocalInheritable.get());
        });

        //testCreate();
    }

    private static void testCreate() {
        User user = User.of().userName("zhangsan").mobile("13811112222");
        User newUser = null;

        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();

        Constructor<? extends User> userConstructor =
            ClassUtils.getConstructorIfAvailable(user.getClass(), user.getClass());
        if (userConstructor != null) {
            try {
                ReflectionUtils.makeAccessible(userConstructor);
                newUser = userConstructor.newInstance(user);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // public com.fast.springboot.basic.model.User(com.fast.springboot.basic.model.User)
        System.out.println(userConstructor);
        // User(userName=zhangsan-1, mobile=13811112222-1)
        System.out.println(newUser);
    }
}
