package com.fast.springboot.basic.optimize;

import java.math.BigDecimal;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author bowen.yan
 * @since 2020-04-26
 */
public class AliOptimize {
    public static void main(String[] args) {
        verifyCalc();
    }

    private static void calcBasic() {
        // 1 金额
        Double money = 1.21D;
        System.out.println(new BigDecimal(money)); // wrong
        System.out.println(new BigDecimal(Double.toString(money))); // correct

        // 2 线程创建
        ExecutorService executorService = Executors.newFixedThreadPool(10);// wrong  容易OOM
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            5,
            10,
            5,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(10)); // correct

        // 3 自增计算器
        AtomicLong atomicLong = new AtomicLong();// wrong
        LongAdder longAdder = new LongAdder();// correct

        // 4 ThreadLocalRandom取代Random
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
    }

    private static void verifyCalc() {
        Integer a = 1;
        Integer b = 2;
        Integer c = null;
        Boolean flag = false;
        // a*b的结果是int类型，那么c会强制拆箱成int类型，抛出NPE异常
        Integer result = (flag ? a * b : c);
        System.out.println(result);
    }
}
