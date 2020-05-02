package com.fast.springboot.basic.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @author bowen.yan
 * @since 2020-04-24
 */
public class TtlSimpleDemo {
    public static void main(String[] args) {
        TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();
        context.set("zhangsan");

        System.out.println(Thread.currentThread().getName() + " -> " + context.get());
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " -> " + context.get());
        }).start();
    }
}
