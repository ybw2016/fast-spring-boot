package com.fast.springboot.basic.utc.common;

/**
 * CPU 指令重牌
 * happens before
 *
 * @author bw
 * @since 2020-11-18
 */
public class HappensBeforeDemo {
    static int x = 0, y = 0, a = 0, b = 0;
    static int equalCount;

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10000; i++) {
            Thread one = new Thread() {
                public void run() {
                    a = 1;
                    x = b;
                }
            };
            Thread two = new Thread() {
                public void run() {
                    b = 1;
                    y = a;
                }
            };
            one.start();
            two.start();
            one.join();
            two.join();

            System.out.println(x + " " + y);
            if (x == 0 && y == 0) {
                equalCount++;
            }
        }

        System.out.println("recording executed completed! --> equalCount:" + equalCount);
    }

    /*
程序运行结果：（0 0有一定的概率出现）
0 1
0 1
0 1
0 1
0 1
0 1
0 1
0 1
0 1
0 1
0 1
0 1
0 1
0 1
1 0
0 1
0 1
0 1
0 1
0 1
0 1

recording executed completed! --> equalCount:1
    */
}
