package com.fast.springboot.basic.utc.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author bw
 * @since 2020-11-18
 */
public class AtomicIntegerArrayDemo {
    public static void main(String[] args) {
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);
        System.out.println(atomicIntegerArray.get(0));
        System.out.println(atomicIntegerArray.get(3));
        System.out.println(atomicIntegerArray.incrementAndGet(3));
    }
}
