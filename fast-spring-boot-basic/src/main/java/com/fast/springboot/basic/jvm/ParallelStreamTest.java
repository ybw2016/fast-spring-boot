package com.fast.springboot.basic.jvm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * parallelStream导致数据丢失的问题：
 * https://www.cnblogs.com/sueyyyy/p/13079525.html
 *
 * @author bw
 * @since 2020-12-01
 */
public class ParallelStreamTest {
    public static final int ARRAY_SIZE = 50;

    public static void main(String[] args) {
        testStream();
    }

    public static void testStream() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < ARRAY_SIZE; i++) {
            list.add(i);
        }
        System.out.println("raw size ------> " + list.size());

        // 结果：10000条
        IntStream.rangeClosed(1, 100).forEach(number -> {
            addIntoArrayList_synchronizedList(list);
        });

        // 结果：抛出异常ArrayIndexOutOfBoundsException
        //addIntoArrayList(list);

        // 解决办法
        // 1. 使用parallelStream() -> stream();
        // 2. 使用CopyOnWriteArrayList;
        // 3. 多线程往对象里写的时候加锁;
    }

    private static void addIntoArrayList(List<Integer> rawList) {
        List<Integer> newList = new ArrayList<>();

        /*
        可以看到add方法可以概括为以下两个步骤:
        1. ensureCapacityInternal()，确认下当前ArrayList中的数组，是否还可以加入新的元素。如果不行，
            就会再申请一个：int newCapacity = oldCapacity + (oldCapacity >> 1) 大小的数组（这个容量相当于：1 + 1/2 = 1.5倍），
            然后将数据copy过去。
        2. elementData[size++] = e：添加元素到elementData数组中。
         */
        rawList.stream().forEach(newList::add);
        System.out.println("newList size ------> " + newList.size());
    }

    private static void addIntoConcurrentArrayList(List<Integer> rawList) {
        List<Integer> streamList = new ArrayList<>();
        //List<Integer> newStreamList = Collections.synchronizedList(streamList);
        rawList.parallelStream()
            //.peek(data -> System.out.println(" current data -> " + data))
            .forEach(streamList::add);

        System.out.println("newStreamList size ------> " + streamList.size());

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (streamList.size() < ARRAY_SIZE) {
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

            System.out.println("newStreamList size again ------> " + streamList.size());
        }
    }



    private static void addIntoArrayList_stream(List<Integer> rawList) {
        List<Integer> newList = new ArrayList<>();
        rawList.stream().forEach(newList::add);
        System.out.println("newList size ------> " + newList.size());
    }

    private static void addIntoArrayList_synchronizedList(List<Integer> rawList) {
        List<Integer> newList = new ArrayList<>();
        List<Integer> newStreamList = Collections.synchronizedList(newList);
        rawList.stream().forEach(newStreamList::add);
        System.out.println("newStreamList size ------> " + newStreamList.size());
    }

    private static void addIntoArrayList_CopyOnWriteArrayList(List<Integer> rawList) {
        CopyOnWriteArrayList<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        rawList.parallelStream().forEach(copyOnWriteArrayList::add);
        System.out.println("copyOnWriteArrayList size ------> " + copyOnWriteArrayList.size());
    }
}
