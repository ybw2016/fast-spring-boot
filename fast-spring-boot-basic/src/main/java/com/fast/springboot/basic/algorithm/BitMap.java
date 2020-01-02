package com.fast.springboot.basic.algorithm;

import java.util.stream.IntStream;

/**
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

        // num%8得到在byte[index]的位置
        int position = number & 0x07;

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
    }

    public static void main(String[] args) {
        BitMap bitmap = new BitMap(100);
        IntStream.rangeClosed(1, 100).forEach(num -> {
            bitmap.add(num);
            System.out.println(String.format("插入%s成功", num));
        });

        IntStream.rangeClosed(1, 102).forEach(num -> {
            boolean exist = bitmap.contains(num);
            System.out.println(String.format("%s是否存在:%s", num, exist));
        });

//        bitmap.clear(number);
//        exist = bitmap.contains(number);
//        System.out.println(String.format("%s是否存在:%s", number, exist));
    }
}
