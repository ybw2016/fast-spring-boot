package com.fast.springboot.basic.utc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author bw
 * @since 2020-11-18
 */
public class ConditionDemo {
    public static void main(String[] args) throws InterruptedException {

        ReentrantLock lock = new ReentrantLock();

        lock.lock();

        Condition condition = lock.newCondition();

        Thread thread1 = new Thread(() -> {
            try {
                System.out.println("Thread 1 enters");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("Thread 1 triggered!");

                condition.signal();

                System.out.println("Thread 1 executed done!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            try {
                System.out.println("Thread 2 enters");
                condition.await();
                System.out.println("Thread 2 executed done!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread2.start();

        thread1.join();
        thread2.join();

        lock.unlock();
    }
}
