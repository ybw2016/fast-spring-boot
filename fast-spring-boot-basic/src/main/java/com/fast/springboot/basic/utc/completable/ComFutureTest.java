package com.fast.springboot.basic.utc.completable;

import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author bw
 * @since 2020-12-06
 */
public class ComFutureTest {
    public static void main(String[] args) throws Exception {
        //thenCombine();
        parallelExecuteAndJoin();
    }

    private static void thenCombine() throws Exception {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                return "hello1";
            }
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                return "hello2";
            }
        });
        CompletableFuture<String> result = future1.thenCombine(future2, new BiFunction<String, String, String>() {
            @Override
            public String apply(String t, String u) {
                return t + " -- " + u;
            }
        });
        System.out.println(result.get());
    }

    private static void parallelExecuteAndJoin() throws Exception {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(new Supplier<String>() {
            @SneakyThrows
            @Override
            public String get() {
                TimeUnit.SECONDS.sleep(3);
                System.out.println("future1 executed done!");
                return "hello1";
            }
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(new Supplier<String>() {
            @SneakyThrows
            @Override
            public String get() {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("future2 executed done!");
                return "hello2";
            }
        });

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                System.out.println("future3 executed done!");
                return "hello3";
            }
        });

        String result1 = future1.join();
        String result2 = future2.join();
        String result3 = future3.join();

        System.out.println(result1 + " -- " + result2 + " -- " + result3);
    }
}
