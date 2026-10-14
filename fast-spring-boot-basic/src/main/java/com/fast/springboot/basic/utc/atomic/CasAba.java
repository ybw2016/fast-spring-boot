package com.fast.springboot.basic.utc.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 *
 * @author yanbowen
 * @since 2026-09-26
 */
public class CasAba {
    private static class AtomicIntegerExistAba {
        // AtomicInteger也是会出现ABA
        private static AtomicInteger atomicInteger = new AtomicInteger(100);
        private static AtomicStampedReference<Integer> atomicStampedRef = new AtomicStampedReference<Integer>(100, 0);

        public static void main(String[] args) throws InterruptedException {
            // 其中一个线程改了，又改回去
            Thread intT1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    atomicInteger.compareAndSet(100, 101);
                    // 这期间处理业务,比如说调用业务接口
                    if (atomicInteger.get() == 101) {
                        System.out.println("todoAAA");
                    }
                    atomicInteger.compareAndSet(101, 100);
                }
            });

            // 另一个线程由于等了一会，这个间隙intT1已经操作了这个数据，但是intT2不清楚的，以为值还是之前的，导致修改成功
            Thread intT2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    boolean flag = atomicInteger.compareAndSet(100, 101);// true
                    System.out.println("thread intT2: " + atomicInteger);
                    System.out.println("flag is " + flag);
                    if (atomicInteger.get() == 101) {
                        System.out.println("todoAAA");
                    }
                }
            });

            intT1.start();
            intT2.start();
        }
    }

    private static class AtomicStampedReferenceNotExistAba {
        private static AtomicStampedReference<Integer> atomicStampedRef = new AtomicStampedReference<Integer>(100, 0);

        public static void main(String[] args) throws InterruptedException {
            Thread refT1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    atomicStampedRef.compareAndSet(100, 101, atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1);
                    atomicStampedRef.compareAndSet(101, 100, atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1);
                }
            });

            Thread refT2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    int stamp = atomicStampedRef.getStamp();
                    System.out.println("before sleep : stamp = " + stamp);
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("after sleep : stamp = " + atomicStampedRef.getStamp());
                    boolean flag = atomicStampedRef.compareAndSet(100, 101, stamp, stamp + 1);
                    System.out.println("thread refT2: " + atomicStampedRef.getReference() + ", flag is " + flag);
                }
            });
            refT1.start();
            refT2.start();
        }
    }
}
