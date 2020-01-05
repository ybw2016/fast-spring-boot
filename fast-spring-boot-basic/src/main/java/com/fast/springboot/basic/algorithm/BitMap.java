package com.fast.springboot.basic.algorithm;

import java.util.stream.IntStream;

/**
 *  参考链接： https://www.jianshu.com/p/e530baada558
 * @author bowen.yan
 * @since 2019-12-31
 */
public class BitMap {
    //保存数据的
    private byte[] bits;

    //能够存储多少数据
    private int capacity;

    public BitMap(int capacity) {
        this.capacity = capacity;

        //1bit能存储8个数据，那么capacity数据需要多少个bit呢，capacity/8+1,右移3位相当于除以8
        bits = new byte[(capacity >> 3) + 1];
    }

    public void add(int number) {
        // num/8得到byte[]的index
        int arrayIndex = number >> 3;
        //int arrayIndex = number / 8;

        // num%8得到在byte[index]的位置
        int position = number & 0x07;
        //int position = number % 8;

        //将1左移position后，那个位置自然就是1，然后和以前的数据做|，这样，那个位置就替换成1了。
        bits[arrayIndex] |= 1 << position;
    }

    public boolean contains(int number) {
        // num/8得到byte[]的index
        int arrayIndex = number >> 3;

        // num%8得到在byte[index]的位置
        int position = number & 0x07;

        //将1左移position后，那个位置自然就是1，然后和以前的数据做&，判断是否为0即可
        return (bits[arrayIndex] & (1 << position)) != 0;
    }

    public void clear(int number) {
        // num/8得到byte[]的index
        int arrayIndex = number >> 3;

        // num%8得到在byte[index]的位置
        int position = number & 0x07;

        //将1左移position后，那个位置自然就是1，然后对取反，再与当前值做&，即可清除当前的位置了.
        bits[arrayIndex] &= ~(1 << position);
        //bits[arrayIndex] &= 0 << position;
    }

    public static void main(String[] args) {
        int endNumber = 10;
        BitMap bitmap = new BitMap(8);
        IntStream.rangeClosed(1, endNumber).forEach(num -> {
            bitmap.add(num);
            System.out.println(String.format("插入%s成功", num));
        });

        IntStream.rangeClosed(1, endNumber).forEach(num -> {
            boolean exist = bitmap.contains(num);
            System.out.println(String.format("%s是否存在:%s", num, exist));
        });

        int number = endNumber;
        bitmap.clear(number);
        boolean exist = bitmap.contains(number);
        System.out.println(String.format("%s是否存在:%s", number, exist));
    }

    /*
    参数链接：https://zhidao.baidu.com/question/1495840591329126139.html
    二进制11111110在计算机中被表示成一个负数。它的值是多少呢？(不是254，而是2)
    因为负数的补码等于其绝对值的原码按位取反再加一，所以如果知道一个负数的补码，
    我们可以先减一再按位取反就得到了该数的绝对值。
    11111110 - 1 = 11111101
    按位取反后得 00000010，即十进制的2.
    所以最后的结果是-2.

=================1.反推======================
原数：     1   1   1   1   1   1   1   0
-1：      1   1   1   1   1   1   0   1
取反：     0   0   0   0   0   0   1   0
十进制：    2
真实数据：  -2

=================2.顺推======================
真实数据：       -2
十进制（正）：    2
二进制：         0   0   0   0   0   0   1   0
取反：           1   1   1   1   1   1   0   1
加1：            1   1   1   1   1   1   1   0
    * */
}
