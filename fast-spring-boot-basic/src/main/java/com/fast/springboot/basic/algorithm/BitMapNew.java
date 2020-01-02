package com.fast.springboot.basic.algorithm;

import java.util.stream.IntStream;

/**
 * @author bowen.yan
 * @since 2019-12-31
 */
public class BitMapNew {
    private int capacity;
    private int[] bitArr;

    private static final int ARRAY_DATA_COUNT = 8;

    public BitMapNew(int capacity) {
        this.capacity = capacity;
        this.bitArr = new int[(capacity / ARRAY_DATA_COUNT) + 1];
    }

    private int getArrayIndex(int number) {
        return number / ARRAY_DATA_COUNT;
    }

    private int getByteIndex(int number) {
        // number = 9, ARRAY_DATA_COUNT = 8, 期望值：1
        return number & (ARRAY_DATA_COUNT - 1);
    }

    public void add(int number) {
        int arrayIndex = getArrayIndex(number); //向右移3位 = 除以8
        int byteIndex = getByteIndex(number);
        bitArr[arrayIndex] |= (1 << byteIndex);
    }

    public boolean contains(int number) {
        int arrayIndex = getArrayIndex(number);
        int byteIndex = getByteIndex(number);
        return (bitArr[arrayIndex] & (1 << byteIndex)) != 0;
    }

    public void clear(int number) {
        int arrayIndex = getArrayIndex(number);
        int byteIndex = getByteIndex(number);
        bitArr[arrayIndex] &= ~(1 << byteIndex);
    }

    public static void main(String[] args) {
        BitMapNew bitMapNew = new BitMapNew(2000);
        testAdd(bitMapNew);
        // testClear(bitMapNew);
    }

    private static void testAdd(BitMapNew bitMapNew) {
        IntStream.rangeClosed(1, 200).forEach(data -> {
            bitMapNew.add(data);
        });

        IntStream.rangeClosed(1, 203).forEach(data -> {
            boolean exist = bitMapNew.contains(data);
            System.out.println(String.format("%s exist: %s", data, exist));
        });

        System.out.println();
    }

    private static void testClear(BitMapNew bitMapNew) {
        IntStream.rangeClosed(1, 10).forEach(data -> {
            bitMapNew.clear(data);
        });

        IntStream.rangeClosed(1, 12).forEach(data -> {
            boolean exist = bitMapNew.contains(data);
            System.out.println(String.format("%s exist: %s", data, exist));
        });

        System.out.println();
    }
}
