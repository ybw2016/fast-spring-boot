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
        calcLongValue();
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

    // long赋值
    private static void calcLongValue() {
        long[] arr = new long[64];
        for (int i = 0; i < 64; i++) {
            //arr[i] = 1 << i;
            arr[i] = (long) Math.pow(2, i);
        }
        for (int i = 0; i < 64; i++) {
            System.out.println(String.format("%s -> %s", i, arr[i]));
        }
        /*
        0 -> 1
        1 -> 2
        2 -> 4
        3 -> 8
        4 -> 16
        5 -> 32
        6 -> 64
        7 -> 128
        8 -> 256
        9 -> 512
        10 -> 1024
        11 -> 2048
        12 -> 4096
        13 -> 8192
        14 -> 16384
        15 -> 32768
        16 -> 65536
        17 -> 131072
        18 -> 262144
        19 -> 524288
        20 -> 1048576
        21 -> 2097152
        22 -> 4194304
        23 -> 8388608
        24 -> 16777216
        25 -> 33554432
        26 -> 67108864
        27 -> 134217728
        28 -> 268435456
        29 -> 536870912
        30 -> 1073741824
        31 -> 2147483648
        32 -> 4294967296
        33 -> 8589934592
        34 -> 17179869184
        35 -> 34359738368
        36 -> 68719476736
        37 -> 137438953472
        38 -> 274877906944
        39 -> 549755813888
        40 -> 1099511627776
        41 -> 2199023255552
        42 -> 4398046511104
        43 -> 8796093022208
        44 -> 17592186044416
        45 -> 35184372088832
        46 -> 70368744177664
        47 -> 140737488355328
        48 -> 281474976710656
        49 -> 562949953421312
        50 -> 1125899906842624
        51 -> 2251799813685248
        52 -> 4503599627370496
        53 -> 9007199254740992
        54 -> 18014398509481984
        55 -> 36028797018963968
        56 -> 72057594037927936
        57 -> 144115188075855872
        58 -> 288230376151711744
        59 -> 576460752303423488
        60 -> 1152921504606846976
        61 -> 2305843009213693952
        62 -> 4611686018427387904
        63 -> 9223372036854775807
        */
    }
}
