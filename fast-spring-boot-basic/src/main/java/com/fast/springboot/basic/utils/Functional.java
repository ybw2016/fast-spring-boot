package com.fast.springboot.basic.utils;

import java.util.Collections;
import java.util.List;

/**
 * @author bowen.yan
 * @date 2019-02-26
 */
public class Functional {
    public static void main(String[] args) {
        List<String> userNames = Collections.EMPTY_LIST;
        System.out.println(userNames.stream().anyMatch(userName -> userName.contains("zhangsan"))); // false
        System.out.println(userNames.stream().noneMatch(userName -> userName.contains("zhangsan"))); // true
    }
}
