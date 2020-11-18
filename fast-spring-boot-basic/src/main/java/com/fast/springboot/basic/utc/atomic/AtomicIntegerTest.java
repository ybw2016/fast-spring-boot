package com.fast.springboot.basic.utc.atomic;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * @author bw
 * @since 2020-11-18
 */
public class AtomicIntegerTest {
    @Test
    public void testAll() throws InterruptedException {
        final AtomicInteger value = new AtomicInteger(10);
        assertEquals(value.compareAndSet(10, 2), true);
        assertEquals(value.compareAndSet(2, 3), true);
        assertEquals(value.compareAndSet(3, 10), true);
        // 期望值应该为3，而当前为111，显然不相等，则compareAndSwap失败
        assertEquals(value.compareAndSet(111, 222), false);

        // 当前的值已重新置为10
        assertEquals(value.get(), 10);
        Assert.assertTrue(value.compareAndSet(10, 3));
        assertEquals(value.get(), 3);
        value.set(0);

        assertEquals(value.incrementAndGet(), 1);
        assertEquals(value.getAndAdd(2), 1);
        assertEquals(value.getAndSet(5), 3);
        assertEquals(value.get(), 5);

        // value的值已经为5
        // 现在用10个线程来同时update value
        final int threadSize = 10;
        Thread[] ts = new Thread[threadSize];
        for (int i = 0; i < threadSize; i++) {
            ts[i] = new Thread() {
                public void run() {
                    // 可以看一下源码
                    /*
                      第一步：
                        public class AtomicInteger{
                            private static final Unsafe unsafe = Unsafe.getUnsafe();
                            private static final long valueOffset;

                            static {
                                try {
                                // ------> 将AtomicInteger类的类成员变量value的偏移地址获取到，以便后面获取value的值
                                // ------> 具体是指value这个字段在AtomicInteger类的内存中相对于[该类首地址]的偏移量
                                    valueOffset = unsafe.objectFieldOffset(AtomicInteger.class.getDeclaredField("value"));
                                } catch (Exception ex) { throw new Error(ex); }
                            }

                            // ------> volatile关键字保证了在多线程中value的值是可见的，任何一个线程修改了value值，会将其立即写回内存当中
                            private volatile int value;
                        }

                        第二步：
                        public final int incrementAndGet() {
                            return unsafe.getAndAddInt(this, valueOffset, 1) + 1;
                        }

                        第三步：
                          public final int getAndAddInt(Object var1, long var2, int var4) {
                            int var5;
                            do {
                                // ------> 获取value的值，因为value用volatile修饰，具有内存可见性，能不断拿到新值
                                var5 = this.getIntVolatile(var1, var2);
                            }
                            // ------> CAS获取失败则不断循环重新获取、直到CAS成功
                            while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

                            return var5;
                        }
                     */

                    value.incrementAndGet();
                }
            };
        }

        for (Thread t : ts) {
            t.start();
        }
        for (Thread t : ts) {
            t.join();
        }

        System.out.println(String.format("value:%s, 5+threadSize:%s", value.get(), (5 + threadSize)));
        assertEquals(value.get(), 5 + threadSize);
    }
}
