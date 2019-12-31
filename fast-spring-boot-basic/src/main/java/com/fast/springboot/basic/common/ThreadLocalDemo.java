package com.fast.springboot.basic.common;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author bowen.yan
 * @since 2019-12-25
 */
public class ThreadLocalDemo {
    /**
     * The next hash code to be given out. Updated atomically. Starts at
     * zero.
     */
    private static AtomicInteger nextHashCode =
        new AtomicInteger();
    private static final int HASH_INCREMENT = 0x61c88647;
    /**
     * The initial capacity -- MUST be a power of two.
     */
    private static final int INITIAL_CAPACITY = 16;

    private static int nextHashCode() {
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }

    private static int setThreshold(int len) {
        return len * 2 / 3;
    }

    public static void main(String[] args) {
        // nextHashCode() = 0
        System.out.println("nextHashCode() = " + nextHashCode());

        int i = nextHashCode() & (INITIAL_CAPACITY - 1);
        // i = 7
        System.out.println("i = " + i);

        // setThreshold(16) = 10
        System.out.println("setThreshold(16) = " + setThreshold(16));

//        int data = 1640531527;
//        System.out.println(data % 2);

    }
}
