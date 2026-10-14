package com.fast.springboot.basic.algorithm.bitmap;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author yanbowen
 * @since 2026-10-14
 */
public class LoopTest {
    public static void main(String[] args) {
        int i = 0;
        while (true) {
            try {
                System.out.println("test jvm params, " + ++i);
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
