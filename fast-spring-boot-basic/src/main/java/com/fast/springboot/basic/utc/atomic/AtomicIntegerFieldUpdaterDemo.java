package com.fast.springboot.basic.utc.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * AtomicIntegerFieldUpdater更新的字段必须是volatile修改才可以
 *
 * @author bw
 * @since 2020-11-18
 */
public class AtomicIntegerFieldUpdaterDemo {

    private class DemoData {
        public volatile int value1 = 1;
        volatile int value2 = 2;
        protected volatile int value3 = 3;
        private volatile int value4 = 4;
    }

    private AtomicIntegerFieldUpdater<DemoData> getUpdater(String fieldName) {
        return AtomicIntegerFieldUpdater.newUpdater(DemoData.class, fieldName);
    }

    private void doit() {
        DemoData data = new DemoData();
        AtomicIntegerFieldUpdater<DemoData> updater = getUpdater("value1");
        updater.getAndSet(data, 111111);
        System.out.println("value1 ==> " + getUpdater("value1").get(data));

        System.out.println("value2 ==> " + getUpdater("value2").incrementAndGet(data));
        System.out.println("value2 ==> " + getUpdater("value2").get(data));

        System.out.println("value3 ==> " + getUpdater("value3").decrementAndGet(data));
        System.out.println("value3 ==> " + getUpdater("value3").get(data));

        // throw error
        System.out.println("value4 ==> " + getUpdater("value4").compareAndSet(data, 4, 5));
    }

    public static void main(String[] args) {
        AtomicIntegerFieldUpdaterDemo demo = new AtomicIntegerFieldUpdaterDemo();
        demo.doit();
    }
}
