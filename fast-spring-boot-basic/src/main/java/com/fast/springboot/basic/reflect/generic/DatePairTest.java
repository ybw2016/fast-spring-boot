package com.fast.springboot.basic.reflect.generic;

import java.util.Date;

/**
 * @author bw
 * @since 2020-11-27
 */
public class DatePairTest {
    public static void main(String[] args) throws ClassNotFoundException {
        DatePair datePair = new DatePair();
        datePair.setValue(new Date());
        // dateInter.setValue(new Object());//编译错误,不允许存放 Object 类型


    }

    private static void test() {
        /*
        ArrayList<String> arrayList = new ArrayList<>();
        if( arrayList instanceof ArrayList<String>) //报错
            if( arrayList instanceof ArrayList<?>) //没报错
        */
    }
}