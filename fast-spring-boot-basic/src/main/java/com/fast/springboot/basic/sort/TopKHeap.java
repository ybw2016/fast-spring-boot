package com.fast.springboot.basic.sort;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * topK算法
 * 经典例子：从1亿个数据中寻找前100个最大的
 *
 * @author bw
 * @since 2020-11-30
 */
public class TopKHeap {
    // 求最大topK，小顶堆算法
    public static List<Integer> getTopKBigDataByHeap(int[] arr, int k) {
        List<Integer> list = new ArrayList<>();
        if (k > arr.length || k == 0) {
            return list;
        }

        // 泛型类型为Integer的优先队列，默认的排序方式是从小到大;
        // 因此：只要队首的数据比前当前新的数据小，则将队首数据出队，将新的数据入队，这样队列里的数据永远就是最大的那几个;
        // 经典的top问题就可这样求解
        Queue<Integer> queue = new PriorityQueue<>();
        for (int num : arr) {
            if (queue.size() < k) {
                queue.add(num);
                // 当前数比堆里的大，则将当前数放入堆
            } else if (queue.peek() < num) {
                queue.poll();
                queue.add(num);
            }
        }
        while (k-- > 0) {
            list.add(queue.poll());
        }
        return list;
    }

    // 求最小topK，大顶堆算法
    public static List<Integer> getTopKSmallDataByHeap(int[] arr, int k) {
        List<Integer> list = new ArrayList<>();
        if (k > arr.length || k == 0) {
            return list;
        }

        Queue<Integer> queue = new PriorityQueue<>(5, new DataWrapper());
        for (int num : arr) {
            if (queue.size() < k) {
                queue.add(num);
                // 最大的比当前元素大，则将当前元素加入到堆中。即：将小的元素往堆里加，堆里永远都是较小的元素
            } else if (queue.peek() > num) {
                queue.poll();
                queue.add(num);
            }
        }
        while (k-- > 0) {
            list.add(queue.poll());
        }
        return list;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{32, 23, 28, 50, 96, 5, 102, 6, 82};
        System.out.println(getTopKBigDataByHeap(arr, 5));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class DataWrapper implements Comparator<Integer> {
        private int data;

        @Override
        public int compare(Integer o1, Integer o2) {
            return o2.compareTo(o1);
        }
    }
}
