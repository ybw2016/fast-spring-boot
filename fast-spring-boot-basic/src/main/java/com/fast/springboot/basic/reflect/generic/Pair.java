package com.fast.springboot.basic.reflect.generic;

/**
 * @author bw
 * @since 2020-11-27
 */
public class Pair<T> {
    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
