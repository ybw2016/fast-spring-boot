package com.fast.springboot.basic.jvm;

/**
 * @author bw
 * @since 2020-11-30
 */
public class BufferLine64Byte8Variables {
    private static class Padding {
        //7*8字节
        public volatile long p1, p2, p3, p4, p5, p6, p7;
    }

    public static class T extends Padding {
        //8字节
        private volatile long x = 0L;
    }

    private static T[] arr = new T[2];

    static {
        arr[0] = new T();
        arr[1] = new T();
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                //volatile的缓存一致性协议MESI或者锁总线，会消耗时间
                arr[0].x = i;
            }
        });

        Thread thread2 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                arr[1].x = i;
            }
        });
        long startTime = System.nanoTime();
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("总计消耗时间：" + (System.nanoTime() - startTime) / 100_000);
    }
}
