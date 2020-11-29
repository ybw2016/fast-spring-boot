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

    /*  编译后泛型擦除:

    bw$ javap -c Pair.class
    Compiled from "Pair.java"
    public class com.fast.springboot.basic.reflect.generic.Pair<T> {
  public com.fast.springboot.basic.reflect.generic.Pair();
        Code:
        0: aload_0
        1: invokespecial #1                  // Method java/lang/Object."<init>":()V
        4: return

        public T getValue();
        Code:
        0: aload_0
        1: getfield      #2                  // Field value:Ljava/lang/Object;
        4: areturn

        public void setValue(T);
        Code:
        0: aload_0
        1: aload_1
        2: putfield      #2                  // Field value:Ljava/lang/Object;
        5: return
    }
*/
}
