package com.fast.springboot.basic.algorithm;

/**
 * 常用位运算：
 * https://zhuanlan.zhihu.com/p/100595139
 * https://blog.csdn.net/hjssss/article/details/86592000
 * https://www.cnblogs.com/fusiwei/p/11384756.html
 *
 * @author bowen.yan
 * @since 2020-01-07
 */
public class BitCalc {
    public static void main(String[] args) {
        swapByCalc();
    }

    private static void swapByBit() {
        int a = 6;  // 0000 0110
        int b = 3;  // 0000 0011
        a ^= b;     // 0000 0101    ->  a=5
        b ^= a;     // 0000 0110    ->  b=6
        a ^= b;     // 0000 0011    ->  a=3

        // 运行结果：a=3, b=6
        System.out.println(String.format("a=%s, b=%s", a, b));
    }

    private static void swapByCalc() {
        int a = 6;
        int b = 3;
        a = a + b;
        b = a - b;
        a = a - b;

        // 运行结果：a=3, b=6
        System.out.println(String.format("a=%s, b=%s", a, b));
    }

    // 偶数
    private static boolean isEven(int n) {
        return (n & 1) == 0;
    }

    // 下面的代码用来判断一个整数是否为奇数
    private static boolean isOdd(int n) {
        return (n & 1) == 1;
    }
}
