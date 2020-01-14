package com.fast.springboot.basic.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author bowen.yan
 * @since 2019-12-25
 */
public class MiscClass {
    private static final ThreadLocal<String> threadLocalStr = new ThreadLocal<>();
    private static final ThreadLocal<Integer> threadLocalInt = new ThreadLocal<>();

    public static void main(String[] args) {
        //createThreadLocal();
        testThreadLocalMultiThread();
    }

    private static void createThreadLocal() {
        // 同一个Thread中设置两个变量后，table.length会增加2， tab.length: 3 -> 5
        threadLocalStr.set("aaa");
        threadLocalInt.set(1024);

        threadLocalStr.remove();

        System.out.println(threadLocalStr.get());
        System.out.println(threadLocalInt.get());
    }

    private static void testThreadLocalMultiThread() {
        ThreadLocal<String> userNameThreadLocal = new ThreadLocal<>();
        userNameThreadLocal.set("zhangsan");

        InheritableThreadLocal<String> userNameThreadLocalInheritable = new InheritableThreadLocal<>();
        userNameThreadLocalInheritable.set("lisi");

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(() -> {
            System.out.println("userNameThreadLocal -> " + userNameThreadLocal.get());
            System.out.println("userNameThreadLocalInheritable -> " + userNameThreadLocalInheritable.get());
        });
    }
}
