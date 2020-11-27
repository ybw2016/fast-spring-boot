package com.fast.springboot.basic.reflect.superextends;

/**
 * @author bw
 * @since 2020-11-27
 */
public class Plate<T> {
    private T item;

    public Plate(T t) {
        item = t;
    }

    public void set(T t) {
        item = t;
    }

    public T get() {
        return item;
    }
}
