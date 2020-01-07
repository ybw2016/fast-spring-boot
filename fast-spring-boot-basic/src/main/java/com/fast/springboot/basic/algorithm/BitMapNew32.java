package com.fast.springboot.basic.algorithm;

import java.util.stream.IntStream;

/**
 * 32位存储正常
 *
 * @author bowen.yan
 * @since 2019-12-31
 */
public class BitMapNew32 {
    private int capacity;
    private long[] bitArr;

    private static final int ARRAY_DATA_COUNT = 32;

    public BitMapNew32(int capacity) {
        this.capacity = capacity;
        this.bitArr = new long[(capacity / ARRAY_DATA_COUNT) + 1];
    }

    private int getArrayIndex(int number) {
        return number / ARRAY_DATA_COUNT;
    }

    private int getByteIndex(int number) {
        // number = 9, ARRAY_DATA_COUNT = 8, 期望值：1
        //return number & (ARRAY_DATA_COUNT - 1);
        return number % ARRAY_DATA_COUNT;
    }

    public void add(int number) {
        int arrayIndex = getArrayIndex(number); //向右移3位 = 除以8
        int byteIndex = getByteIndex(number);
        bitArr[arrayIndex] |= 1 << byteIndex;
    }

    public boolean contains(int number) {
        int arrayIndex = getArrayIndex(number);
        long byteIndex = getByteIndex(number);
        return ((bitArr[arrayIndex] >>> byteIndex) & 1) == 1;
    }

    public void clear(int number) {
        int arrayIndex = getArrayIndex(number);
        long byteIndex = getByteIndex(number);
        bitArr[arrayIndex] &= ~(1 << byteIndex);
    }

    private static int SIZE = 100;

    public static void main(String[] args) {
        BitMapNew32 bitMapNew = new BitMapNew32(SIZE);
        testAdd(bitMapNew);
        testClear(bitMapNew);
    }

    private static void testAdd(BitMapNew32 bitMapNew) {
        IntStream.rangeClosed(1, SIZE).forEach(data -> {
            bitMapNew.add(data);
        });

        IntStream.rangeClosed(1, 105).forEach(data -> {
            boolean exist = bitMapNew.contains(data);
            System.out.println(String.format("%s exist: %s", data, exist));
        });

        System.out.println();
    }

    private static void testClear(BitMapNew32 bitMapNew) {
        IntStream.rangeClosed(1, 10).forEach(data -> {
            bitMapNew.clear(data);
        });

        IntStream.rangeClosed(1, 13).forEach(data -> {
            boolean exist = bitMapNew.contains(data);
            System.out.println(String.format("%s exist: %s", data, exist));
        });

        System.out.println();
    }
}
