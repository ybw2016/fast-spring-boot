package com.fast.springboot.basic.utc.common;

import sun.misc.Unsafe;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author bw
 * @since 2020-11-18
 */
public class UnsafeTest {
    private static long valueOffset;
    private static volatile int value1;

    public static void main(String[] args) {
        final Unsafe unsafe = Unsafe.getUnsafe();
        try {
            // Exception in thread "main" java.lang.SecurityException: Unsafe
            /*
                @CallerSensitive
                public static Unsafe getUnsafe() {
                    Class var0 = Reflection.getCallerClass();

                    // ------> 虚拟机会判断类加载器，用户自定义的类是不受信任的，是不能调用unsafe的;
                    if (!VM.isSystemDomainLoader(var0.getClassLoader())) {
                        throw new SecurityException("Unsafe");
                    } else {
                        return theUnsafe;
                    }
                }
            * */
            valueOffset = unsafe.objectFieldOffset(AtomicInteger.class.getDeclaredField("value1"));
            System.out.println("valueOffset ------>" + valueOffset);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
