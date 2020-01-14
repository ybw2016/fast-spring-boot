package com.fast.springboot.basic.algorithm;

import java.util.stream.IntStream;

/**
 * 64位存储时有问题，在number=31时会有溢出问题（已解决，lin36行：(long) 1 << byteIndex）
 *
 * @author bowen.yan
 * @since 2019-12-31
 */
public class BitMapNew64 {
    private int capacity;
    private long[] bitArr;

    private static final int ARRAY_DATA_COUNT = 64;

    public BitMapNew64(int capacity) {
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
        // 注意：此处需要用long强制转型，否则移位后的结果会变int型溢出
        bitArr[arrayIndex] |= (long) 1 << byteIndex;
    }

    public boolean contains(int number) {
        int arrayIndex = getArrayIndex(number);
        long byteIndex = getByteIndex(number);
        return ((bitArr[arrayIndex] >>> byteIndex) & 1) == 1;
        //return (bitArr[arrayIndex] & (1 << byteIndex)) != 0;
    }

    public void clear(int number) {
        int arrayIndex = getArrayIndex(number);
        long byteIndex = getByteIndex(number);
        bitArr[arrayIndex] &= ~(1 << byteIndex);
    }

    private static int SIZE = 100;

    public static void main(String[] args) {
        BitMapNew64 bitMapNew = new BitMapNew64(SIZE);
        testAdd(bitMapNew);
        //testClear(bitMapNew);
    }

    private static void testAdd(BitMapNew64 bitMapNew) {
        IntStream.rangeClosed(1, SIZE).forEach(data -> {
            bitMapNew.add(data);
        });

        IntStream.rangeClosed(1, 103).forEach(data -> {
            boolean exist = bitMapNew.contains(data);
            System.out.println(String.format("%s exist: %s", data, exist));
        });

        System.out.println();
    }

    private static void testClear(BitMapNew64 bitMapNew) {
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
